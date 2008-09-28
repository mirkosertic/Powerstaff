package de.powerstaff.business.lucene.analysis;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LengthFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WordlistLoader;

class ProfileAnalyzer extends Analyzer {

    private static final int MIN_LENGTH = 2;

    private static final int MAX_LENGTH = Integer.MAX_VALUE;

    private Set<String> stopSet = new HashSet<String>();

    public ProfileAnalyzer() {
        URL theUrl = getClass().getResource("/de/powerstaff/business/lucene/analysis/Stoplist.txt");
        try {
            stopSet = WordlistLoader.getWordSet(new InputStreamReader(theUrl.openStream()));
        } catch (IOException e) {
            throw new RuntimeException("Error loading stoplist", e);
        }
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        ProfileCharTokenizer tokenStream = new ProfileCharTokenizer(reader);
        TokenFilter theFilter = new LengthFilter(tokenStream, MIN_LENGTH, MAX_LENGTH);
        theFilter = new StopFilter(theFilter, stopSet);
        return theFilter;
    }

    private static final class SavedStreams {
        private ProfileCharTokenizer tokenStream;

        private TokenFilter filter;
    }

    @Override
    public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
        SavedStreams streams = (SavedStreams) getPreviousTokenStream();
        if (streams == null) {
            streams = new SavedStreams();
            setPreviousTokenStream(streams);
            streams.tokenStream = new ProfileCharTokenizer(reader);
            streams.filter = new LengthFilter(streams.tokenStream, MIN_LENGTH, MAX_LENGTH);
            streams.filter = new StopFilter(streams.filter, stopSet);
        } else {
            streams.tokenStream.reset(reader);
        }

        return streams.filter;
    }
}