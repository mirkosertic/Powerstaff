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

import de.mogwai.common.business.service.impl.LogableService;
import de.mogwai.common.usercontext.UserContextHolder;
import de.powerstaff.business.dao.ProfileSearchDAO;
import de.powerstaff.business.dto.*;
import de.powerstaff.business.entity.*;
import de.powerstaff.business.lucene.analysis.ProfileAnalyzerFactory;
import de.powerstaff.business.service.FSCache;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.ProfileIndexerService;
import de.powerstaff.business.service.ProfileSearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SpanGradientFormatter;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

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
public class ProfileSearchServiceImpl extends LogableService implements
        ProfileSearchService {
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

    private void copySearchRequestInto(SearchRequestSupport aSource,
                                       SearchRequestSupport aDestination) {
        aDestination.setProfileContent(aSource.getProfileContent());
        aDestination.setPlz(aSource.getPlz());
        aDestination.setStundensatzVon(aSource.getStundensatzVon());
        aDestination.setStundensatzBis(aSource.getStundensatzBis());
        aDestination.setSortierung(aSource.getSortierung());
        aDestination.setProject(aSource.getProject());
        aDestination.setId(aSource.getId());
    }

    private Query getRealQuery(ProfileSearchRequest aRequest, Analyzer aAnalyzer)
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

    public ProfileSearchRequest getSearchRequestForUser(String aUserId) {

        SavedProfileSearch theSearch = profileSearchDAO.getSavedSearchForUser(aUserId);

        ProfileSearchRequest theRequest = new ProfileSearchRequest();
        if (theSearch != null) {
            copySearchRequestInto(theSearch, theRequest);
        } else {
            // Neuen Eintrag erzeugen
            User theUser = (User) UserContextHolder.getUserContext().getAuthenticatable();
            theSearch.setUser(theUser);
        }

        return theRequest;
    }

    public ProfileSearchRequest getSearchRequest(long aSearchRequestId) {
        SavedProfileSearch theSearch = profileSearchDAO.getSavedSearchById(aSearchRequestId);

        ProfileSearchRequest theRequest = new ProfileSearchRequest();
        if (theSearch != null) {
            copySearchRequestInto(theSearch, theRequest);
        }

        return theRequest;
    }

    @Override
    public void saveSearchRequest(ProfileSearchRequest searchRequest, boolean cleanup) {

        User theUser = (User) UserContextHolder.getUserContext().getAuthenticatable();

        SavedProfileSearch theSearch = null;

        if (searchRequest.getId() != null) {
            theSearch = profileSearchDAO.getSavedSearchById(searchRequest.getId());
        }

        if (theSearch == null) {
            theSearch = new SavedProfileSearch();
            theSearch.setUser(theUser);
        }

        copySearchRequestInto(searchRequest, theSearch);

        if (cleanup) {
            theSearch.getProfilesToIgnore().clear();
        }

        profileSearchDAO.save(theSearch);

        // If the search was for a project, it will also be saved as the last search for a user
        if (theSearch.getProject() != null) {

            boolean isNew = false;
            SavedProfileSearch theSearchForUser = profileSearchDAO.getSavedSearchForUser(theUser.getUsername());
            if (theSearchForUser == null) {
                theSearchForUser = new SavedProfileSearch();
                theSearchForUser.setUser(theUser);
                isNew = true;
            } else {
                if (cleanup) {
                    theSearch.getProfilesToIgnore().clear();
                }
            }

            copySearchRequestInto(searchRequest, theSearchForUser);
            if (isNew) {
                // Auf keinen Fall die bereits existierende Id vom SavedSearchRequest pro projekt übernehmen!
                theSearchForUser.setId(null);
            }

            // Keine Zuordnung zu einem Projekt!
            theSearchForUser.setProject(null);

            profileSearchDAO.save(theSearchForUser);
        }
    }

    @Override
    public DataPage<ProfileSearchEntry> findProfileDataPage(
            ProfileSearchRequest aRequest, int startRow, int pageSize)
            throws Exception {

        if (aRequest.getId() == null) {
            // Kann passieren, wenn die Suche das erste mal aufgerufen wird
            return new DataPage<ProfileSearchEntry>(0, 0, new ArrayList<ProfileSearchEntry>());
        }

        SavedProfileSearch theSearch = profileSearchDAO.getSavedSearchById(aRequest.getId());

        Analyzer theAnalyzer = ProfileAnalyzerFactory.createAnalyzer();

        FullTextSession theSession = Search.getFullTextSession(sessionFactory.getCurrentSession());

        Query theQuery = getRealQuery(aRequest, theAnalyzer);

        logger.logInfo("Search query is " + theQuery + " from " + startRow
                + " with pagesize " + pageSize);

        Highlighter theHighlighter = new Highlighter(new SpanGradientFormatter(
                1, "#000000", "#0000FF", null, null), new QueryScorer(theQuery));

        BooleanQuery theRealQuery = new BooleanQuery();
        theRealQuery.add(theQuery, Occur.MUST);

        if (theSearch != null) {
            for (String theId : theSearch.getProfilesToIgnore()) {
                theRealQuery.add(new TermQuery(new Term(
                        ProfileIndexerService.UNIQUE_ID, theId)),
                        Occur.MUST_NOT);
            }
        }

        logger.logInfo("Query with ignore is " + theRealQuery);

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
            ProfileSearchEntry theEntry = new ProfileSearchEntry();
            theEntry.setCode(theFreelancer.getCode());
            theEntry.setDocumentId("" + theFreelancer.getId());

            ProfileSearchInfoDetail theDetail = new ProfileSearchInfoDetail();
            theDetail.setId(theFreelancer.getId());
            theDetail.setName1(theFreelancer.getName1());
            theDetail.setName2(theFreelancer.getName2());
            theDetail.setAvailability(theFreelancer
                    .getAvailabilityAsDate());
            theDetail.setPlz(theFreelancer.getPlz());
            theDetail.setStundensatz(theFreelancer.getSallaryLong());
            theDetail.setContactforbidden(theFreelancer
                    .isContactforbidden());
            theDetail.setContacts(new ArrayList<FreelancerContact>(
                    theFreelancer.getContacts()));

            String theContent = theDocument.get(ProfileIndexerService.ORIG_CONTENT);
            theEntry.setHighlightResult(getHighlightedSearchResult(theAnalyzer,
                    theHighlighter, theContent, theQuery));

            theEntry.setFreelancer(theDetail);

            theResult.add(theEntry);
        }

        return new DataPage<ProfileSearchEntry>(theHibernateQuery.getResultSize(), theStart,
                theResult);
    }

    @Override
    public void removeSavedSearchEntry(ProfileSearchRequest searchRequest, String aDocumentId) {

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
}