package de.powerstaff.business.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import de.mogwai.common.business.service.impl.LogableService;
import de.powerstaff.business.dao.FreelancerDAO;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.service.LuceneService;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.ProfileIndexerService;
import de.powerstaff.business.service.WrongDataService;

public class WrongDataServiceImpl extends LogableService implements
		WrongDataService {

	private PowerstaffSystemParameterService systemParameterService;

	private LuceneService luceneService;

	private FreelancerDAO freelancerDao;

	public void setSystemParameterService(
			PowerstaffSystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	public void setLuceneService(LuceneService luceneService) {
		this.luceneService = luceneService;
	}

	public void setFreelancerDao(FreelancerDAO freelancerDao) {
		this.freelancerDao = freelancerDao;
	}

	private String listFilesFor(String aField, String aValue,
			IndexSearcher aSearcher) throws IOException {
		StringBuilder theResult = new StringBuilder();
		Query theQuery = new TermQuery(new Term(aField, aValue));
		Hits theHits = aSearcher.search(theQuery);
		for (int i = 0; i < theHits.length(); i++) {
			Document theDocument = theHits.doc(i);
			if (i > 0) {
				theResult = theResult.append(";");
			}
			theResult.append(theDocument
					.get(ProfileIndexerService.STRIPPEDPATH));
		}

		return theResult.toString();
	}

	@Override
	public void run() throws Exception {

		logger.logInfo("Starting reporting for wrong data");

		File theReportFile = new File(systemParameterService
				.getWrongDataReportDir());
		File theDBOhneProfil = new File(theReportFile,
				"Freiberufler_mit_Code_ohne_Profil.csv");
		File theProfileOhneDB = new File(theReportFile,
				"Profile_ohne_Datenbankeintrag.csv");
		File theProfileDoppelterCode = new File(theReportFile,
				"Profile_Kodierung_doppelt.csv");
		File theProfileDoppelterInhalt = new File(theReportFile,
				"Profile_Doppelter_Inhalt.csv");

		Set<String> theKnownCodes = new HashSet<String>();
		Set<String> theKnownContent = new HashSet<String>();

		PrintWriter theDBOhneProfilWriter = null;
		PrintWriter theProfileOhneDBWriter = null;
		PrintWriter theProfileDoppelterCodeWriter = null;
		PrintWriter theProfileDoppelterInhaltWriter = null;

		try {

			theDBOhneProfilWriter = new PrintWriter(theDBOhneProfil);
			theProfileOhneDBWriter = new PrintWriter(theProfileOhneDB);
			theProfileDoppelterCodeWriter = new PrintWriter(
					theProfileDoppelterCode);
			theProfileDoppelterInhaltWriter = new PrintWriter(
					theProfileDoppelterInhalt);

			theProfileDoppelterCodeWriter.println("Kodierung;Dateinamen");
			theProfileDoppelterInhaltWriter.println("Kodierung;Dateinamen");
			theProfileOhneDBWriter.println("Kodierung;Dateinamen");
			theDBOhneProfilWriter.println("Kodierung;Name;Vorname");

			Set<String> theCodesFromDB = freelancerDao.getKnownCodesFromDB();

			IndexReader theReader = luceneService.getIndexReader();
			IndexSearcher theSearcher = luceneService.getIndexSearcher();

			for (int i = 0; i < theReader.numDocs(); i++) {

				if (i % 100 == 0) {
					logger.logInfo("Done with " + i + " documents");
				}

				if (!theReader.isDeleted(i)) {
					Document theDocument = theReader.document(i);

					String theCode = theDocument
							.get(ProfileIndexerService.CODE);
					String theContent = theDocument
							.get(ProfileIndexerService.SHACHECKSUM);
					String theMarchingRecord = theDocument
							.get(ProfileIndexerService.HASMATCHINGRECORD);

					theCodesFromDB.remove(theCode);

					if (!theKnownCodes.contains(theCode)) {
						theKnownCodes.add(theCode);
					} else {
						// Doppelter Code
						theProfileDoppelterCodeWriter.println(theCode
								+ ";"
								+ listFilesFor(ProfileIndexerService.CODE,
										theCode, theSearcher));

					}

					if (!theKnownContent.contains(theContent)) {
						theKnownContent.add(theContent);
					} else {
						theProfileDoppelterInhaltWriter.println(theCode
								+ ";"
								+ listFilesFor(
										ProfileIndexerService.SHACHECKSUM,
										theContent, theSearcher));
					}

					if (!theMarchingRecord.equals("true")) {
						// Kein Datensatz
						theProfileOhneDBWriter
								.println(theCode
										+ ";"
										+ theDocument
												.get(ProfileIndexerService.STRIPPEDPATH));
					}
				}
			}

			int count = 0;
			for (String theCode : theCodesFromDB) {

				count++;
				if (count % 100 == 0) {
					logger.logInfo("Done with " + count + " codes");
				}

				Freelancer theFreelancer = freelancerDao
						.findByCodeReal(theCode);
				if (theFreelancer != null) {
					theDBOhneProfilWriter.println(theCode + ";"
							+ theFreelancer.getName1() + ";"
							+ theFreelancer.getName2());

					freelancerDao.detach(theFreelancer);
				} else {
					theDBOhneProfilWriter.println(theCode);
				}
			}
		} finally {

			logger.logInfo("Finished");

			if (theDBOhneProfilWriter != null) {
				theDBOhneProfilWriter.close();
			}
			if (theProfileDoppelterCodeWriter != null) {
				theProfileDoppelterCodeWriter.close();
			}
			if (theProfileDoppelterInhaltWriter != null) {
				theProfileDoppelterInhaltWriter.close();
			}
			if (theProfileOhneDBWriter != null) {
				theProfileOhneDBWriter.close();
			}
		}
	}
}