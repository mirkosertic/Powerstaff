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

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;

import de.powerstaff.business.entity.*;
import de.powerstaff.business.lucene.analysis.ProfileAnalyzer;
import org.apache.commons.lang.StringUtils;
import org.apache.jasper.tagplugins.jstl.core.Catch;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CachingTokenFilter;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.misc.ChainedFilter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeFilter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SpanGradientFormatter;
import org.apache.lucene.search.similar.MoreLikeThis;
import org.apache.lucene.store.FSDirectory;

import de.mogwai.common.business.service.impl.LogableService;
import de.mogwai.common.usercontext.UserContextHolder;
import de.powerstaff.business.dao.ProfileSearchDAO;
import de.powerstaff.business.dto.DataPage;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.dto.ProfileSearchInfoDetail;
import de.powerstaff.business.dto.ProfileSearchRequest;
import de.powerstaff.business.dto.SearchRequestSupport;
import de.powerstaff.business.lucene.analysis.ProfileAnalyzerFactory;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.LuceneService;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.ProfileIndexerService;
import de.powerstaff.business.service.ProfileSearchService;

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

    private LuceneService luceneService;

    private ProfileSearchDAO profileSearchDAO;

    public PowerstaffSystemParameterService getSystemParameterService() {
        return systemParameterService;
    }

    public void setSystemParameterService(
            PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    public FreelancerService getFreelancerService() {
        return freelancerService;
    }

    public void setFreelancerService(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    public ProfileSearchDAO getProfileSearchDAO() {
        return profileSearchDAO;
    }

    public void setProfileSearchDAO(ProfileSearchDAO profileSearchDAO) {
        this.profileSearchDAO = profileSearchDAO;
    }

    public LuceneService getLuceneService() {
        return luceneService;
    }

    public void setLuceneService(LuceneService luceneService) {
        this.luceneService = luceneService;
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

        GoogleStyleQueryParser theParser = new GoogleStyleQueryParser(
                luceneService.getIndexReader());

        theQuery.add(theParser.parseQuery(aRequest.getProfileContent(),
                aAnalyzer, ProfileIndexerService.CONTENT), Occur.MUST);

        if (!StringUtils.isEmpty(aRequest.getPlz())) {
            theQuery.add(new WildcardQuery(new Term(ProfileIndexerService.PLZ,
                    aRequest.getPlz().replace("%", "*"))), Occur.MUST);
        }

        return theQuery;
    }

    public Vector<FreelancerProfile> findProfiles(String aCode)
            throws Exception {

        Vector<FreelancerProfile> theResult = new Vector<FreelancerProfile>();

        if (aCode != null) {
            aCode = aCode.trim();
        }

        if (!StringUtils.isEmpty(aCode)) {

            String theRealQuery = "\"" + aCode + "\"";

            logger.logInfo("Search query is " + theRealQuery);

            IndexSearcher theSearcher = luceneService.getIndexSearcher();

            Analyzer theAnalyzer = new KeywordAnalyzer();
            QueryParser theParser = LuceneUtils.createQueryParser(
                    ProfileIndexerService.CODE, theAnalyzer);
            Query theQuery = theParser.parse(theRealQuery.toString());

            TopDocs theDocs = theSearcher.search(theQuery, 10);

            logger.logInfo("Size of search result is " + theDocs.totalHits);

            SimpleDateFormat theFormat = new SimpleDateFormat("dd.MM.yyyy");

            for (ScoreDoc theDoc : theDocs.scoreDocs) {

                Document theDocument = theSearcher.doc(theDoc.doc);

                FreelancerProfile theSearchResult = new FreelancerProfile();

                theSearchResult.setFileNameOnServer(theDocument
                        .get(ProfileIndexerService.PATH));

                String theFileName = theDocument
                        .get(ProfileIndexerService.PATH);

                Long theModifiedDate = Long.parseLong(theDocument
                        .get(ProfileIndexerService.MODIFIED));
                Date theDate = new Date();
                theDate.setTime(theModifiedDate.longValue());

                theSearchResult.setInfotext("Aktualisiert : "
                        + theFormat.format(theDate));

                int p = theFileName.lastIndexOf(File.separator);
                if (p >= 0) {
                    theFileName = theFileName.substring(p + 1);
                }

                String theBaseDir = systemParameterService
                        .getIndexerNetworkDir();
                if (!theBaseDir.endsWith("\\")) {
                    theBaseDir += "\\";
                }

                theSearchResult.setName(theFileName);
                theSearchResult.setFileName(theBaseDir
                        + theDocument.get(ProfileIndexerService.STRIPPEDPATH));

                theResult.add(theSearchResult);

                // Ähnliche Dokumente finden
                ProfileAnalyzer theProfileAnalyzer = ProfileAnalyzerFactory.createAnalyzer();

                MoreLikeThis theMoreLikeThis = new MoreLikeThis(luceneService.getIndexReader());
                theMoreLikeThis.setAnalyzer(theProfileAnalyzer);
                theMoreLikeThis.setStopWords(theProfileAnalyzer.getStopSet());
                theMoreLikeThis.setFieldNames(new String[]{ProfileIndexerService.CONTENT});

                Query theLikeThis = theMoreLikeThis.like(new StringReader(theDocument.get(ProfileIndexerService.ORIG_CONTENT)));
                TopDocs theLikeThisDocs = theSearcher.search(theLikeThis, 50);
                for (ScoreDoc theLikeThisDoc : theLikeThisDocs.scoreDocs) {
                    if (theLikeThisDoc.doc != theDoc.doc) {
                        // Wir haben was gefunden
                        Document theSimilarDocument = theSearcher.doc(theLikeThisDoc.doc);
                        System.out.println("Ähnliches Dokument : " + theSimilarDocument.get(ProfileIndexerService.CODE) + " " + theLikeThisDoc.score);
                    }
                }

            }
        }

        return theResult;
    }

    protected String getHighlightedSearchResult(Analyzer aAnalyzer,
                                                Highlighter aHighlighter, Document aDocument, Query aQuery)
            throws IOException, InvalidTokenOffsetsException {

        String theContent = aDocument.get(ProfileIndexerService.ORIG_CONTENT);

        CachingTokenFilter tokenStream = new CachingTokenFilter(aAnalyzer
                .tokenStream(ProfileIndexerService.CONTENT, new StringReader(
                        theContent)));

        return aHighlighter.getBestFragments(tokenStream, theContent, 5,
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

    @Override
    public DataPage<ProfileSearchEntry> findProfileDataPage(
            ProfileSearchRequest aRequest, int startRow, int pageSize)
            throws Exception {

        User theUser = (User) UserContextHolder.getUserContext()
                .getAuthenticatable();

        SavedProfileSearch theSearch = profileSearchDAO
                .getSavedSearchFor(theUser);

        Analyzer theAnalyzer = ProfileAnalyzerFactory.createAnalyzer();

        Query theQuery = getRealQuery(aRequest, theAnalyzer);

        logger.logInfo("Search query is " + theQuery + " from " + startRow
                + " with pagesize " + pageSize);

        long theStartTime = System.currentTimeMillis();

        Searcher theSearcher = luceneService.getIndexSearcher();

        theQuery = theSearcher.rewrite(theQuery);

        logger.logInfo("Rewritten Search query is " + theQuery);

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

        TopDocs theHits;
        if (theSort == null) {
            theHits = theSearcher.search(theRealQuery, theFilter, theEnd);
        } else {
            theHits = theSearcher.search(theRealQuery, theFilter, theEnd,
                    theSort);
        }

        long theDuration = System.currentTimeMillis() - theStartTime;

        if (theEnd > theHits.totalHits) {
            theEnd = theHits.totalHits;
        }

        logger.logInfo("Size of search result is " + theHits.totalHits
                + " duration = " + theDuration);

        List<ProfileSearchEntry> theResult = new ArrayList<ProfileSearchEntry>();

        for (int i = theStart; i < theEnd; i++) {

            Document theDocument = theSearcher.doc(theHits.scoreDocs[i].doc);

            ProfileSearchEntry theEntry = new ProfileSearchEntry();
            theEntry.setCode(theDocument.get(ProfileIndexerService.CODE));
            theEntry.setHighlightResult(getHighlightedSearchResult(theAnalyzer,
                    theHighlighter, theDocument, theQuery));
            theEntry.setDocumentId(theDocument
                    .get(ProfileIndexerService.UNIQUE_ID));

            ProfileSearchInfoDetail theDetail = new ProfileSearchInfoDetail();

            String theFreelancerId = theDocument
                    .get(ProfileIndexerService.FREELANCERID);
            if (!StringUtils.isEmpty(theFreelancerId)) {
                Freelancer theFreelancer = freelancerService
                        .findByPrimaryKey(Long.parseLong(theFreelancerId));
                if (theFreelancer != null) {
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
                }
            }

            theEntry.setFreelancer(theDetail);

            theResult.add(theEntry);
        }

        return new DataPage<ProfileSearchEntry>(theHits.totalHits, theStart,
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
    public void findMatchingFreelancer(Project aProject) {
        try {
            IndexSearcher theSearcher = luceneService.getIndexSearcher();

            ProfileAnalyzer theAnalyzer = ProfileAnalyzerFactory.createAnalyzer();

            // Ähnliche Dokumente finden
            MoreLikeThis theMoreLikeThis = new MoreLikeThis(luceneService.getIndexReader());
            theMoreLikeThis.setAnalyzer(theAnalyzer);
            theMoreLikeThis.setStopWords(theAnalyzer.getStopSet());
            theMoreLikeThis.setFieldNames(new String[]{ProfileIndexerService.CONTENT});

            Query theLikeThis = theMoreLikeThis.like(new StringReader(aProject.getDescriptionShort() + " " + aProject.getDescriptionLong() + " " + aProject.getSkills()));
            TopDocs theLikeThisDocs = theSearcher.search(theLikeThis, 50);
            for (ScoreDoc theLikeThisDoc : theLikeThisDocs.scoreDocs) {
                // Wir haben was gefunden
                Document theSimilarDocument = theSearcher.doc(theLikeThisDoc.doc);
                String theId = theSimilarDocument.get(ProfileIndexerService.CODE);
                if (theId != null) {
                    Freelancer theFreelancer = freelancerService.findRealFreelancerByCode(theId);
                    if (theFreelancer != null) {
                        System.out.println("Ähnlicher Freiberufler : " + theSimilarDocument.get(ProfileIndexerService.CODE) + " " + theLikeThisDoc.score);
                    } else {
                        System.out.println("Kein Freiberufler gefunden für " + theId);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}