/*
  Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.

  This library is free software; you can redistribute it and/or modify it under
  the terms of the GNU Lesser General Public License as published by the Free
  Software Foundation; either version 2.1 of the License, or (at your option)
  any later version.

  This library is distributed in the hope that it will be useful, but WITHOUT
  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
  FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
  details.

  You should have received a copy of the GNU Lesser General Public License
  along with this library; if not, write to the Free Software Foundation, Inc.,
  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.powerstaff.business.service.impl;

import de.powerstaff.business.dao.WebsiteDAO;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerContact;
import de.powerstaff.business.entity.NewsletterMail;
import de.powerstaff.business.service.FSCache;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.WrongDataService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WrongDataServiceImpl implements
        WrongDataService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(WrongDataServiceImpl.class);

    private PowerstaffSystemParameterService systemParameterService;

    private SessionFactory sessionFactory;

    private WebsiteDAO websiteDao;

    private FSCache fsCache;

    public void setSystemParameterService(
            final PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setFsCache(final FSCache fsCache) {
        this.fsCache = fsCache;
    }

    public void setWebsiteDao(final WebsiteDAO websiteDao) {
        this.websiteDao = websiteDao;
    }

    private String saveString(final Date aValue) {
        if (aValue == null) {
            return "";
        }
        final SimpleDateFormat theFormat = new SimpleDateFormat("dd.MM.yyyy");
        return theFormat.format(aValue);
    }

    private String saveString(final String aValue) {
        if (aValue == null) {
            return "";
        }
        return aValue;
    }

    private void processFreelancer(final File aReportFile) throws FileNotFoundException, ParseException {

        final File theDBOhneProfil = new File(aReportFile,
                "Freiberufler_mit_Code_ohne_Profil.csv");
        final File theFreelancerOhneNewsletter = new File(aReportFile,
                "Freiberufler_ohne_Newsletter.csv");
        final File theFreelancerMitHomepageOhneKontakt = new File(aReportFile,
                "Freiberufler_mit_Homepage_ohne_Kontakt.csv");
        final File theFreelancerForNewsletter = new File(aReportFile,
                "Freiberufler_fär_Newsletter.csv");
        final File theProfileOhneDB = new File(aReportFile,
                "Profile_ohne_Datenbankeintrag.csv");
        final File theProfileDoppelterCode = new File(aReportFile,
                "Profile_Kodierung_doppelt.csv");

        PrintWriter theDBOhneProfilWriter = null;
        PrintWriter theFreelancerOhneNewsletterWriter = null;
        PrintWriter theFreelancerMitHomepageOhneKontaktWriter = null;
        PrintWriter theFreelancerForNewsletterWriter = null;
        PrintWriter theProfileOhneDBWriter = null;
        PrintWriter theProfileDoppelterCodeWriter = null;

        try {

            theProfileDoppelterCodeWriter = new PrintWriter(
                    theProfileDoppelterCode);

            theDBOhneProfilWriter = new PrintWriter(theDBOhneProfil);
            theFreelancerOhneNewsletterWriter = new PrintWriter(
                    theFreelancerOhneNewsletter);
            theFreelancerMitHomepageOhneKontaktWriter = new PrintWriter(
                    theFreelancerMitHomepageOhneKontakt);
            theFreelancerForNewsletterWriter = new PrintWriter(
                    theFreelancerForNewsletter);
            theProfileOhneDBWriter = new PrintWriter(theProfileOhneDB);

            theDBOhneProfilWriter.println("Kodierung;Name;Vorname;Kreditor");
            theFreelancerOhneNewsletterWriter
                    .println("Kodierung;Name;Vorname;Mail");
            theFreelancerMitHomepageOhneKontaktWriter
                    .println("Kodierung;Name;Vorname;Homepage");
            theFreelancerForNewsletterWriter.println("Kürzel;Name;Vorname;Titel;eMail;Eintrag in Kreditor;Verfügbarkeit;Homepage;letzter Kontakt;Xing;Gulp");
            theProfileOhneDBWriter.println("Kodierung;Dateinamen");
            theProfileDoppelterCodeWriter.println("Kodierung;Dateinamen");

            final boolean newsletterEnabled = systemParameterService
                    .isNewsletterEnabled();
            final Set<String> theMails = new HashSet<>();
            Date theStartDate = null;

            final DateFormat theDateFormat = new SimpleDateFormat("dd.MM.yyyy");

            if (newsletterEnabled) {
                theStartDate = theDateFormat.parse(systemParameterService
                        .getStartDateForNotInNewsletter());

                for (final NewsletterMail theMail : websiteDao.getConfirmedMails()) {
                    theMails.add(theMail.getMail().toLowerCase());
                }

            }

            final Session theSession = sessionFactory.getCurrentSession();
            final int theFetchSize = 100;
            final int theLogCount = theFetchSize * 10;

            final Query theQuery = theSession.createQuery("from Freelancer");
            theQuery.setFetchSize(theFetchSize);
            final ScrollableResults theResults = theQuery.scroll(ScrollMode.FORWARD_ONLY);
            int counter = 0;

            final Set<String> theKnownCodes = new HashSet<>();

            while (theResults.next()) {
                final Freelancer theFreelancer = (Freelancer) theResults.get(0);

                String theCode = theFreelancer.getCode();
                if (!StringUtils.isEmpty(theCode)) {
                    theCode = theCode.toLowerCase();
                    theKnownCodes.add(theCode);

                    final Set<File> theFiles = fsCache.getFilesForCode(theCode);
                    if ((theFiles == null || theFiles.size() == 0)) {
                        theDBOhneProfilWriter.println(theCode + ";"
                                + saveString(theFreelancer.getName1()) + ";"
                                + saveString(theFreelancer.getName2()) + ";" + saveString(theFreelancer.getKreditorNr()));
                    }
                }

                final List<FreelancerContact> theMailContacts = theFreelancer.getEMailContacts();
                final List<FreelancerContact> theWebContacts = theFreelancer.getWebContacts();

                final Date theLastContact = theFreelancer.getLastContactDate();

                if (!theFreelancer.isContactforbidden()) {

                    String theMail = null;
                    for (final FreelancerContact theContact : theMailContacts) {
                        if (StringUtils.isEmpty(theMail) && "eMail".equalsIgnoreCase(theContact.getType().getDescription())) {
                            theMail = theContact.getValue();
                        }
                    }
                    String theWeb = "";
                    for (final FreelancerContact theContact : theWebContacts) {
                        if (StringUtils.isEmpty(theWeb) && "Web".equalsIgnoreCase(theContact.getType().getDescription())) {
                            theWeb = theContact.getValue();
                        }
                    }
                    String theGulp = "";
                    for (final FreelancerContact theContact : theWebContacts) {
                        if (StringUtils.isEmpty(theWeb) && "Gulp".equalsIgnoreCase(theContact.getType().getDescription())) {
                            theGulp = theContact.getValue();
                        }
                    }

                    String theXing = "";
                    for (final FreelancerContact theContact : theWebContacts) {
                        if (StringUtils.isEmpty(theWeb) && "Xing".equalsIgnoreCase(theContact.getType().getDescription())) {
                            theXing = theContact.getValue();
                        }
                    }


                    String theAvailable = "";
                    final Date theAvailability = theFreelancer.getAvailabilityAsDate();
                    if (theAvailability != null) {
                        theAvailable = theDateFormat.format(theAvailability);
                    }

                    theFreelancerForNewsletterWriter.print(saveString(theFreelancer.getCode()));
                    theFreelancerForNewsletterWriter.print(";");
                    theFreelancerForNewsletterWriter.print(saveString(theFreelancer.getName1()));
                    theFreelancerForNewsletterWriter.print(";");
                    theFreelancerForNewsletterWriter.print(saveString(theFreelancer.getName2()));
                    theFreelancerForNewsletterWriter.print(";");
                    theFreelancerForNewsletterWriter.print(saveString(theFreelancer.getTitel()));
                    theFreelancerForNewsletterWriter.print(";");
                    theFreelancerForNewsletterWriter.print(saveString(theMail));
                    theFreelancerForNewsletterWriter.print(";");
                    theFreelancerForNewsletterWriter.print(saveString(theFreelancer.getKreditorNeu()));
                    theFreelancerForNewsletterWriter.print(";");
                    theFreelancerForNewsletterWriter.print(saveString(theAvailable));
                    theFreelancerForNewsletterWriter.print(";");
                    theFreelancerForNewsletterWriter.print(saveString(theWeb));
                    theFreelancerForNewsletterWriter.print(";");
                    theFreelancerForNewsletterWriter.print(saveString(theLastContact));
                    theFreelancerForNewsletterWriter.print(";");
                    theFreelancerForNewsletterWriter.print(saveString(theXing));
                    theFreelancerForNewsletterWriter.print(";");
                    theFreelancerForNewsletterWriter.print(saveString(theGulp));
                    theFreelancerForNewsletterWriter.println();
                }

                if (newsletterEnabled) {

                    if (theLastContact != null
                            && !theFreelancer.isContactforbidden()) {

                        String theMail = "";

                        boolean hasMail = false;
                        for (final FreelancerContact theContact : theMailContacts) {
                            theMail = theContact.getValue();
                            if (theMails
                                    .contains(theMail.toLowerCase())) {
                                hasMail = true;
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

                if (theLastContact == null) {

                    boolean hasHomepage = false;
                    String theHomepage = null;
                    for (final FreelancerContact theContact : theWebContacts) {
                        theHomepage = theContact.getValue();
                        hasHomepage = true;
                    }

                    if (hasHomepage) {
                        theFreelancerMitHomepageOhneKontaktWriter
                                .println(theFreelancer.getCode() + ";"
                                        + theFreelancer.getName1() + ";"
                                        + theFreelancer.getName2() + ";"
                                        + theHomepage);
                    }

                }

                if (counter % theLogCount == 0) {
                    LOGGER.info("Processing record " + counter);
                }

                if (counter % theFetchSize == 0) {

                    LOGGER.debug("Flushing session");
                    theSession.clear();
                }
                counter++;
            }

            final Set<String> theCodesFromFiles = new HashSet<>(fsCache.getKnownCodes());
            for (final String theCode : theCodesFromFiles) {
                final Set<File> theFiles = fsCache.getFilesForCode(theCode);
                if (theFiles != null && theFiles.size() > 1) {
                    // Doppelter Code
                    final StringBuilder theBuilder = new StringBuilder();
                    for (final File theFile : theFiles) {
                        if (theBuilder.length() > 0) {
                            theBuilder.append(";");
                        }
                        theBuilder.append(theFile.toString());
                    }
                    theProfileDoppelterCodeWriter.println(theCode
                            + ";"
                            + theBuilder);
                }
            }

            theCodesFromFiles.removeAll(theKnownCodes);

            for (final String theCode : theCodesFromFiles) {
                final Set<File> theFiles = fsCache.getFilesForCode(theCode);
                if (theFiles != null) {
                    for (final File theFile : theFiles) {
                        theProfileOhneDBWriter
                                .println(theCode
                                        + ";"
                                        + theFile);
                    }
                }
            }
        } catch (final Exception e) {
            LOGGER.error("Error processing freelancer", e);
        } finally {
            IOUtils.closeQuietly(theDBOhneProfilWriter);
            IOUtils.closeQuietly(theFreelancerOhneNewsletterWriter);
            IOUtils.closeQuietly(theFreelancerMitHomepageOhneKontaktWriter);
            IOUtils.closeQuietly(theFreelancerForNewsletterWriter);
            IOUtils.closeQuietly(theProfileOhneDBWriter);
            IOUtils.closeQuietly(theProfileDoppelterCodeWriter);
        }
    }

    private void processFiles(final File aReportFile) {
        final File theProfileDoppelterInhalt = new File(aReportFile,
                "Profile_doppelter_Inhalt.csv");

        PrintWriter theProfileDoppelterInhaltWriter = null;

        try {
            theProfileDoppelterInhaltWriter = new PrintWriter(
                    theProfileDoppelterInhalt);

            theProfileDoppelterInhaltWriter.println("Dateinamen");

            final Map<String, Set<File>> theHashes = new HashMap<>();

            final Set<String> theCodesFromFiles = new HashSet<>(fsCache.getKnownCodes());

            for (final String theCode : theCodesFromFiles) {
                final Set<File> theFiles = fsCache.getFilesForCode(theCode);
                if (theFiles != null) {
                    for (final File theFile : theFiles) {
                        InputStream theStream = null;
                        try {
                            theStream = new FileInputStream(theFile);
                            final String theHash = DigestUtils.sha256Hex(theStream);

                            final Set<File> theFilesForHash = theHashes.computeIfAbsent(theHash, k -> new HashSet<>());
                            theFilesForHash.add(theFile);
                        } catch (final Exception e) {
                            LOGGER.error("Error scanning file " + theFile, e);
                        } finally {
                            IOUtils.closeQuietly(theStream);
                        }
                    }
                }
            }

            for (final Map.Entry<String, Set<File>> theEntry : theHashes.entrySet()) {
                if (theEntry.getValue().size() > 1) {
                    final StringBuilder theBuilder = new StringBuilder();
                    for (final File theFile : theEntry.getValue()) {
                        if (theBuilder.length() > 0) {
                            theBuilder.append(";");
                        }
                        theBuilder.append(theFile.toString());
                    }
                    theProfileDoppelterInhaltWriter.println(theBuilder);
                }
            }

        } catch (final Exception e) {
            LOGGER.error("Error processing files", e);
        } finally {
            IOUtils.closeQuietly(theProfileDoppelterInhaltWriter);
        }
    }

    @Override
    public void run() throws Exception {

        LOGGER.info("Starting reporting for wrong data");

        final File theReportFile = new File(systemParameterService
                .getWrongDataReportDir());

        processFreelancer(theReportFile);
        processFiles(theReportFile);

        LOGGER.info("Finished");
    }
}