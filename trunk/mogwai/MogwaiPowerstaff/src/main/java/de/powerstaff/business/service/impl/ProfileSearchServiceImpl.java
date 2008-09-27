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
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.analyzing.AnalyzingQueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SpanGradientFormatter;

import de.mogwai.common.business.service.impl.LogableService;
import de.mogwai.common.usercontext.UserContextHolder;
import de.powerstaff.business.dao.ProfileSearchDAO;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.dto.ProfileSearchInfoDetail;
import de.powerstaff.business.dto.ProfileSearchRequest;
import de.powerstaff.business.dto.ProfileSearchResult;
import de.powerstaff.business.dto.SearchRequestSupport;
import de.powerstaff.business.entity.FreelancerProfile;
import de.powerstaff.business.entity.SavedProfileSearch;
import de.powerstaff.business.entity.SavedProfileSearchEntry;
import de.powerstaff.business.entity.User;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.ProfileIndexerService;
import de.powerstaff.business.service.ProfileSearchService;

/**
 * @author Mirko Sertic
 */
public class ProfileSearchServiceImpl extends LogableService implements ProfileSearchService {

    private FreelancerService freelancerService;

    private PowerstaffSystemParameterService systemParameterService;

    private ProfileSearchDAO profileSearchDAO;

    /**
     * @return the systemParameterService
     */
    public PowerstaffSystemParameterService getSystemParameterService() {
        return systemParameterService;
    }

    /**
     * @param systemParameterService
     *                the systemParameterService to set
     */
    public void setSystemParameterService(PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    public FreelancerService getFreelancerService() {
        return freelancerService;
    }

    public void setFreelancerService(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    /**
     * @return the profileSearchDAO
     */
    public ProfileSearchDAO getProfileSearchDAO() {
        return profileSearchDAO;
    }

    /**
     * @param profileSearchDAO
     *                the profileSearchDAO to set
     */
    public void setProfileSearchDAO(ProfileSearchDAO profileSearchDAO) {
        this.profileSearchDAO = profileSearchDAO;
    }

    private void copySearchRequestInto(SearchRequestSupport aSource, SearchRequestSupport aDestination) {
        aDestination.setProfileContent(aSource.getProfileContent());
        aDestination.setPlz(aSource.getPlz());
        aDestination.setStundensatzVon(aSource.getStundensatzVon());
        aDestination.setStundensatzBis(aSource.getStundensatzBis());
    }

    public ProfileSearchResult searchDocument(ProfileSearchRequest aRequest) throws Exception {

        int theMaxSearchResult = systemParameterService.getProfileMaxSearchResult();

        User theUser = (User) UserContextHolder.getUserContext().getAuthenticatable();

        profileSearchDAO.deleteSavedSearchesFor(theUser);

        SavedProfileSearch theSearch = new SavedProfileSearch();
        theSearch.setUser(theUser);

        copySearchRequestInto(aRequest, theSearch);

        ProfileSearchResult theResult = new ProfileSearchResult();
        theResult.setSearchRequest(aRequest);

        StringBuilder theRealQuery = getRealQuery(aRequest);

        logger.logDebug("Search query is " + theRealQuery);

        long theStartTime = System.currentTimeMillis();

        Searcher theSearcher = new IndexSearcher(systemParameterService.getIndexerPath());
        Analyzer theAnalyzer = new StandardAnalyzer();
        AnalyzingQueryParser theParser = new AnalyzingQueryParser(ProfileIndexerService.CONTENT, theAnalyzer);
        theParser.setAllowLeadingWildcard(true);
        Query theQuery = theParser.parse(theRealQuery.toString());
        Highlighter theHighlighter = new Highlighter(new SpanGradientFormatter(1, "#000000", "#0000FF", null, null),
                new QueryScorer(theQuery));

        Hits theHits = theSearcher.search(theQuery);

        long theDuration = System.currentTimeMillis() - theStartTime;

        logger.logDebug("Size of search result is " + theHits.length() + " duration = " + theDuration);

        theResult.setTotalFound(theHits.length());
        boolean isExtendedSearch = aRequest.isExtendedSearch();

        for (int i = 0; i < theHits.length(); i++) {

            Document theDocument = theHits.doc(i);

            ProfileSearchEntry theEntry = new ProfileSearchEntry();
            theEntry.setCode(theDocument.get(ProfileIndexerService.CODE));

            ProfileSearchInfoDetail theFreelancer;
            if (isExtendedSearch) {
                theFreelancer = freelancerService.findFreelancerByCodeExtended(theEntry.getCode(), aRequest);
            } else {
                theFreelancer = freelancerService.findFreelancerByCode(theEntry.getCode());
            }

            if (theFreelancer != null) {
                theEntry.setFreelancer(theFreelancer);
            }

            theEntry.setHighlightResult(getHighlightedSearchResult(theAnalyzer, theHighlighter, theDocument));

            if (isExtendedSearch) {
                if (theFreelancer != null) {
                    theResult.getEnties().add(theEntry);

                    SavedProfileSearchEntry theSavedEntry = new SavedProfileSearchEntry();
                    theEntry.setSavedSearchEntry(theSavedEntry);
                    if (theFreelancer != null) {
                        theSavedEntry.setFreelancerId(theFreelancer.getId());
                    }
                    theSavedEntry.setUniqueDocumentId(theDocument.get(ProfileIndexerService.UNIQUE_ID));
                    theSearch.getEntries().add(theSavedEntry);

                }
            } else {

                SavedProfileSearchEntry theSavedEntry = new SavedProfileSearchEntry();
                theEntry.setSavedSearchEntry(theSavedEntry);
                if (theFreelancer != null) {
                    theSavedEntry.setFreelancerId(theFreelancer.getId());
                }
                theSavedEntry.setUniqueDocumentId(theDocument.get(ProfileIndexerService.UNIQUE_ID));
                theSearch.getEntries().add(theSavedEntry);

                theResult.getEnties().add(theEntry);
            }

            if (theResult.getEnties().size() >= theMaxSearchResult) {

                theDuration = System.currentTimeMillis() - theStartTime;
                logger.logDebug("Reached max result count, duration = " + theDuration);

                profileSearchDAO.save(theSearch);

                return theResult;
            }
        }

        if (isExtendedSearch) {
            theResult.setTotalFound(theResult.getEnties().size());
        }

        theDuration = System.currentTimeMillis() - theStartTime;
        logger.logDebug("Finished, duration = " + theDuration);

        profileSearchDAO.save(theSearch);

        return theResult;
    }

    private StringBuilder getRealQuery(ProfileSearchRequest aRequest) {
        StringBuilder theRealQuery = new StringBuilder();
        StringTokenizer st = new StringTokenizer(aRequest.getProfileContent(), " ");
        while (st.hasMoreElements()) {
            if (theRealQuery.length() > 0) {
                theRealQuery.append(" AND ");
            }
            theRealQuery.append("CONTENT:\"");
            theRealQuery.append(st.nextToken().toLowerCase());
            theRealQuery.append("\"");
        }
        return theRealQuery;
    }

    public Vector<FreelancerProfile> findProfiles(String aCode) throws Exception {

        Vector<FreelancerProfile> theResult = new Vector<FreelancerProfile>();
        if (!StringUtils.isEmpty(aCode)) {

            String theRealQuery = "\"" + aCode + "\"";

            logger.logDebug("Search query is " + theRealQuery);

            Searcher theSearcher = new IndexSearcher(systemParameterService.getIndexerPath());
            Analyzer theAnalyzer = new KeywordAnalyzer();
            QueryParser theParser = new QueryParser(ProfileIndexerService.CODE, theAnalyzer);
            Query theQuery = theParser.parse(theRealQuery.toString());
            Hits theHits = theSearcher.search(theQuery);

            logger.logDebug("Size of search result is " + theHits.length());
            
            SimpleDateFormat theFormat = new SimpleDateFormat("dd.MM.yyyy");

            for (int i = 0; i < theHits.length(); i++) {

                Document theDocument = theHits.doc(i);

                FreelancerProfile theSearchResult = new FreelancerProfile();

                String theFileName = theDocument.get(ProfileIndexerService.PATH);
                
                Long theModifiedDate = Long.parseLong(theDocument.get(ProfileIndexerService.MODIFIED));
                Date theDate = new Date();
                theDate.setTime(theModifiedDate.longValue());
                
                theSearchResult.setInfotext("Aktualisiert : " + theFormat.format(theDate));
                
                int p = theFileName.lastIndexOf(File.separator);
                if (p >= 0) {
                    theFileName = theFileName.substring(p + 1);
                }

                String theBaseDir = systemParameterService.getIndexerNetworkDir();
                if (!theBaseDir.endsWith("\\")) {
                    theBaseDir += "\\";
                }

                theSearchResult.setName(theFileName);
                theSearchResult.setFileName(theBaseDir + theDocument.get(ProfileIndexerService.STRIPPEDPATH));

                theResult.add(theSearchResult);
            }

        }

        return theResult;
    }

    protected String getHighlightedSearchResult(Analyzer aAnalyzer, Highlighter aHighlighter, Document aDocument)
            throws IOException {

        String theContent = aDocument.get(ProfileIndexerService.ORIG_CONTENT);
        TokenStream tokenStream = aAnalyzer.tokenStream(ProfileIndexerService.CONTENT, new StringReader(theContent));

        return aHighlighter.getBestFragments(tokenStream, theContent, 5, "&nbsp;...&nbsp;");
    }

    public ProfileSearchResult getLastSearchResult() throws Exception {

        User theUser = (User) UserContextHolder.getUserContext().getAuthenticatable();

        SavedProfileSearch theSearch = profileSearchDAO.getSavedSearchFor(theUser);
        if (theSearch == null) {
            return null;
        }

        ProfileSearchRequest theRequest = new ProfileSearchRequest();
        copySearchRequestInto(theSearch, theRequest);

        ProfileSearchResult theResult = new ProfileSearchResult();
        theResult.setSearchRequest(theRequest);

        StringBuilder theRealQuery = getRealQuery(theRequest);

        Searcher theSearcher = new IndexSearcher(systemParameterService.getIndexerPath());
        Analyzer theAnalyzer = new StandardAnalyzer();
        QueryParser theQueryParser = new QueryParser(ProfileIndexerService.CONTENT, theAnalyzer);
        Query theQuery = theQueryParser.parse(theRealQuery.toString());
        Highlighter theHighlighter = new Highlighter(new SpanGradientFormatter(1, "#000000", "#0000FF", null, null),
                new QueryScorer(theQuery));

        Analyzer theDocumentSearchAnalyzer = new KeywordAnalyzer();
        QueryParser theDocumentSearchParser = new QueryParser(ProfileIndexerService.UNIQUE_ID,
                theDocumentSearchAnalyzer);

        boolean isExtendedSearch = theRequest.isExtendedSearch();

        for (SavedProfileSearchEntry theEntry : theSearch.getEntries()) {
            ProfileSearchEntry theSearchEntry = new ProfileSearchEntry();

            String theUniqueId = theEntry.getUniqueDocumentId();
            String theDocumentSearcjRealQuery = "\"" + theUniqueId + "\"";

            Query theDocumentQuery = theDocumentSearchParser.parse(theDocumentSearcjRealQuery);
            Hits theDocumentHits = theSearcher.search(theDocumentQuery);
            if (theDocumentHits.length() == 1) {

                Document theDocument = theDocumentHits.doc(0);

                theSearchEntry.setCode(theDocument.get(ProfileIndexerService.CODE));

                theSearchEntry.setHighlightResult(getHighlightedSearchResult(theAnalyzer, theHighlighter, theDocument));

                ProfileSearchInfoDetail theFreelancer;
                    theFreelancer = freelancerService.findFreelancerByCode(theSearchEntry.getCode());
                    
                theSearchEntry.setSavedSearchEntry(theEntry);
                theSearchEntry.setFreelancer(theFreelancer);

                if (isExtendedSearch) {
                    if (theFreelancer != null) {
                        theResult.getEnties().add(theSearchEntry);
                    }
                } else {
                    theResult.getEnties().add(theSearchEntry);
                }
            }
        }
        
        theResult.setTotalFound(theResult.getEnties().size());
        return theResult;
    }

    public void removeSavedSearchEntry(SavedProfileSearchEntry aSavedSearchEntry) {
        
        User theUser = (User) UserContextHolder.getUserContext().getAuthenticatable();

        SavedProfileSearch theSearch = profileSearchDAO.getSavedSearchFor(theUser);
        if (theSearch == null) {
            return;
        }

        theSearch.getEntries().remove(aSavedSearchEntry);
        profileSearchDAO.save(theSearch);
    }
}
