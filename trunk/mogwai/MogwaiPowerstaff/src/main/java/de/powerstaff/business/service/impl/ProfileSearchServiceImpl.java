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
import de.powerstaff.business.service.*;
import de.powerstaff.business.service.impl.reader.DocumentReader;
import de.powerstaff.business.service.impl.reader.DocumentReaderFactory;
import de.powerstaff.business.service.impl.reader.ReadResult;
import java.io.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.*;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SpanGradientFormatter;
import org.hibernate.*;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

/**
 * @author Mirko Sertic
 */
public class ProfileSearchServiceImpl extends LogableService implements
        ProfileSearchService {
    {
        BooleanQuery.setMaxClauseCount(8192);
    }

    private FreelancerService freelancerService;

    private PowerstaffSystemParameterService systemParameterService;

    private ProfileSearchDAO profileSearchDAO;

    private SessionFactory sessionFactory;

    private DocumentReaderFactory documentReaderFactory;

    public void setDocumentReaderFactory(DocumentReaderFactory documentReaderFactory) {
        this.documentReaderFactory = documentReaderFactory;
    }

    public void setSystemParameterService(
            PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    public void setFreelancerService(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
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

    public ProfileSearchRequest getLastSearchRequest() throws Exception {

        User theUser = (User) UserContextHolder.getUserContext()
                .getAuthenticatable();

        SavedProfileSearch theSearch = profileSearchDAO
                .getSavedSearchFor(theUser);
        if (theSearch == null) {
            return null;
        }

        ProfileSearchRequest theRequest = new ProfileSearchRequest();
        copySearchRequestInto(theSearch, theRequest);

        return theRequest;
    }

    @Override
    public void saveSearchRequest(ProfileSearchRequest searchRequest) {
        User theUser = (User) UserContextHolder.getUserContext()
                .getAuthenticatable();

        SavedProfileSearch theSearch = profileSearchDAO
                .getSavedSearchFor(theUser);

        if (theSearch == null) {
            theSearch = new SavedProfileSearch();
            theSearch.setUser(theUser);
        }

        copySearchRequestInto(searchRequest, theSearch);

        theSearch.getProfilesToIgnore().clear();

        profileSearchDAO.save(theSearch);
    }

    private String getOriginalContentFor(Freelancer aFreelancer) {
        List<FreelancerProfile> theProfiles = loadProfilesFor(aFreelancer);

        documentReaderFactory.initialize();

        StringBuilder theContent = new StringBuilder();

        for (FreelancerProfile theProfile : theProfiles) {
            DocumentReader theReader = documentReaderFactory.getDocumentReaderForFile(theProfile.getFileOnserver());
            if (theReader != null) {
                try {
                    ReadResult theResult = theReader.getContent(theProfile.getFileOnserver());
                    String theResultString = theResult.getContent();

                    theContent.append(theResultString).append(" ");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return theContent.toString();
    }

    @Override
    public DataPage<ProfileSearchEntry> findProfileDataPage(
            ProfileSearchRequest aRequest, int startRow, int pageSize)
            throws Exception {

        User theUser = (User) UserContextHolder.getUserContext()
                .getAuthenticatable();

        SavedProfileSearch theSearch = profileSearchDAO
                .getSavedSearchFor(theUser);


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
            theSort = new Sort(new SortField(aRequest.getSortierung(),
                    SortField.STRING));
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

        FullTextQuery theHibernateQuery = theSession.createFullTextQuery(theQuery, Freelancer.class);
        if (theFilter != null) {
            theHibernateQuery.setFilter(theFilter);
        }
        if (theSort != null) {
            theHibernateQuery.setSort(theSort);
        }
        theHibernateQuery.setFirstResult(theStart);
        theHibernateQuery.setMaxResults(theEnd - theStart);

        List<ProfileSearchEntry> theResult = new ArrayList<ProfileSearchEntry>();

        for (Object theSingleEntity : theHibernateQuery.list()) {
            Freelancer theFreelancer = (Freelancer) theSingleEntity;
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

            String theContent = getOriginalContentFor(theFreelancer);
            theEntry.setHighlightResult(getHighlightedSearchResult(theAnalyzer,
                    theHighlighter, theContent, theQuery));

            theEntry.setFreelancer(theDetail);

            theResult.add(theEntry);
        }

        return new DataPage<ProfileSearchEntry>(theHibernateQuery.getResultSize(), theStart,
                theResult);
    }

    @Override
    public void removeSavedSearchEntry(String aDocumentId) {

        User theUser = (User) UserContextHolder.getUserContext()
                .getAuthenticatable();

        SavedProfileSearch theSearch = profileSearchDAO
                .getSavedSearchFor(theUser);

        theSearch.getProfilesToIgnore().add(aDocumentId);

        profileSearchDAO.save(theSearch);
    }

    @Override
    public int getPageSize() {
        return systemParameterService.getMaxSearchResult();
    }

    @Override
    public List<FreelancerProfile> loadProfilesFor(Freelancer aFreelancer) {
        List<FreelancerProfile> theProfiles = new ArrayList<FreelancerProfile>();
        if (aFreelancer != null && aFreelancer.getCode() != null) {
            String theCode = aFreelancer.getCode().trim();

            File theSourcepath = new File(systemParameterService.getIndexerSourcePath());
            fillProfiles(theProfiles, theCode, theSourcepath);
        }
        return theProfiles;
    }

    private void fillProfiles(List<FreelancerProfile> aList, String aCode, File aFile) {

        final String thePrefix = "profil " + aCode.toLowerCase()+".";
        String theClientBaseDir = systemParameterService.getIndexerNetworkDir();
        if (!theClientBaseDir.endsWith("\\")) {
            theClientBaseDir += "\\";
        }
        String theServerBaseDir = systemParameterService.getIndexerSourcePath();
        if (!theServerBaseDir.endsWith("\\")) {
            theServerBaseDir += "\\";
        }


        FileFilter theFilter = new FileFilter() {
            @Override
            public boolean accept(File aFile) {
                if (aFile.isDirectory()) {
                    return true;
                }
                return aFile.getName().toLowerCase().startsWith(thePrefix);
            }
        };

        SimpleDateFormat theFormat = new SimpleDateFormat("dd.MM.yyyy");

        for (File theFile : aFile.listFiles(theFilter)) {
            if (theFile.isDirectory()) {
                fillProfiles(aList, aCode, theFile);
            } else {
                FreelancerProfile theProfile = new FreelancerProfile();
                theProfile.setName(theFile.getName());
                theProfile.setInfotext("Aktualisiert : " + theFormat.format(new Date(theFile.lastModified())));
                theProfile.setFileOnserver(theFile);

                String theStrippedPath = theFile.toString().substring(
                        theServerBaseDir.length());
                theProfile.setFileName(theClientBaseDir + theStrippedPath);

                aList.add(theProfile);
            }
        }
    }
}