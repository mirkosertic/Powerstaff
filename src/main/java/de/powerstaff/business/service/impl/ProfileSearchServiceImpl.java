/*
  Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.

  This library is free software; you can redistribute it and/or modify it under
  the terms of the GNU Lesser General Public License as published by the Free
  Software Foundation; either version 2.1 of the License, or (at your option)
  any later version.

  This library is distributed in the hope that it will be useful, but WITHOUT
  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
  FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
  details.

  You should have received a copy of the GNU Lesser General Public License
  along with this library; if not, write to the Free Software Foundation, Inc.,
  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.powerstaff.business.service.impl;

import de.mogwai.common.usercontext.UserContextHolder;
import de.powerstaff.business.dao.ProfileSearchDAO;
import de.powerstaff.business.dto.DataPage;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.dto.ProfileSearchInfoDetail;
import de.powerstaff.business.entity.*;
import de.powerstaff.business.lucene.analysis.ProfileAnalyzerFactory;
import de.powerstaff.business.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SpanGradientFormatter;
import org.apache.lucene.search.similar.MoreLikeThis;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.store.DirectoryProvider;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Mirko Sertic
 */
public class ProfileSearchServiceImpl implements
        ProfileSearchService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ProfileSearchServiceImpl.class);

    static {
        BooleanQuery.setMaxClauseCount(8192);
    }

    private PowerstaffSystemParameterService systemParameterService;

    private ProfileSearchDAO profileSearchDAO;

    private SessionFactory sessionFactory;

    private FSCache fileSystemCache;

    public void setFileSystemCache(final FSCache fileSystemCache) {
        this.fileSystemCache = fileSystemCache;
    }

    public void setSystemParameterService(
            final PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    public void setProfileSearchDAO(final ProfileSearchDAO profileSearchDAO) {
        this.profileSearchDAO = profileSearchDAO;
    }

    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Query getRealQuery(final SavedProfileSearch aRequest, final Analyzer aAnalyzer)
            throws IOException, ParseException {

        final BooleanQuery theQuery = new BooleanQuery();

        final GoogleStyleQueryParser theParser = new GoogleStyleQueryParser(null);

        theQuery.add(theParser.parseQuery(aRequest.getProfileContent(),
                aAnalyzer, ProfileIndexerService.CONTENT), Occur.MUST);

        if (!StringUtils.isEmpty(aRequest.getPlz())) {
            theQuery.add(new WildcardQuery(new Term(ProfileIndexerService.PLZ,
                    aRequest.getPlz().replace("%", "*"))), Occur.MUST);
        }

        return theQuery;
    }

    protected String getHighlightedSearchResult(final Analyzer aAnalyzer,
                                                final Highlighter aHighlighter, final String aContent, final Query aQuery)
            throws IOException, InvalidTokenOffsetsException {

        final CachingTokenFilter tokenStream = new CachingTokenFilter(aAnalyzer
                .tokenStream(ProfileIndexerService.CONTENT, new StringReader(
                        aContent)));

        return aHighlighter.getBestFragments(tokenStream, aContent, 5,
                "&nbsp;...&nbsp;");
    }

    public SavedProfileSearch getSearchRequestForUser(final String aUserId) {
        return profileSearchDAO.getSavedSearchForUser(aUserId);
    }

    public SavedProfileSearch getSearchRequest(final long aSearchRequestId) {
        return profileSearchDAO.getSavedSearchById(aSearchRequestId);
    }

    @Override
    public void saveSearchRequest(final SavedProfileSearch searchRequest, final boolean cleanup) throws OptimisticLockException {

        final User theUser = (User) UserContextHolder.getUserContext().getAuthenticatable();

        if (cleanup) {
            searchRequest.getProfilesToIgnore().clear();
        }

        // If the search was for a project, it will also be saved as the last search for a user
        if (searchRequest.getProject() != null) {

            boolean isNew = false;
            SavedProfileSearch theSearchForUser = profileSearchDAO.getSavedSearchForUser(theUser.getUsername());
            if (theSearchForUser == null) {
                theSearchForUser = new SavedProfileSearch();
                theSearchForUser.setUser(theUser);
                isNew = true;
            } else {
                if (cleanup) {
                    searchRequest.getProfilesToIgnore().clear();
                }
            }

            theSearchForUser.setPlz(searchRequest.getPlz());
            theSearchForUser.setProfileContent(searchRequest.getProfileContent());
            theSearchForUser.setSortierung(searchRequest.getSortierung());
            theSearchForUser.setStundensatzVon(searchRequest.getStundensatzVon());
            theSearchForUser.setStundensatzBis(searchRequest.getStundensatzBis());
            theSearchForUser.getProfilesToIgnore().addAll(searchRequest.getProfilesToIgnore());
            theSearchForUser.setSelectedTags(searchRequest.getSelectedTags());

            if (isNew) {
                // Auf keinen Fall die bereits existierende Id vom SavedSearchRequest pro projekt äbernehmen!
                theSearchForUser.setId(null);
            }

            // Keine Zuordnung zu einem Projekt!
            theSearchForUser.setProject(null);

            profileSearchDAO.save(theSearchForUser);
        }

        profileSearchDAO.save(searchRequest);
    }

    @Override
    public DataPage<ProfileSearchEntry> findProfileDataPage(
            final SavedProfileSearch aRequest, final int startRow, final int pageSize)
            throws Exception {

        if (aRequest.getId() == null) {
            // Kann passieren, wenn die Suche das erste mal aufgerufen wird
            return new DataPage<>(0, 0, new ArrayList<>());
        }

        final Analyzer theAnalyzer = ProfileAnalyzerFactory.createAnalyzer();

        final FullTextSession theSession = Search.getFullTextSession(sessionFactory.getCurrentSession());

        final Query theQuery = getRealQuery(aRequest, theAnalyzer);

        LOGGER.info("Search query is " + theQuery + " from " + startRow
                + " with pagesize " + pageSize);

        final Highlighter theHighlighter = new Highlighter(new SpanGradientFormatter(
                1, "#000000", "#0000FF", null, null), new QueryScorer(theQuery));

        final BooleanQuery theRealQuery = new BooleanQuery();
        theRealQuery.add(theQuery, Occur.MUST);

        for (final String theId : aRequest.getProfilesToIgnore()) {
            theRealQuery.add(new TermQuery(new Term(
                ProfileIndexerService.UNIQUE_ID, theId)),
                Occur.MUST_NOT);
        }

        if (aRequest.getSelectedTags() != null) {
            final String[] tags = aRequest.getSelectedTags().split(";");
            BooleanQuery orQuery = null;
            for (final String tag : tags) {
                if (!StringUtils.isEmpty(tag)) {
                    if (orQuery == null) {
                        orQuery = new BooleanQuery();
                    }
                    orQuery.add(new TermQuery(new Term(ProfileIndexerService.TAGS, tag)), Occur.SHOULD);
                }
            }
            if (orQuery != null) {
                theRealQuery.add(orQuery, Occur.MUST);
            }
        }

        LOGGER.info("Query with ignore is " + theRealQuery);

        Sort theSort = null;
        if (!StringUtils.isEmpty(aRequest.getSortierung())) {
            int theSortType = SortField.STRING;
            boolean theReverse = false;

            final String theSortField = aRequest.getSortierungField();

            if (ProfileIndexerService.STUNDENSATZ.equals(theSortField)) {
                theSortType = SortField.LONG;
            }
            if (ProfileIndexerService.VERFUEGBARKEIT.equals(theSortField)) {
                theReverse = true;
            }
            if (ProfileIndexerService.LETZTERKONTAKT.equals(theSortField)) {
                theReverse = true;
            }

            if (aRequest.isSortierungReverse()) {
                theReverse = !theReverse;
            }

            theSort = new Sort(new SortField(theSortField,
                    theSortType, theReverse));
        }

        final List<Filter> theFilterList = new ArrayList<>();
        final TermsFilter theContactForbidden = new TermsFilter();
        theContactForbidden.addTerm(new Term(ProfileIndexerService.KONTAKTSPERRE, "false"));
        theFilterList.add(theContactForbidden);

        if (aRequest.getStundensatzVon() != null
                || aRequest.getStundensatzBis() != null) {
            if (aRequest.getStundensatzVon() != null) {
                theFilterList.add(NumericRangeFilter.newLongRange(
                        ProfileIndexerService.STUNDENSATZ, aRequest
                        .getStundensatzVon(), Long.MAX_VALUE, true,
                        true));
            }
            if (aRequest.getStundensatzBis() != null) {
                theFilterList.add(NumericRangeFilter.newLongRange(
                        ProfileIndexerService.STUNDENSATZ, 0L, aRequest
                        .getStundensatzBis(), true, true));
            }
        }

        final Filter theFilter = new ChainedFilter(theFilterList
                .toArray(new Filter[0]), ChainedFilter.AND);


        final int theEnd = startRow + pageSize;

        final FullTextQuery theHibernateQuery = theSession.createFullTextQuery(theRealQuery, Freelancer.class);
        theHibernateQuery.setFilter(theFilter);

        if (theSort != null) {
            theHibernateQuery.setSort(theSort);
        }
        theHibernateQuery.setFirstResult(startRow);
        theHibernateQuery.setMaxResults(theEnd - startRow);
        theHibernateQuery.setProjection(FullTextQuery.THIS, FullTextQuery.DOCUMENT);

        final List<ProfileSearchEntry> theResult = new ArrayList<>();

        for (final Object theSingleEntity : theHibernateQuery.list()) {
            final Object[] theRow = (Object[]) theSingleEntity;
            final Freelancer theFreelancer = (Freelancer) theRow[0];
            final Document theDocument = (Document) theRow[1];
            final ProfileSearchEntry theEntry = createResultEntry(theAnalyzer, theQuery, theHighlighter, theFreelancer, theDocument);

            theResult.add(theEntry);
        }

        return new DataPage<>(theHibernateQuery.getResultSize(), startRow,
                theResult);
    }

    private ProfileSearchEntry createResultEntry(final Analyzer aAnalyzer, final Query aQuery, final Highlighter aHighlighter, final Freelancer aFreelancer, final Document aDocument) throws IOException, InvalidTokenOffsetsException {
        final ProfileSearchEntry theEntry = new ProfileSearchEntry();
        theEntry.setCode(aFreelancer.getCode());
        theEntry.setDocumentId("" + aFreelancer.getId());

        final ProfileSearchInfoDetail theDetail = new ProfileSearchInfoDetail();
        theDetail.setId(aFreelancer.getId());
        theDetail.setName1(aFreelancer.getName1());
        theDetail.setName2(aFreelancer.getName2());
        theDetail.setAvailability(aFreelancer
                .getAvailabilityAsDate());
        theDetail.setPlz(aFreelancer.getPlz());
        theDetail.setStundensatz(aFreelancer.getSallaryLong());
        theDetail.setContactforbidden(aFreelancer
                .isContactforbidden());
        theDetail.setContacts(new ArrayList<>(
                aFreelancer.getContacts()));
        theDetail.setLastContact(aFreelancer.getLastContactDate());
        for (final FreelancerToTag theTag : aFreelancer.getTags()) {
            theDetail.getTags().add(theTag.getTag());
        }

        final String theContent = aDocument.get(ProfileIndexerService.ORIG_CONTENT);
        theEntry.setHighlightResult(getHighlightedSearchResult(aAnalyzer,
                aHighlighter, theContent, aQuery));

        theEntry.setFreelancer(theDetail);

        return theEntry;
    }

    @Override
    public void removeSavedSearchEntry(final SavedProfileSearch searchRequest, final String aDocumentId) throws OptimisticLockException {

        final SavedProfileSearch theSearch = profileSearchDAO.getSavedSearchById(searchRequest.getId());

        theSearch.getProfilesToIgnore().add(aDocumentId);

        profileSearchDAO.save(theSearch);
    }

    @Override
    public int getPageSize() {
        return systemParameterService.getMaxSearchResult();
    }


    @Override
    public synchronized List<FreelancerProfile> loadProfilesFor(final Freelancer aFreelancer) {
        final List<FreelancerProfile> theProfiles = new ArrayList<>();

        if (aFreelancer != null && aFreelancer.getCode() != null) {

            final String theCode = aFreelancer.getCode().trim().toLowerCase();
            final Set<File> theFilesForCode = fileSystemCache.getFilesForCode(theCode);

            if (theFilesForCode != null) {

                String theClientBaseDir = systemParameterService.getIndexerNetworkDir();
                if (!theClientBaseDir.endsWith("\\")) {
                    theClientBaseDir += "\\";
                }
                String theServerBaseDir = systemParameterService.getIndexerSourcePath();
                if (!theServerBaseDir.endsWith("\\")) {
                    theServerBaseDir += "\\";
                }

                final SimpleDateFormat theFormat = new SimpleDateFormat("dd.MM.yyyy");

                for (final File theFile : theFilesForCode) {
                    final FreelancerProfile theProfile = new FreelancerProfile();
                    theProfile.setName(theFile.getName());
                    theProfile.setInfotext("Aktualisiert : " + theFormat.format(new Date(theFile.lastModified())));
                    theProfile.setFileOnserver(theFile);

                    final String theStrippedPath = theFile.toString().substring(
                            theServerBaseDir.length());
                    theProfile.setFileName(theClientBaseDir + theStrippedPath);

                    theProfiles.add(theProfile);
                }
            }

        }
        return theProfiles;
    }

    @Override
    public List<ProfileSearchEntry> getSimilarFreelancer(final Freelancer aFreelancer) {
        final List<ProfileSearchEntry> theResult = new ArrayList<>();
        if (aFreelancer != null && aFreelancer.getId() != null) {

            final FullTextSession theSession = Search.getFullTextSession(sessionFactory.getCurrentSession());
            final SearchFactory theSearchFactory = theSession.getSearchFactory();

            final Analyzer theAnalyzer = ProfileAnalyzerFactory.createAnalyzer();

            final DirectoryProvider<?> theFreeelancerProvider = theSearchFactory.getDirectoryProviders(Freelancer.class)[0];

            IndexReader theIndexReader = null;

            try {
                theIndexReader = theSearchFactory.getReaderProvider().openReader(theFreeelancerProvider);

                final MoreLikeThis theMoreLikeThis = new MoreLikeThis(theIndexReader);

                // Zuerst den Freiberufler raussuchen
                final Query theQuery = new TermQuery(new Term(ProfileIndexerService.UNIQUE_ID, aFreelancer.getId().toString()));
                final FullTextQuery theHibernateQuery = theSession.createFullTextQuery(theQuery, Freelancer.class);
                theHibernateQuery.setProjection(FullTextQuery.THIS, FullTextQuery.DOCUMENT);

                for (final Object theSingleEntity : theHibernateQuery.list()) {
                    final Object[] theRow = (Object[]) theSingleEntity;
                    final Freelancer theFreelancer = (Freelancer) theRow[0];
                    final Document theDocument = (Document) theRow[1];

                    theMoreLikeThis.setMinDocFreq(1);
                    theMoreLikeThis.setMinTermFreq(1);
                    theMoreLikeThis.setAnalyzer(theAnalyzer);
                    theMoreLikeThis.setFieldNames(new String[]{ProfileIndexerService.CONTENT});
                    final Query theMltQuery = theMoreLikeThis.like(new StringReader(theDocument.get(ProfileIndexerService.ORIG_CONTENT)));

                    final FullTextQuery theMoreLikeThisQuery = theSession.createFullTextQuery(theMltQuery, Freelancer.class);
                    theMoreLikeThisQuery.setProjection(FullTextQuery.THIS, FullTextQuery.DOCUMENT, FullTextQuery.SCORE);
                    theMoreLikeThisQuery.setMaxResults(50);

                    final Highlighter theHighlighter = new Highlighter(new SpanGradientFormatter(
                            1, "#000000", "#0000FF", null, null), new QueryScorer(theMltQuery));

                    for (final Object theSingleMltEntry : theMoreLikeThisQuery.list()) {
                        final Object[] theMltRow = (Object[]) theSingleMltEntry;
                        final Freelancer theMltFreelancer = (Freelancer) theMltRow[0];
                        final Document theMltDocument = (Document) theMltRow[1];
                        final Float theMltScore = (Float) theMltRow[2];

                        if (theMltFreelancer != theFreelancer) {
                            // Einen gefunden
                            final ProfileSearchEntry theEntry = createResultEntry(theAnalyzer, theMltQuery, theHighlighter, theMltFreelancer, theMltDocument);
                            theResult.add(theEntry);
                        }
                    }

                }
            } catch (final Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (theIndexReader != null) {
                    theSearchFactory.getReaderProvider().closeReader(theIndexReader);
                }
            }

        }
        return theResult;
    }

    @Override
    public List<ProfileSearchEntry> getSimilarFreelancer(final Project aProject) {
        final List<ProfileSearchEntry> theResult = new ArrayList<>();
        if (aProject != null && aProject.getId() != null) {

            final FullTextSession theSession = Search.getFullTextSession(sessionFactory.getCurrentSession());
            final SearchFactory theSearchFactory = theSession.getSearchFactory();

            final Analyzer theAnalyzer = ProfileAnalyzerFactory.createAnalyzer();

            final DirectoryProvider<?> theFreeelancerProvider = theSearchFactory.getDirectoryProviders(Freelancer.class)[0];

            IndexReader theIndexReader = null;

            try {
                theIndexReader = theSearchFactory.getReaderProvider().openReader(theFreeelancerProvider);

                final MoreLikeThis theMoreLikeThis = new MoreLikeThis(theIndexReader);

                theMoreLikeThis.setMinDocFreq(1);
                theMoreLikeThis.setMinTermFreq(1);
                theMoreLikeThis.setAnalyzer(theAnalyzer);
                theMoreLikeThis.setFieldNames(new String[]{ProfileIndexerService.CONTENT});
                final Query theMltQuery = theMoreLikeThis.like(new StringReader(aProject.getDescriptionShort() + " " + aProject.getDescriptionLong() + " " + aProject.getSkills()));

                final FullTextQuery theMoreLikeThisQuery = theSession.createFullTextQuery(theMltQuery, Freelancer.class);
                theMoreLikeThisQuery.setProjection(FullTextQuery.THIS, FullTextQuery.DOCUMENT, FullTextQuery.SCORE);
                theMoreLikeThisQuery.setMaxResults(50);

                final Highlighter theHighlighter = new Highlighter(new SpanGradientFormatter(
                        1, "#000000", "#0000FF", null, null), new QueryScorer(theMltQuery));

                for (final Object theSingleMltEntry : theMoreLikeThisQuery.list()) {
                    final Object[] theMltRow = (Object[]) theSingleMltEntry;
                    final Freelancer theMltFreelancer = (Freelancer) theMltRow[0];
                    final Document theMltDocument = (Document) theMltRow[1];
                    final Float theMltScore = (Float) theMltRow[2];

                    final ProfileSearchEntry theEntry = createResultEntry(theAnalyzer, theMltQuery, theHighlighter, theMltFreelancer, theMltDocument);
                    theResult.add(theEntry);
                }

            } catch (final Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (theIndexReader != null) {
                    theSearchFactory.getReaderProvider().closeReader(theIndexReader);
                }
            }

        }
        return theResult;
    }
}