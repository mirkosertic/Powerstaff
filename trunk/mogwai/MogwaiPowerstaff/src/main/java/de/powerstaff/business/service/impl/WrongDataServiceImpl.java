package de.powerstaff.business.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import de.mogwai.common.business.service.impl.LogableService;
import de.powerstaff.business.dao.FreelancerDAO;
import de.powerstaff.business.dao.WebsiteDAO;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerContact;
import de.powerstaff.business.entity.NewsletterMail;
import de.powerstaff.business.service.LuceneService;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.ProfileIndexerService;
import de.powerstaff.business.service.WrongDataService;

public class WrongDataServiceImpl extends LogableService implements
		WrongDataService {

	private PowerstaffSystemParameterService systemParameterService;

	private LuceneService luceneService;

	private FreelancerDAO freelancerDao;

	private WebsiteDAO websiteDao;

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

	public void setWebsiteDao(WebsiteDAO websiteDao) {
		this.websiteDao = websiteDao;
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
				"Profile_doppelter_Inhalt.csv");
		File theFreelancerOhneNewsletter = new File(theReportFile,
				"Freiberufler_ohne_Newsletter.csv");
		File theFreelancerMitHomepageOhneKontakt = new File(theReportFile,
				"Freiberufler_mit_Homepage_ohne_Kontakt.csv");

		Set<String> theKnownCodes = new HashSet<String>();
		Set<String> theKnownContent = new HashSet<String>();

		PrintWriter theDBOhneProfilWriter = null;
		PrintWriter theProfileOhneDBWriter = null;
		PrintWriter theProfileDoppelterCodeWriter = null;
		PrintWriter theProfileDoppelterInhaltWriter = null;
		PrintWriter theFreelancerOhneNewsletterWriter = null;
		PrintWriter theFreelancerMitHomepageOhneKontaktWriter = null;

		try {

			theDBOhneProfilWriter = new PrintWriter(theDBOhneProfil);
			theProfileOhneDBWriter = new PrintWriter(theProfileOhneDB);
			theProfileDoppelterCodeWriter = new PrintWriter(
					theProfileDoppelterCode);
			theProfileDoppelterInhaltWriter = new PrintWriter(
					theProfileDoppelterInhalt);
			theFreelancerOhneNewsletterWriter = new PrintWriter(
					theFreelancerOhneNewsletter);
			theFreelancerMitHomepageOhneKontaktWriter = new PrintWriter(
					theFreelancerMitHomepageOhneKontakt);

			theProfileDoppelterCodeWriter.println("Kodierung;Dateinamen");
			theProfileDoppelterInhaltWriter.println("Kodierung;Dateinamen");
			theProfileOhneDBWriter.println("Kodierung;Dateinamen");
			theDBOhneProfilWriter.println("Kodierung;Name;Vorname");
			theFreelancerOhneNewsletterWriter
					.println("Kodierung;Name;Vorname;Mail");
			theFreelancerMitHomepageOhneKontaktWriter
					.println("Kodierung;Name;Vorname;Homepage");

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

			boolean newsletterEnabled = systemParameterService
					.isNewsletterEnabled();
			Set<String> theMails = new HashSet<String>();
			Date theStartDate = null;

			DateFormat theDateFormat = new SimpleDateFormat("dd.MM.yyyy");

			if (newsletterEnabled) {
				theStartDate = theDateFormat.parse(systemParameterService
						.getStartDateForNotInNewsletter());

				for (NewsletterMail theMail : websiteDao.getConfirmedMails()) {
					theMails.add(theMail.getMail().toLowerCase());
				}

			}

			count = 0;
			for (Iterator theIt = freelancerDao.getAllIterator(); theIt
					.hasNext();) {
				Freelancer theFreelancer = (Freelancer) theIt.next();

				String theLastContact = theFreelancer.getLastContact();

				if (newsletterEnabled) {
					if (!StringUtils.isEmpty(theLastContact)
							&& !theFreelancer.isContactforbidden()) {

						boolean validDate = true;
						try {
							Date theDate = theDateFormat.parse(theLastContact);
							if (!theDate.after(theStartDate)) {
								validDate = false;
							}
						} catch (Exception e) {
							validDate = false;
						}

						String theMail = "";

						if (validDate) {
							boolean hasMail = false;
							for (FreelancerContact theContact : theFreelancer
									.getContacts()) {
								if (theContact.getType().isEmail()) {
									theMail = theContact.getValue();
									if (theMails
											.contains(theMail.toLowerCase())) {
										hasMail = true;
									}
								}
							}

							if (!hasMail) {
								theFreelancerOhneNewsletterWriter
										.println(theFreelancer.getCode() + ";"
												+ theFreelancer.getName1()
												+ ";"
												+ theFreelancer.getName2()
												+ ";" + theMail);
							}
						}
					}
				}

				if (StringUtils.isEmpty(theLastContact)
						|| "kein kontakt".equals(theLastContact.trim()
								.toLowerCase())) {

					boolean hasHomepage = false;
					String theHomepage = null;
					for (FreelancerContact theContact : theFreelancer
							.getContacts()) {
						if (theContact.getType().isWeb()) {
							theHomepage = theContact.getValue();
							hasHomepage = true;
						}
					}

					if (hasHomepage) {
						theFreelancerMitHomepageOhneKontaktWriter
								.println(theFreelancer.getCode() + ";"
										+ theFreelancer.getName1() + ";"
										+ theFreelancer.getName2() + ";"
										+ theHomepage);
					}

				}

				freelancerDao.detach(theFreelancer);

				count++;
				if (count % 100 == 0) {
					logger.logInfo("Done with " + count + " freelancer");
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
			if (theFreelancerOhneNewsletterWriter != null) {
				theFreelancerOhneNewsletterWriter.close();
			}
			if (theFreelancerMitHomepageOhneKontaktWriter != null) {
				theFreelancerMitHomepageOhneKontaktWriter.close();
			}
		}
	}
}