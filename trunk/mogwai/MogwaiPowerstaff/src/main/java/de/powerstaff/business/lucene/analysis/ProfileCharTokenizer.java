package de.powerstaff.business.lucene.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.CharTokenizer;

public class ProfileCharTokenizer extends CharTokenizer {
    
    public static final String NON_TOKEN_CHARS = " ,:.?!(){}\"'<>\t\n\b\r\f;|";

    public ProfileCharTokenizer(Reader input) {
        super(input);
    }

    @Override
    protected boolean isTokenChar(char c) {
        return NON_TOKEN_CHARS.indexOf(c) < 0;
    }

    @Override
    protected char normalize(char c) {
        return Character.toLowerCase(c);
    }
}
