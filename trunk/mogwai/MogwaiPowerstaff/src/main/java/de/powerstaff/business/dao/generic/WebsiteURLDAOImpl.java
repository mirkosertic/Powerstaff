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
package de.powerstaff.business.dao.generic;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.digester.Digester;

import de.mogwai.common.logging.Logger;
import de.powerstaff.business.dao.WebsiteDAO;
import de.powerstaff.business.entity.NewsletterMail;
import de.powerstaff.business.entity.WebProject;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.impl.ResultVector;

public class WebsiteURLDAOImpl implements WebsiteDAO {

    private static final Logger LOGGER = new Logger(WebsiteURLDAOImpl.class);

    private PowerstaffSystemParameterService systemParameterService;

    /**
     * @return the systemParameterService
     */
    public PowerstaffSystemParameterService getSystemParameterService() {
        return systemParameterService;
    }

    /**
     * @param systemParameterService
     *            the systemParameterService to set
     */
    public void setSystemParameterService(PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    protected InputStream executeStatement(String aStatement) throws IOException {
        return executeStatement(aStatement, null);
    }

    protected InputStream executeStatement(String aStatement, HashMap aParams) throws IOException {

        String theConfigURL = systemParameterService.getRemoteWebUrl();

        LOGGER.logDebug("Executing statement " + aStatement + " to url " + theConfigURL);

        URL theURL = new URL(theConfigURL);
        HttpURLConnection theConnection = (HttpURLConnection) theURL.openConnection();

        ClientHttpRequest theRequest = new ClientHttpRequest(theConnection);
        theRequest.setParameter("statement", aStatement);

        if (aParams != null) {
            for (Object theKey : aParams.keySet()) {
                Object theValue = aParams.get(theKey);
                String theValueAsString = theValue != null ? theValue.toString() : "";

                theRequest.setParameter((String) theKey, theValueAsString);
            }
        }

        InputStream theServerResponse = theRequest.post();

        LOGGER.logDebug("Response code was " + theConnection.getResponseCode() + " "
                + theConnection.getResponseMessage());

        ByteArrayOutputStream theResult = new ByteArrayOutputStream();

        byte[] theData = new byte[8192];
        int length = theServerResponse.read(theData);
        while (length > 0) {

            theResult.write(theData, 0, length);
            length = theServerResponse.read(theData);
        }

        theData = theResult.toByteArray();

        StringBuffer theBuffer = new StringBuffer();
        BufferedReader theReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(theData), "UTF-8"));
        while (theReader.ready()) {
            String theLine = theReader.readLine();
            theBuffer.append(theLine);
        }

        String theStrResult = theBuffer.toString();

        return new StringBufferInputStream(theStrResult);
    }

    protected Vector<WebProject> getProjectsByStatement(String aStatement) {

        Digester theDigester = new Digester();
        theDigester.setValidating(false);
        theDigester.addObjectCreate("result", ResultVector.class);
        theDigester.addSetProperties("result");
        theDigester.addObjectCreate("result/row", WebProject.class);
        theDigester.addCallMethod("result/row/id", "setId", 0, new Class[] { Long.class });
        theDigester.addCallMethod("result/row/date", "setDate", 0);
        theDigester.addCallMethod("result/row/projectnumber", "setProjectNumber", 0);
        theDigester.addCallMethod("result/row/workplace", "setWorkplace", 0);
        theDigester.addCallMethod("result/row/start", "setStart", 0);
        theDigester.addCallMethod("result/row/duration", "setDuration", 0);
        theDigester.addCallMethod("result/row/descriptionshort", "setDescriptionShort", 0);
        theDigester.addCallMethod("result/row/descriptionlong", "setDescriptionLong", 0);
        theDigester.addCallMethod("result/row/originid", "setOriginId", 0, new Class[] { Long.class });
        theDigester.addCallMethod("result/row/notified", "setNotifiedForNewsletter", 0, new Class[] { boolean.class });
        theDigester.addSetNext("result/row", "add");

        try {
            ResultVector theResult = (ResultVector) theDigester.parse(executeStatement(aStatement));
            String theError = theResult.getError();

            LOGGER.logDebug("Server returned error string " + theError + " for statement " + theResult.getStatement());
            if ((theError != null) && (!"".equals(theError))) {
                LOGGER.logError("Error executing statement " + theError + " : " + theError);

                throw new RuntimeException("Error executing statement " + aStatement + " : " + theError);
            }

            LOGGER.logDebug("Server returned " + theResult.size() + " objects");

            return theResult;

        } catch (Exception e) {

            LOGGER.logDebug("Error processing server response", e);

            throw new RuntimeException(e);
        }
    }

    protected void executeWithoutResult(String aStatement, HashMap aParams) {

        Digester theDigester = new Digester();
        theDigester.setValidating(false);
        theDigester.addObjectCreate("result", ResultVector.class);
        theDigester.addSetProperties("result");

        try {
            ResultVector theResult = (ResultVector) theDigester.parse(executeStatement(aStatement, aParams));
            String theError = theResult.getError();

            if ((theError != null) && (!"".equals(theError))) {
                LOGGER.logError("Error executing statement " + theError + " : " + theError);

                throw new RuntimeException("Error executing statement " + theResult.getStatement() + " : " + theError);
            }

            LOGGER.logDebug("Server returned error string " + theResult.getError() + " for statement "
                    + theResult.getStatement());

            if (theResult.size() != 0) {

                String theMessage = "Server returned " + theResult.size() + " objects, expected none!";

                LOGGER.logError(theMessage);

                throw new RuntimeException(theMessage);
            }

        } catch (Exception e) {

            LOGGER.logDebug("Error processing server response", e);

            throw new RuntimeException(e);
        }

    }

    protected HashMap getObjectProperties(Object aObject) {
        HashMap theResult = new HashMap();

        PropertyDescriptor[] theDescriptors = PropertyUtils.getPropertyDescriptors(aObject);
        for (PropertyDescriptor theDescriptor : theDescriptors) {
            if ((theDescriptor.getReadMethod() != null) && (theDescriptor.getWriteMethod() != null)) {
                LOGGER.logDebug("Processing property " + theDescriptor.getName());

                try {
                    theResult.put(theDescriptor.getName(), PropertyUtils.getProperty(aObject, theDescriptor.getName()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return theResult;
    }

    public void delete(WebProject aProject) {

        if (aProject.getId() != null) {

            String theStatement = "DELETE FROM webproject WHERE id= ::id::";

            executeWithoutResult(theStatement, getObjectProperties(aProject));
        }
    }

    public WebProject findByOriginId(Long aId) {

        String theStatement = "SELECT id,date,projectNumber,workplace,start,duration,descriptionShort,descriptionLong,originId,ASCII(notifiedForNewsletter) as notified FROM webproject WHERE originId="
                + aId;
        Vector<WebProject> theResult = getProjectsByStatement(theStatement);
        if (theResult.size() == 0) {
            return null;
        }
        if (theResult.size() == 1) {
            return theResult.get(0);
        }

        String theMessage = "Server returned " + theResult.size() + " objects, but extected exact one!";
        LOGGER.logDebug(theMessage);

        throw new RuntimeException(theMessage);
    }

    public Collection<NewsletterMail> getConfirmedMails() {
        String theStatement = "SELECT mail,registeredTimestamp,confirmedTimestamp,errorcounter,ASCII(confirmed) as confirmed FROM newslettermail WHERE confirmedTimestamp IS NOT NULL";

        Digester theDigester = new Digester();
        theDigester.setValidating(false);
        theDigester.addObjectCreate("result", Vector.class);
        theDigester.addObjectCreate("result/row", NewsletterMail.class);
        theDigester.addCallMethod("result/row/mail", "setMail", 0);
        theDigester.addCallMethod("result/row/errorcounter", "setErrorcounter", 0, new Class[] { int.class });
        theDigester.addCallMethod("result/row/confirmed", "setConfirmed", 0);
        theDigester.addSetNext("result/row", "add");

        try {
            Vector theResult = (Vector) theDigester.parse(executeStatement(theStatement));

            LOGGER.logDebug("Server returned " + theResult.size() + " objects");

            return theResult;

        } catch (Exception e) {

            LOGGER.logError("Error processing server response", e);

            throw new RuntimeException(e);
        }
    }

    public Collection<WebProject> getCurrentProjects() {

        String theStatement = "SELECT id,date,projectNumber,workplace,start,duration,descriptionShort,descriptionLong,originId,notifiedForNewsletter as notified FROM webproject ORDER BY projectNumber";

        return getProjectsByStatement(theStatement);
    }

    public void saveOrUpdate(WebProject aProject) {

        if (aProject.getId() == null) {

            aProject.setId(aProject.getOriginId());

            // Insert
            String theStatement = "INSERT INTO webproject (id,date,projectNumber,workplace,start,duration,descriptionShort,descriptionLong,originId) VALUES (::id::,::date::,::projectNumber::,::workplace::,::start::,::duration::,::descriptionShort::,::descriptionLong::,::originId::)";

            HashMap theProperties = getObjectProperties(aProject);

            executeWithoutResult(theStatement, theProperties);

        } else {

            // Update
            String theStatement = "UPDATE webproject SET date = ::date::,projectNumber = ::projectNumber::,workplace = ::workplace::,start = ::start::,duration = ::duration::,descriptionShort = ::descriptionShort::,descriptionLong = ::descriptionLong::,originId = ::originId::,notifiedForNewsletter=::notifiedForNewsletter:: WHERE id = ::id::";

            HashMap theProperties = getObjectProperties(aProject);

            executeWithoutResult(theStatement, theProperties);
        }

    }

    public void saveOrUpdate(NewsletterMail aMail) {

        if (aMail.getId() != null) {

            String theStatement = "UPDATE newslettermail SET errorcounter = ::errorcounter:: WHERE mail = ::mail::";

            executeWithoutResult(theStatement, getObjectProperties(aMail));

        } else {

            String theMessage = "Can only be used with persistent mails";

            LOGGER.logError(theMessage);

            throw new RuntimeException(theMessage);

        }

    }
}