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

import de.mogwai.common.utils.WorkerQueue;
import de.powerstaff.business.dao.WebsiteDAO;
import de.powerstaff.business.entity.NewsletterMail;
import de.powerstaff.business.entity.WebProject;
import de.powerstaff.business.service.NewsletterService;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.ServiceLoggerService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class NewsletterServiceImpl implements NewsletterService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(NewsletterServiceImpl.class);

    private static final String SERVICE_ID = "Newsletter";

    private ServiceLoggerService serviceLogger;

    private WebsiteDAO websiteDAO;

    private PowerstaffSystemParameterService systemParameterService;

    public ServiceLoggerService getServiceLogger() {
        return serviceLogger;
    }

    public void setServiceLogger(ServiceLoggerService serviceLogger) {
        this.serviceLogger = serviceLogger;
    }

    public WebsiteDAO getWebsiteDAO() {
        return websiteDAO;
    }

    public void setWebsiteDAO(WebsiteDAO websiteDAO) {
        this.websiteDAO = websiteDAO;
    }

    /**
     * @return the systemParameterService
     */
    public PowerstaffSystemParameterService getSystemParameterService() {
        return systemParameterService;
    }

    /**
     * @param systemParameterService the systemParameterService to set
     */
    public void setSystemParameterService(PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    public void sendNewsletter() {
        sendNewsletter(false, null);
    }

    public void sendTestNewsletterTo(String aDebugAdress) {
        sendNewsletter(true, aDebugAdress);
    }

    public void sendNewsletter(boolean aDebugMode, String aDebugAdress) {

        if (!systemParameterService.isNewsletterEnabled() && !aDebugMode) {
            LOGGER.info("Newsletter wurde deaktiviert");
            return;
        }

        LOGGER.debug("Sending newsletter");

        Properties theMailProperties = new Properties();
        theMailProperties.put("mail.smtp.debug", Boolean.TRUE);

        final JavaMailSenderImpl theMailSender = new JavaMailSenderImpl();
        theMailSender.setHost(systemParameterService.getSmtpHost());
        theMailSender.setPort(systemParameterService.getSmtpPort());
        theMailSender.setUsername(systemParameterService.getSmtpUser());
        theMailSender.setPassword(systemParameterService.getSmtpPwd());
        theMailSender.setJavaMailProperties(theMailProperties);

        serviceLogger.logStart(SERVICE_ID, "");

        final List theProjects = new ArrayList<>();

        LOGGER.debug("Retrieving projects");

        for (WebProject theWebProject : websiteDAO.getCurrentProjects()) {

            if (!theWebProject.isNotifiedForNewsletter()) {
                theProjects.add(theWebProject);

                if (systemParameterService.isNewsletterSendDelta()) {
                    theWebProject.setNotifiedForNewsletter(true);
                    websiteDAO.saveOrUpdate(theWebProject);
                } else {
                    theWebProject.setNotifiedForNewsletter(false);
                    websiteDAO.saveOrUpdate(theWebProject);
                }
            } else {
                if (!systemParameterService.isNewsletterSendDelta()) {
                    theWebProject.setNotifiedForNewsletter(false);
                    websiteDAO.saveOrUpdate(theWebProject);
                }
            }

            if (aDebugMode && !theProjects.contains(theWebProject)) {
                theProjects.add(theWebProject);
            }
        }

        LOGGER.debug("Retrieving emails");

        Collection<NewsletterMail> theReceiver = websiteDAO.getConfirmedMails();

        LOGGER.debug("Sending mails for " + theReceiver.size() + " receiver");

        final List<NewsletterMail> theErrors = new ArrayList<>();

        // Initialize velocity
        Properties theProperties = new Properties();
        theProperties.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
                "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
        theProperties.setProperty("runtime.log.logsystem.log4j.category", "velocity");
        theProperties.setProperty("resource.loader", "class,newsletter");
        theProperties.setProperty("newsletter.resource.loader.class", "de.mogwai.common.velocity.FileResourceLoader");
        theProperties.setProperty("newsletter.resource.loader.description", "Newsletter resource loader");

        VelocityEngine theEngine = new VelocityEngine();
        try {
            theEngine.init(theProperties);
        } catch (Exception e2) {
            LOGGER.error("Error initializing velocity", e2);
            return;
        }

        File theTemplateFileName = new File(systemParameterService.getNewsletterTemplate());
        File theTemplateDir = theTemplateFileName.getParentFile();

        Template theTemplate;
        try {
            theTemplate = theEngine.getTemplate(systemParameterService.getNewsletterTemplate());
        } catch (Exception e2) {
            LOGGER.error("Error retrieving mail template", e2);
            return;
        }

        // Nur fär Debug - Zwecke !!!
        if (aDebugMode) {
            theReceiver = new ArrayList<>();
            NewsletterMail theTempMail = new NewsletterMail();
            theTempMail.setMail(aDebugAdress);
            theReceiver.add(theTempMail);
        }

        WorkerQueue theQueue = new WorkerQueue(systemParameterService.getNewsletterMaxThreadCount());

        if ((theProjects.size() > 0) && (theReceiver.size() > 0)) {

            int theCounter = 0;

            for (NewsletterMail theMail : theReceiver) {

                theCounter++;

                LOGGER.debug("Processing mail " + theMail.getMail());

                if (theCounter % 10 == 0) {
                    LOGGER.debug(theCounter + " mails sent");
                }

                try {

                    VelocityContext theContext = new VelocityContext();
                    theContext.put("receiver", theMail);
                    theContext.put("projects", theProjects);

                    final MimeMessage theMessage = theMailSender.createMimeMessage();
                    theMessage.setFrom(new InternetAddress(systemParameterService.getNewsletterSender()));
                    theMessage.setRecipients(RecipientType.TO, theMail.getMail());
                    theMessage.setSubject(systemParameterService.getNewsletterSubject());

                    MimeMultipart theMultipart = new MimeMultipart();
                    theMultipart.setSubType("related");

                    StringWriter theWriter = new StringWriter();
                    theTemplate.merge(theContext, theWriter);

                    MimeBodyPart theBodyPart = new MimeBodyPart();
                    theBodyPart.setContent(theWriter.toString(), "text/html");

                    theMultipart.addBodyPart(theBodyPart);

                    // Add the content to the mail
                    File[] theFiles = theTemplateDir.listFiles();
                    for (File theFile : theFiles) {
                        if ((!theFile.getName().endsWith(".html")) && (!theFile.isDirectory())) {

                            theBodyPart = new MimeBodyPart();
                            theBodyPart.setFileName(theFile.getName());
                            theBodyPart.setDataHandler(new DataHandler(new FileDataSource(theFile)));
                            theBodyPart.setHeader("Content-ID", theFile.getName());

                            theMultipart.addBodyPart(theBodyPart);
                        }
                    }

                    theMessage.setContent(theMultipart);

                    final NewsletterMail theFinalMail = theMail;

                    theQueue.execute(() -> {

                        try {
                            theMailSender.send(theMessage);

                            LOGGER.debug("Mail to " + theFinalMail.getMail() + " successfully sent");

                        } catch (Exception e) {

                            LOGGER.debug("Error sending mail", e);

                            theErrors.add(theFinalMail);

                            theFinalMail.setErrorcounter(theFinalMail.getErrorcounter() + 1);
                            try {
                                websiteDAO.saveOrUpdate(theFinalMail);
                            } catch (Exception e1) {
                                // Nothing will happen here
                            }

                        }
                    });

                    if (theCounter % systemParameterService.getNewsletterBatchCount() == 0) {

                        int theSleepInterval = systemParameterService.getNewsletterSleepIntervall();

                        LOGGER.debug("Going to sleep for " + theSleepInterval + "ms");

                        try {
                            Thread.sleep(theSleepInterval);
                        } catch (Exception e) {
                            // Nothing will happen here
                        }

                    }

                } catch (Exception e) {

                    LOGGER.debug("Error sending mail to " + theMail.getMail(), e);

                }
            }

        }

        while (theQueue.isRunning()) {

            LOGGER.debug("Waiting for queue to finish. " + theQueue.size() + " entries left");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Nothing will happen here
            }
        }

        theQueue.shutdown();

        serviceLogger.logEnd(SERVICE_ID, "" + theProjects.size() + " projects, " + theReceiver.size()
                + " receiver, errors " + theErrors.size());

        LOGGER.debug("Completed");
    }
}
