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

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;

public class GoogleStyleQueryParser {

    public static final char SPACE = ' ';

    public static final char DOUBLEQUOTES = '\"';

    public static final String EMPTY = "";
    
    private char delimiter;
    
    public GoogleStyleQueryParser() {
        this(SPACE);
    }

    public GoogleStyleQueryParser(char aDelimiter) {
        delimiter = aDelimiter;
    }

    protected boolean isWildcardTerm(String aTerm) {
        if ((aTerm.startsWith("*") || (aTerm.endsWith("*")))) {
            return true;
        }
        return false;
    }
    
    protected void addWildcardOrTermQueries(String aTerm, BooleanQuery aQuery, String aField, Analyzer aAnalyzer) throws IOException {

        Query theTempQuery;
        
        TokenStream theTokenStream = aAnalyzer.tokenStream(aField, new StringReader(aTerm));
        Token theToken = theTokenStream.next();
        while (theToken != null) {
            String theTokenText = theToken.termText();
            
            if (isWildcardTerm(aTerm)) {
                theTempQuery = new WildcardQuery(new Term(aField, theTokenText));        
            } else {
                theTempQuery = new TermQuery(new Term(aField, theTokenText));        
            }
            aQuery.add(theTempQuery, Occur.MUST);
            
            theToken = theTokenStream.next(theToken);
        }
    }
    
    protected void addPhraseQuery(String aTerm, BooleanQuery aQuery, String aField, Analyzer aAnalyzer) throws IOException {

        PhraseQuery thePhraseQuery = new PhraseQuery();
        
        TokenStream theTokenStream = aAnalyzer.tokenStream(aField, new StringReader(aTerm));
        Token theToken = theTokenStream.next();
        while (theToken != null) {
            String theTokenText = theToken.termText();

            Term theTerm = new Term(aField, theTokenText);
            thePhraseQuery.add(theTerm);
            
            theToken = theTokenStream.next(theToken);
        }
        
        aQuery.add(thePhraseQuery, Occur.MUST);
    }
    
    
    public Query parseQuery(String aQueryString, Analyzer aAnalyzer, String aField) throws ParseException, IOException {

        BooleanQuery theQuery = new BooleanQuery(true);
        
        aQueryString = aQueryString.trim();
        while (aQueryString.length() > 0) {
            int p = aQueryString.indexOf(DOUBLEQUOTES);
            if (p == 0) {
                int theEnd = aQueryString.indexOf(DOUBLEQUOTES, p + 1);
                if (theEnd > p) {
                    String theToken = aQueryString.substring(1, theEnd);
                    
                    addPhraseQuery(theToken, theQuery, aField, aAnalyzer);
                    
                    if (aQueryString.length() > theEnd) {
                        aQueryString = aQueryString.substring(theEnd + 1).trim();
                    } else {
                        aQueryString = EMPTY;
                    }
                } else {
                    throw new ParseException("Query error: missing double quotes");
                }
                
            } else {
                p = aQueryString.indexOf(delimiter);
                if (p > 0) {
                    String theToken = aQueryString.substring(0, p);
                    
                    addWildcardOrTermQueries(theToken, theQuery, aField, aAnalyzer);
                    
                    aQueryString = aQueryString.substring(p + 1).trim();

                } else {
                    String theToken = aQueryString;
                    
                    addWildcardOrTermQueries(theToken, theQuery, aField, aAnalyzer);
                    
                    aQueryString = EMPTY;
                }
            }
        }
        
        BooleanClause[] theClausses = theQuery.getClauses();
        if ((theClausses != null) && (theClausses.length == 1)) {
            return theClausses[0].getQuery();
        }
        
        return theQuery;
    }
}