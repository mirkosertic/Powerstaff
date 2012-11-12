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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.search.BooleanClause.Occur;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class GoogleStyleQueryParser {

    public static final char SPACE = ' ';

    public static final char DOUBLEQUOTES = '\"';

    public static final String EMPTY = "";

    private char delimiter;

    private IndexReader reader;

    public GoogleStyleQueryParser(IndexReader aReader) {
        this(aReader, SPACE);
    }

    public GoogleStyleQueryParser(IndexReader aReader, char aDelimiter) {
        delimiter = aDelimiter;
        reader = aReader;
    }

    protected boolean isWildcardTerm(String aTerm) {
        if ((aTerm.startsWith("*") || (aTerm.endsWith("*")))) {
            return true;
        }
        if ((aTerm.startsWith("%") || (aTerm.endsWith("%")))) {
            return true;
        }
        return false;
    }

    protected String getCorrectedWildcardTerm(String aTerm) {
        return aTerm.replace("%", "*");
    }

    protected void addWildcardOrTermQueries(String aTerm, BooleanQuery aQuery,
                                            String aField, Analyzer aAnalyzer) throws IOException {

        Query theTempQuery;

        TokenStream theTokenStream = aAnalyzer.tokenStream(aField,
                new StringReader(aTerm));
        while (theTokenStream.incrementToken()) {

            TermAttribute theTermAttribute = theTokenStream
                    .getAttribute(TermAttribute.class);

            String theTokenText = theTermAttribute.term();

            if (isWildcardTerm(aTerm)) {
                theTempQuery = new WildcardQuery(new Term(aField, getCorrectedWildcardTerm(aTerm)));
            } else {
                theTempQuery = new TermQuery(new Term(aField, theTokenText));
            }
            aQuery.add(theTempQuery, Occur.MUST);
        }
    }

    protected void addPhraseQuery(String aTerm, BooleanQuery aQuery,
                                  String aField, Analyzer aAnalyzer) throws IOException {

        MultiPhraseQuery thePhraseQuery = new MultiPhraseQuery();

        TokenStream theTokenStream = aAnalyzer.tokenStream(aField,
                new StringReader(aTerm));
        while (theTokenStream.incrementToken()) {

            TermAttribute theTermAttribute = theTokenStream
                    .getAttribute(TermAttribute.class);

            String theTokenText = theTermAttribute.term();

            Term theTerm = new Term(aField, theTokenText);

            if (!isWildcardTerm(theTokenText)) {
                thePhraseQuery.add(theTerm);
            } else {
                Term theWildcardTerm = new Term(theTerm.field(), getCorrectedWildcardTerm(theTerm.text()));
                WildcardTermEnum theEnum = new WildcardTermEnum(reader, theWildcardTerm);
                try {
                    List<Term> theTerms = new ArrayList<Term>();
                    do {
                        theTerms.add(theEnum.term());
                    } while (theEnum.next());
                    thePhraseQuery.add(theTerms.toArray(new Term[0]));
                } finally {
                    theEnum.close();
                }
            }
        }

        aQuery.add(thePhraseQuery, Occur.MUST);
    }

    public Query parseQuery(String aQueryString, Analyzer aAnalyzer,
                            String aField) throws ParseException, IOException {

        BooleanQuery theQuery = new BooleanQuery(true);

        if (aQueryString == null) {
            aQueryString = "";
        }
        aQueryString = aQueryString.trim();
        while (aQueryString.length() > 0) {
            int p = aQueryString.indexOf(DOUBLEQUOTES);
            if (p == 0) {
                int theEnd = aQueryString.indexOf(DOUBLEQUOTES, p + 1);
                if (theEnd > p) {
                    String theToken = aQueryString.substring(1, theEnd);

                    addPhraseQuery(theToken, theQuery, aField, aAnalyzer);

                    if (aQueryString.length() > theEnd) {
                        aQueryString = aQueryString.substring(theEnd + 1)
                                .trim();
                    } else {
                        aQueryString = EMPTY;
                    }
                } else {
                    throw new ParseException(
                            "Query error: missing double quotes");
                }

            } else {
                p = aQueryString.indexOf(delimiter);
                if (p > 0) {
                    String theToken = aQueryString.substring(0, p);

                    addWildcardOrTermQueries(theToken, theQuery, aField,
                            aAnalyzer);

                    aQueryString = aQueryString.substring(p + 1).trim();

                } else {
                    String theToken = aQueryString;

                    addWildcardOrTermQueries(theToken, theQuery, aField,
                            aAnalyzer);

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