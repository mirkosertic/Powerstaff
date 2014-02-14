/**
 * Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
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

    {
        BooleanQuery.setMaxClauseCount(8192);
    }

    private PowerstaffSystemParameterService systemParameterService;

    private ProfileSearchDAO profileSearchDAO;

    private SessionFactory sessionFactory;

    private FSCache fileSystemCache;

    public void setFileSystemCache(FSCache fileSystemCache) {
        this.fileSystemCache = fileSystemCache;
    }

    public void setSystemParameterService(
            PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    public void setProfileSearchDAO(ProfileSearchDAO profileSearchDAO) {
        this.profileSearchDAO = profileSearchDAO;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Query getRealQuery(SavedProfileSearch aRequest, Analyzer aAnalyzer)
            throws IOException, ParseException {

        BooleanQuery theQuery = new BooleanQuery();

        GoogleStyleQueryParser theParser = new GoogleStyleQueryParser(null);

        theQuery.add(theParser.parseQuery(aRequest.getProfileContent(),
                aAnalyzer, ProfileIndexerService.CONTENT), Occur.MUST);

        if (!StringUtils.isEmpty(aRequest.getPlz())) {
            theQuery.add(new WildcardQuery(new Term(ProfileIndexerService.PLZ,
                    aRequest.getPlz().replace("%", "*"))), Occur.MUST);
        }

        return theQuery;
    }

    protected String getHighlightedSearchResult(Analyzer aAnalyzer,
                                                Highlighter aHighlighter, String aContent, Query aQuery)
            throws IOException, InvalidTokenOffsetsException {

        CachingTokenFilter tokenStream = new CachingTokenFilter(aAnalyzer
                .tokenStream(ProfileIndexerService.CONTENT, new StringReader(
                        aContent)));

        return aHighlighter.getBestFragments(tokenStream, aContent, 5,
                "&nbsp;...&nbsp;");
    }

    public SavedProfileSearch getSearchRequestForUser(String aUserId) {
        return profileSearchDAO.getSavedSearchForUser(aUserId);
    }

    public SavedProfileSearch getSearchRequest(long aSearchRequestId) {
        return profileSearchDAO.getSavedSearchById(aSearchRequestId);
    }

    @Override
    public void saveSearchRequest(SavedProfileSearch searchRequest, boolean cleanup) throws OptimisticLockException {

        User theUser = (User) UserContextHolder.getUserContext().getAuthenticatable();

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

            if (isNew) {
                // Auf keinen Fall die bereits existierende Id vom SavedSearchRequest pro projekt übernehmen!
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
            SavedProfileSearch aRequest, int startRow, int pageSize)
            throws Exception {

        if (aRequest.getId() == null) {
            // Kann passieren, wenn die Suche das erste mal aufgerufen wird
            return new DataPage<ProfileSearchEntry>(0, 0, new ArrayList<ProfileSearchEntry>());
        }

        Analyzer theAnalyzer = ProfileAnalyzerFactory.createAnalyzer();

        FullTextSession theSession = Search.getFullTextSession(sessionFactory.getCurrentSession());

        Query theQuery = getRealQuery(aRequest, theAnalyzer);

        LOGGER.info("Search query is " + theQuery + " from " + startRow
                + " with pagesize " + pageSize);

        Highlighter theHighlighter = new Highlighter(new SpanGradientFormatter(
                1, "#000000", "#0000FF", null, null), new QueryScorer(theQuery));

        BooleanQuery theRealQuery = new BooleanQuery();
        theRealQuery.add(theQuery, Occur.MUST);

        if (aRequest != null) {
            for (String theId : aRequest.getProfilesToIgnore()) {
                theRealQuery.add(new TermQuery(new Term(
                        ProfileIndexerService.UNIQUE_ID, theId)),
                        Occur.MUST_NOT);
            }
        }

        LOGGER.info("Query with ignore is " + theRealQuery);

        Sort theSort = null;
        if (!StringUtils.isEmpty(aRequest.getSortierung())) {
            int theSortType = SortField.STRING;
            if (ProfileIndexerService.STUNDENSATZ.equals(aRequest.getSortierung())) {
                theSortType = SortField.LONG;
            }
            theSort = new Sort(new SortField(aRequest.getSortierung(),
                    theSortType));
        }

        Filter theFilter = null;

        if (aRequest.getStundensatzVon() != null
                || aRequest.getStundensatzBis() != null) {
            List<Filter> theFilterList = new ArrayList<Filter>();
            if (aRequest.getStundensatzVon() != null) {
                theFilterList.add(NumericRangeFilter.newLongRange(
                        ProfileIndexerService.STUNDENSATZ, aRequest
                        .getStundensatzVon(), Long.MAX_VALUE, true,
                        true));
            }
            if (aRequest.getStundensatzBis() != null) {
                theFilterList.add(NumericRangeFilter.newLongRange(
                        ProfileIndexerService.STUNDENSATZ, 0l, aRequest
                        .getStundensatzBis(), true, true));
            }
            theFilter = new ChainedFilter(theFilterList
                    .toArray(new Filter[]{}), ChainedFilter.AND);
        }

        int theStart = startRow;
        int theEnd = startRow + pageSize;

        FullTextQuery theHibernateQuery = theSession.createFullTextQuery(theRealQuery, Freelancer.class);
        if (theFilter != null) {
            theHibernateQuery.setFilter(theFilter);
        }
        if (theSort != null) {
            theHibernateQuery.setSort(theSort);
        }
        theHibernateQuery.setFirstResult(theStart);
        theHibernateQuery.setMaxResults(theEnd - theStart);
        theHibernateQuery.setProjection(FullTextQuery.THIS, FullTextQuery.DOCUMENT);

        List<ProfileSearchEntry> theResult = new ArrayList<ProfileSearchEntry>();

        for (Object theSingleEntity : theHibernateQuery.list()) {
            Object[] theRow = (Object[]) theSingleEntity;
            Freelancer theFreelancer = (Freelancer) theRow[0];
            Document theDocument = (Document) theRow[1];
            ProfileSearchEntry theEntry = createResultEntry(theAnalyzer, theQuery, theHighlighter, theFreelancer, theDocument);

            theResult.add(theEntry);
        }

        return new DataPage<ProfileSearchEntry>(theHibernateQuery.getResultSize(), theStart,
                theResult);
    }

    private ProfileSearchEntry createResultEntry(Analyzer aAnalyzer, Query aQuery, Highlighter aHighlighter, Freelancer aFreelancer, Document aDocument) throws IOException, InvalidTokenOffsetsException {
        ProfileSearchEntry theEntry = new ProfileSearchEntry();
        theEntry.setCode(aFreelancer.getCode());
        theEntry.setDocumentId("" + aFreelancer.getId());

        ProfileSearchInfoDetail theDetail = new ProfileSearchInfoDetail();
        theDetail.setId(aFreelancer.getId());
        theDetail.setName1(aFreelancer.getName1());
        theDetail.setName2(aFreelancer.getName2());
        theDetail.setAvailability(aFreelancer
                .getAvailabilityAsDate());
        theDetail.setPlz(aFreelancer.getPlz());
        theDetail.setStundensatz(aFreelancer.getSallaryLong());
        theDetail.setContactforbidden(aFreelancer
                .isContactforbidden());
        theDetail.setContacts(new ArrayList<FreelancerContact>(
                aFreelancer.getContacts()));

        String theContent = aDocument.get(ProfileIndexerService.ORIG_CONTENT);
        theEntry.setHighlightResult(getHighlightedSearchResult(aAnalyzer,
                aHighlighter, theContent, aQuery));

        theEntry.setFreelancer(theDetail);
        return theEntry;
    }

    @Override
    public void removeSavedSearchEntry(SavedProfileSearch searchRequest, String aDocumentId) throws OptimisticLockException {

        SavedProfileSearch theSearch = profileSearchDAO.getSavedSearchById(searchRequest.getId());

        theSearch.getProfilesToIgnore().add(aDocumentId);

        profileSearchDAO.save(theSearch);
    }

    @Override
    public int getPageSize() {
        return systemParameterService.getMaxSearchResult();
    }


    @Override
    public synchronized List<FreelancerProfile> loadProfilesFor(Freelancer aFreelancer) {
        List<FreelancerProfile> theProfiles = new ArrayList<FreelancerProfile>();

        if (aFreelancer != null && aFreelancer.getCode() != null) {

            String theCode = aFreelancer.getCode().trim().toLowerCase();
            Set<File> theFilesForCode = fileSystemCache.getFilesForCode(theCode);

            if (theFilesForCode != null) {

                String theClientBaseDir = systemParameterService.getIndexerNetworkDir();
                if (!theClientBaseDir.endsWith("\\")) {
                    theClientBaseDir += "\\";
                }
                String theServerBaseDir = systemParameterService.getIndexerSourcePath();
                if (!theServerBaseDir.endsWith("\\")) {
                    theServerBaseDir += "\\";
                }

                SimpleDateFormat theFormat = new SimpleDateFormat("dd.MM.yyyy");

                for (File theFile : theFilesForCode) {
                    FreelancerProfile theProfile = new FreelancerProfile();
                    theProfile.setName(theFile.getName());
                    theProfile.setInfotext("Aktualisiert : " + theFormat.format(new Date(theFile.lastModified())));
                    theProfile.setFileOnserver(theFile);

                    String theStrippedPath = theFile.toString().substring(
                            theServerBaseDir.length());
                    theProfile.setFileName(theClientBaseDir + theStrippedPath);

                    theProfiles.add(theProfile);
                }
            }

        }
        return theProfiles;
    }

    @Override
    public List<ProfileSearchEntry> getSimilarFreelancer(Freelancer aFreelancer) {
        List<ProfileSearchEntry> theResult = new ArrayList<ProfileSearchEntry>();
        if (aFreelancer != null && aFreelancer.getId() != null) {

            FullTextSession theSession = Search.getFullTextSession(sessionFactory.getCurrentSession());
            SearchFactory theSearchFactory = theSession.getSearchFactory();

            Analyzer theAnalyzer = ProfileAnalyzerFactory.createAnalyzer();

            DirectoryProvider theFreeelancerProvider = theSearchFactory.getDirectoryProviders(Freelancer.class)[0];

            IndexReader theIndexReader = null;

            try {
                theIndexReader = theSearchFactory.getReaderProvider().openReader(theFreeelancerProvider);

                MoreLikeThis theMoreLikeThis = new MoreLikeThis(theIndexReader);

                // Zuerst den Freiberufler raussuchen
                Query theQuery = new TermQuery(new Term(ProfileIndexerService.UNIQUE_ID, aFreelancer.getId().toString()));
                FullTextQuery theHibernateQuery = theSession.createFullTextQuery(theQuery, Freelancer.class);
                theHibernateQuery.setProjection(FullTextQuery.THIS, FullTextQuery.DOCUMENT);

                for (Object theSingleEntity : theHibernateQuery.list()) {
                    Object[] theRow = (Object[]) theSingleEntity;
                    Freelancer theFreelancer = (Freelancer) theRow[0];
                    Document theDocument = (Document) theRow[1];

                    theMoreLikeThis.setMinDocFreq(1);
                    theMoreLikeThis.setMinTermFreq(1);
                    theMoreLikeThis.setAnalyzer(theAnalyzer);
                    theMoreLikeThis.setFieldNames(new String[]{ProfileIndexerService.CONTENT});
                    Query theMltQuery = theMoreLikeThis.like(new StringReader(theDocument.get(ProfileIndexerService.ORIG_CONTENT)));

                    FullTextQuery theMoreLikeThisQuery = theSession.createFullTextQuery(theMltQuery, Freelancer.class);
                    theMoreLikeThisQuery.setProjection(FullTextQuery.THIS, FullTextQuery.DOCUMENT, FullTextQuery.SCORE);
                    theMoreLikeThisQuery.setMaxResults(50);

                    Highlighter theHighlighter = new Highlighter(new SpanGradientFormatter(
                            1, "#000000", "#0000FF", null, null), new QueryScorer(theMltQuery));

                    for (Object theSingleMltEntry : theMoreLikeThisQuery.list()) {
                        Object[] theMltRow = (Object[]) theSingleMltEntry;
                        Freelancer theMltFreelancer = (Freelancer) theMltRow[0];
                        Document theMltDocument = (Document) theMltRow[1];
                        Float theMltScore = (Float) theMltRow[2];

                        if (theMltFreelancer != theFreelancer) {
                            // Einen gefunden
                            ProfileSearchEntry theEntry = createResultEntry(theAnalyzer, theMltQuery, theHighlighter, theMltFreelancer, theMltDocument);
                            theResult.add(theEntry);
                        }
                    }

                }
            } catch (Exception e) {
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
    public List<ProfileSearchEntry> getSimilarFreelancer(Project aProject) {
        List<ProfileSearchEntry> theResult = new ArrayList<ProfileSearchEntry>();
        if (aProject != null && aProject.getId() != null) {

            FullTextSession theSession = Search.getFullTextSession(sessionFactory.getCurrentSession());
            SearchFactory theSearchFactory = theSession.getSearchFactory();

            Analyzer theAnalyzer = ProfileAnalyzerFactory.createAnalyzer();

            DirectoryProvider theFreeelancerProvider = theSearchFactory.getDirectoryProviders(Freelancer.class)[0];

            IndexReader theIndexReader = null;

            try {
                theIndexReader = theSearchFactory.getReaderProvider().openReader(theFreeelancerProvider);

                MoreLikeThis theMoreLikeThis = new MoreLikeThis(theIndexReader);

                theMoreLikeThis.setMinDocFreq(1);
                theMoreLikeThis.setMinTermFreq(1);
                theMoreLikeThis.setAnalyzer(theAnalyzer);
                theMoreLikeThis.setFieldNames(new String[]{ProfileIndexerService.CONTENT});
                Query theMltQuery = theMoreLikeThis.like(new StringReader(aProject.getDescriptionShort() + " " + aProject.getDescriptionLong() + " " + aProject.getSkills()));

                FullTextQuery theMoreLikeThisQuery = theSession.createFullTextQuery(theMltQuery, Freelancer.class);
                theMoreLikeThisQuery.setProjection(FullTextQuery.THIS, FullTextQuery.DOCUMENT, FullTextQuery.SCORE);
                theMoreLikeThisQuery.setMaxResults(50);

                Highlighter theHighlighter = new Highlighter(new SpanGradientFormatter(
                        1, "#000000", "#0000FF", null, null), new QueryScorer(theMltQuery));

                for (Object theSingleMltEntry : theMoreLikeThisQuery.list()) {
                    Object[] theMltRow = (Object[]) theSingleMltEntry;
                    Freelancer theMltFreelancer = (Freelancer) theMltRow[0];
                    Document theMltDocument = (Document) theMltRow[1];
                    Float theMltScore = (Float) theMltRow[2];

                    ProfileSearchEntry theEntry = createResultEntry(theAnalyzer, theMltQuery, theHighlighter, theMltFreelancer, theMltDocument);
                    theResult.add(theEntry);
                }

            } catch (Exception e) {
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