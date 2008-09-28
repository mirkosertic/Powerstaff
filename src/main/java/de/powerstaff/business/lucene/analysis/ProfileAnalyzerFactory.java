package de.powerstaff.business.lucene.analysis;

import org.apache.lucene.analysis.Analyzer;

public final class ProfileAnalyzerFactory {

    private ProfileAnalyzerFactory() {
    }
    
    public static Analyzer createAnalyzer() {
        return new ProfileAnalyzer();
    }
}
