package de.powerstaff.business.lucene.analysis;

public final class ProfileAnalyzerFactory {

	private ProfileAnalyzerFactory() {
	}

	public static ProfileAnalyzer createAnalyzer() {
		return new ProfileAnalyzer();
	}
}
