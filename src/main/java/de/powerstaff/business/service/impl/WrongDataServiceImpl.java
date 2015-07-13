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

import de.powerstaff.business.dao.WebsiteDAO;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerContact;
import de.powerstaff.business.entity.NewsletterMail;
import de.powerstaff.business.service.FSCache;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.WrongDataService;
import de.powerstaff.web.backingbean.freelancer.FreelancerBackingBeanDataModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.*;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WrongDataServiceImpl implements
        WrongDataService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(WrongDataServiceImpl.class);

    private PowerstaffSystemParameterService systemParameterService;

    private SessionFactory sessionFactory;

    private WebsiteDAO websiteDao;

    private FSCache fsCache;

    public void setSystemParameterService(
            PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setFsCache(FSCache fsCache) {
        this.fsCache = fsCache;
    }

    public void setWebsiteDao(WebsiteDAO websiteDao) {
        this.websiteDao = websiteDao;
    }

    private String saveString(Date aValue) {
        if (aValue == null) {
            return "";
        }
        SimpleDateFormat theFormat = new SimpleDateFormat("dd.MM.yyyy");
        return theFormat.format(aValue);
    }

    private String saveString(String aValue) {
        if (aValue == null) {
            return "";
        }
        return aValue;
    }

    private void processFreelancer(File aReportFile) throws FileNotFoundException, ParseException {

        File theDBOhneProfil = new File(aReportFile,
                "Freiberufler_mit_Code_ohne_Profil.csv");
        File theFreelancerOhneNewsletter = new File(aReportFile,
                "Freiberufler_ohne_Newsletter.csv");
        File theFreelancerMitHomepageOhneKontakt = new File(aReportFile,
                "Freiberufler_mit_Homepage_ohne_Kontakt.csv");
        File theFreelancerForNewsletter = new File(aReportFile,
                "Freiberufler_f�r_Newsletter.csv");
        File theProfileOhneDB = new File(aReportFile,
                "Profile_ohne_Datenbankeintrag.csv");
        File theProfileDoppelterCode = new File(aReportFile,
                "Profile_Kodierung_doppelt.csv");

        PrintWriter theDBOhneProfilWriter = null;
        PrintWriter theFreelancerOhneNewsletterWriter = null;
        PrintWriter theFreelancerMitHomepageOhneKontaktWriter = null;
        PrintWriter theFreelancerForNewsletterWriter = null;
        PrintWriter theProfileOhneDBWriter = null;
        PrintWriter theProfileDoppelterCodeWriter = null;

        FreelancerBackingBeanDataModel theModel = new FreelancerBackingBeanDataModel();

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
            theFreelancerForNewsletterWriter.println("K�rzel;Name;Vorname;Titel;eMail;Eintrag in Kreditor;Verf�gbarkeit;Homepage;letzter Kontakt;Status;Xing;Gulp");
            theProfileOhneDBWriter.println("Kodierung;Dateinamen");
            theProfileDoppelterCodeWriter.println("Kodierung;Dateinamen");

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

            Session theSession = sessionFactory.getCurrentSession();
            int theFetchSize = 100;
            int theLogCount = theFetchSize * 10;

            Query theQuery = theSession.createQuery("from Freelancer");
            theQuery.setFetchSize(theFetchSize);
            ScrollableResults theResults = theQuery.scroll(ScrollMode.FORWARD_ONLY);
            int counter = 0;

            Set<String> theKnownCodes = new HashSet<String>();

            while (theResults.next()) {
                Freelancer theFreelancer = (Freelancer) theResults.get(0);

                String theCode = theFreelancer.getCode();
                if (!StringUtils.isEmpty(theCode)) {
                    theCode = theCode.toLowerCase();
                    theKnownCodes.add(theCode);

                    Set<File> theFiles = fsCache.getFilesForCode(theCode);
                    if ((theFiles == null || theFiles.size() == 0)) {
                        theDBOhneProfilWriter.println(theCode + ";"
                                + saveString(theFreelancer.getName1()) + ";"
                                + saveString(theFreelancer.getName2()) + ";" + saveString(theFreelancer.getKreditorNr()));
                    }
                }

                List<FreelancerContact> theMailContacts = theFreelancer.getEMailContacts();
                List<FreelancerContact> theWebContacts = theFreelancer.getWebContacts();

                Date theLastContact = theFreelancer.getLastContactDate();

                if (!theFreelancer.isContactforbidden()) {

                    String theMail = null;
                    for (FreelancerContact theContact : theMailContacts) {
                        if (StringUtils.isEmpty(theMail) && "eMail".equalsIgnoreCase(theContact.getType().getDescription())) {
                            theMail = theContact.getValue();
                        }
                    }
                    String theWeb = "";
                    for (FreelancerContact theContact : theWebContacts) {
                        if (StringUtils.isEmpty(theWeb) && "Web".equalsIgnoreCase(theContact.getType().getDescription())) {
                            theWeb = theContact.getValue();
                        }
                    }
                    String theGulp = "";
                    for (FreelancerContact theContact : theWebContacts) {
                        if (StringUtils.isEmpty(theWeb) && "Gulp".equalsIgnoreCase(theContact.getType().getDescription())) {
                            theGulp = theContact.getValue();
                        }
                    }

                    String theXing = "";
                    for (FreelancerContact theContact : theWebContacts) {
                        if (StringUtils.isEmpty(theWeb) && "Xing".equalsIgnoreCase(theContact.getType().getDescription())) {
                            theXing = theContact.getValue();
                        }
                    }


                    String theAvailable = "";
                    Date theAvailability = theFreelancer.getAvailabilityAsDate();
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
                    theFreelancerForNewsletterWriter.print(saveString(theFreelancer.getKreditorNr()));
                    theFreelancerForNewsletterWriter.print(";");
                    theFreelancerForNewsletterWriter.print(saveString(theAvailable));
                    theFreelancerForNewsletterWriter.print(";");
                    theFreelancerForNewsletterWriter.print(saveString(theWeb));
                    theFreelancerForNewsletterWriter.print(";");
                    theFreelancerForNewsletterWriter.print(saveString(theLastContact));
                    theFreelancerForNewsletterWriter.print(";");
                    theFreelancerForNewsletterWriter.print(saveString(theModel.getStatusAsString(theFreelancer.getStatus())));
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
                        for (FreelancerContact theContact : theMailContacts) {
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
                    for (FreelancerContact theContact : theWebContacts) {
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

            Set<String> theCodesFromFiles = new HashSet<String>();
            theCodesFromFiles.addAll(fsCache.getKnownCodes());
            for (String theCode : theCodesFromFiles) {
                Set<File> theFiles = fsCache.getFilesForCode(theCode);
                if (theFiles != null && theFiles.size() > 1) {
                    // Doppelter Code
                    StringBuilder theBuilder = new StringBuilder();
                    for (File theFile : theFiles) {
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

            for (String theCode : theCodesFromFiles) {
                Set<File> theFiles = fsCache.getFilesForCode(theCode);
                if (theFiles != null) {
                    for (File theFile : theFiles) {
                        theProfileOhneDBWriter
                                .println(theCode
                                        + ";"
                                        + theFile);
                    }
                }
            }
        } catch (Exception e) {
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

    private void processFiles(File aReportFile) {
        File theProfileDoppelterInhalt = new File(aReportFile,
                "Profile_doppelter_Inhalt.csv");

        PrintWriter theProfileDoppelterInhaltWriter = null;

        try {
            theProfileDoppelterInhaltWriter = new PrintWriter(
                    theProfileDoppelterInhalt);

            theProfileDoppelterInhaltWriter.println("Dateinamen");

            Map<String, Set<File>> theHashes = new HashMap<String, Set<File>>();

            Set<String> theCodesFromFiles = new HashSet<String>();
            theCodesFromFiles.addAll(fsCache.getKnownCodes());

            for (String theCode : theCodesFromFiles) {
                Set<File> theFiles = fsCache.getFilesForCode(theCode);
                if (theFiles != null) {
                    for (File theFile : theFiles) {
                        InputStream theStream = null;
                        try {
                            theStream = new FileInputStream(theFile);
                            String theHash = DigestUtils.sha256Hex(theStream);

                            Set<File> theFilesForHash = theHashes.get(theHash);
                            if (theFilesForHash == null) {
                                theFilesForHash = new HashSet<File>();
                                theHashes.put(theHash, theFilesForHash);
                            }
                            theFilesForHash.add(theFile);
                        } catch (Exception e) {
                            LOGGER.error("Error scanning file " + theFile, e);
                        } finally {
                            IOUtils.closeQuietly(theStream);
                        }
                    }
                }
            }

            for (Map.Entry<String, Set<File>> theEntry : theHashes.entrySet()) {
                if (theEntry.getValue().size() > 1) {
                    StringBuilder theBuilder = new StringBuilder();
                    for (File theFile : theEntry.getValue()) {
                        if (theBuilder.length() > 0) {
                            theBuilder.append(";");
                        }
                        theBuilder.append(theFile.toString());
                    }
                    theProfileDoppelterInhaltWriter.println(theBuilder);
                }
            }

        } catch (Exception e) {
            LOGGER.error("Error processing files", e);
        } finally {
            IOUtils.closeQuietly(theProfileDoppelterInhaltWriter);
        }
    }

    @Override
    public void run() throws Exception {

        LOGGER.info("Starting reporting for wrong data");

        File theReportFile = new File(systemParameterService
                .getWrongDataReportDir());

        processFreelancer(theReportFile);
        processFiles(theReportFile);

        LOGGER.info("Finished");
    }
}