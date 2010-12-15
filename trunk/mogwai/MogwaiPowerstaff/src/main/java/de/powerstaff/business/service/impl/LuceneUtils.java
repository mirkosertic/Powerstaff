package de.powerstaff.business.service.impl;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;

public final class LuceneUtils {

	private LuceneUtils() {
	}

	public static QueryParser createQueryParser(String aField,
			Analyzer aAnalyzer) {
		return new QueryParser(Version.LUCENE_24, aField, aAnalyzer);
	}
}