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
import java.io.StringReader;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SpanGradientFormatter;

import de.mogwai.common.business.service.impl.LogableService;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.dto.ProfileSearchInfoDetail;
import de.powerstaff.business.dto.ProfileSearchRequest;
import de.powerstaff.business.dto.ProfileSearchResult;
import de.powerstaff.business.entity.FreelancerProfile;
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

    public ProfileSearchResult searchDocument(ProfileSearchRequest aRequest) throws Exception {

        int theMaxSearchResult = systemParameterService.getProfileMaxSearchResult();

        ProfileSearchResult theResult = new ProfileSearchResult();

        StringBuilder theRealQuery = new StringBuilder();
        StringTokenizer st = new StringTokenizer(aRequest.getProfileContent(), " ");
        while (st.hasMoreElements()) {
            if (theRealQuery.length() > 0) {
                theRealQuery.append(" AND ");
            }
            theRealQuery.append("\"");
            theRealQuery.append(st.nextToken().toLowerCase());
            theRealQuery.append("\"");
        }

        logger.logDebug("Search query is " + theRealQuery);

        long theStartTime = System.currentTimeMillis();

        Searcher theSearcher = new IndexSearcher(systemParameterService.getIndexerPath());
        Analyzer theAnalyzer = new StandardAnalyzer();
        QueryParser theParser = new QueryParser(ProfileIndexerService.CONTENT, theAnalyzer);
        Query theQuery = theParser.parse(theRealQuery.toString());
        Highlighter theHighlighter = new Highlighter(new SpanGradientFormatter(1, "#000000", "#0000FF", null, null),
                new QueryScorer(theQuery));

        Hits theHits = theSearcher.search(theQuery);

        long theDuration = System.currentTimeMillis() - theStartTime;

        logger.logDebug("Size of search result is " + theHits.length() + " duration = " + theDuration);

        theResult.setTotalFound(theHits.length());
        
        for (int i = 0; i < theHits.length(); i++) {

            Document theDocument = theHits.doc(i);

            ProfileSearchEntry theEntry = new ProfileSearchEntry();
            theEntry.setCode(theDocument.get(ProfileIndexerService.CODE));

            ProfileSearchInfoDetail theFreelancer = freelancerService.findFreelancerByCode(theEntry.getCode());
            if (theFreelancer != null) {
                theEntry.setFreelancer(theFreelancer);
            }

            String theContent = theDocument.get(ProfileIndexerService.ORIG_CONTENT);
            TokenStream tokenStream = theAnalyzer.tokenStream(ProfileIndexerService.CONTENT, new StringReader(
                    theContent));

            String theHighlight = theHighlighter.getBestFragments(tokenStream, theContent, 5, "&nbsp;...&nbsp;");
            theEntry.setHighlightResult(theHighlight);

            theResult.getEnties().add(theEntry);

            if (theResult.getEnties().size() >= theMaxSearchResult) {

                theDuration = System.currentTimeMillis() - theStartTime;
                logger.logDebug("Reached max result count, duration = " + theDuration);

                return theResult;
            }
        }

        theDuration = System.currentTimeMillis() - theStartTime;
        logger.logDebug("Finished, duration = " + theDuration);

        return theResult;
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

            for (int i = 0; i < theHits.length(); i++) {

                Document theDocument = theHits.doc(i);

                FreelancerProfile theSearchResult = new FreelancerProfile();

                String theFileName = theDocument.get(ProfileIndexerService.PATH);
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
}
