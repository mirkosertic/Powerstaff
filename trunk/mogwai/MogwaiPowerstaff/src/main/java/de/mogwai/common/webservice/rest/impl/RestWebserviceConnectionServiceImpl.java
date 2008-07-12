/**
 * Copyright 2002 - 2007 the Mogwai Project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.mogwai.common.webservice.rest.impl;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.protocol.Protocol;

import de.mogwai.common.business.service.impl.LogableService;
import de.mogwai.common.net.ProxyConfigurationProvider;
import de.mogwai.common.utils.LoginCredentialsProvider;
import de.mogwai.common.webservice.rest.MultipartWebserviceRequest;
import de.mogwai.common.webservice.rest.WebserviceConnectionService;
import de.mogwai.common.webservice.rest.WebserviceException;
import de.mogwai.common.webservice.rest.WebserviceRequest;
import de.mogwai.common.webservice.rest.WebserviceResponse;

/**
 * Implementation.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:10:23 $
 */
public class RestWebserviceConnectionServiceImpl extends LogableService implements WebserviceConnectionService,
        ProxyConfigurationProvider, LoginCredentialsProvider {

    private String url;

    private String user;

    private String password;

    private String defaultEncoding = "UTF-8";

    private String httpProxyHost;

    private int httpProxyPort;

    private String httpProxyUser;

    private String httpProxyPassword;

    private boolean usehttpProxy;

    private String httpsProxyHost;

    private int httpsProxyPort;

    private String httpsProxyUser;

    private String httpsProxyPassword;

    private boolean usehttpsProxy;

    /**
     * Gibt den Wert des Attributs <code>httpProxyHost</code> zurück.
     * 
     * @return Wert des Attributs httpProxyHost.
     */
    public String getHttpProxyHost() {
        return httpProxyHost;
    }

    /**
     * Setzt den Wert des Attributs <code>httpProxyHost</code>.
     * 
     * @param httpProxyHost
     *                Wert für das Attribut httpProxyHost.
     */
    public void setHttpProxyHost(String httpProxyHost) {
        this.httpProxyHost = httpProxyHost;
    }

    /**
     * Gibt den Wert des Attributs <code>httpProxyPassword</code> zurück.
     * 
     * @return Wert des Attributs httpProxyPassword.
     */
    public String getHttpProxyPassword() {
        return httpProxyPassword;
    }

    /**
     * Setzt den Wert des Attributs <code>httpProxyPassword</code>.
     * 
     * @param httpProxyPassword
     *                Wert für das Attribut httpProxyPassword.
     */
    public void setHttpProxyPassword(String httpProxyPassword) {
        this.httpProxyPassword = httpProxyPassword;
    }

    /**
     * Gibt den Wert des Attributs <code>httpProxyUser</code> zurück.
     * 
     * @return Wert des Attributs httpProxyUser.
     */
    public String getHttpProxyUser() {
        return httpProxyUser;
    }

    /**
     * Setzt den Wert des Attributs <code>httpProxyUser</code>.
     * 
     * @param httpProxyUser
     *                Wert für das Attribut httpProxyUser.
     */
    public void setHttpProxyUser(String httpProxyUser) {
        this.httpProxyUser = httpProxyUser;
    }

    /**
     * Gibt den Wert des Attributs <code>httpsProxyHost</code> zurück.
     * 
     * @return Wert des Attributs httpsProxyHost.
     */
    public String getHttpsProxyHost() {
        return httpsProxyHost;
    }

    /**
     * Setzt den Wert des Attributs <code>httpsProxyHost</code>.
     * 
     * @param httpsProxyHost
     *                Wert für das Attribut httpsProxyHost.
     */
    public void setHttpsProxyHost(String httpsProxyHost) {
        this.httpsProxyHost = httpsProxyHost;
    }

    /**
     * Gibt den Wert des Attributs <code>httpsProxyPassword</code> zurück.
     * 
     * @return Wert des Attributs httpsProxyPassword.
     */
    public String getHttpsProxyPassword() {
        return httpsProxyPassword;
    }

    /**
     * Setzt den Wert des Attributs <code>httpsProxyPassword</code>.
     * 
     * @param httpsProxyPassword
     *                Wert für das Attribut httpsProxyPassword.
     */
    public void setHttpsProxyPassword(String httpsProxyPassword) {
        this.httpsProxyPassword = httpsProxyPassword;
    }

    /**
     * Gibt den Wert des Attributs <code>httpProxyPort</code> zurück.
     * 
     * @return Wert des Attributs httpProxyPort.
     */
    public int getHttpProxyPort() {
        return httpProxyPort;
    }

    /**
     * Setzt den Wert des Attributs <code>httpProxyPort</code>.
     * 
     * @param httpProxyPort
     *                Wert für das Attribut httpProxyPort.
     */
    public void setHttpProxyPort(int httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }

    /**
     * Gibt den Wert des Attributs <code>httpsProxyPort</code> zurück.
     * 
     * @return Wert des Attributs httpsProxyPort.
     */
    public int getHttpsProxyPort() {
        return httpsProxyPort;
    }

    /**
     * Setzt den Wert des Attributs <code>httpsProxyPort</code>.
     * 
     * @param httpsProxyPort
     *                Wert für das Attribut httpsProxyPort.
     */
    public void setHttpsProxyPort(int httpsProxyPort) {
        this.httpsProxyPort = httpsProxyPort;
    }

    /**
     * Gibt den Wert des Attributs <code>httpsProxyUser</code> zurück.
     * 
     * @return Wert des Attributs httpsProxyUser.
     */
    public String getHttpsProxyUser() {
        return httpsProxyUser;
    }

    /**
     * Setzt den Wert des Attributs <code>httpsProxyUser</code>.
     * 
     * @param httpsProxyUser
     *                Wert für das Attribut httpsProxyUser.
     */
    public void setHttpsProxyUser(String httpsProxyUser) {
        this.httpsProxyUser = httpsProxyUser;
    }

    /**
     * Gibt den Wert des Attributs <code>password</code> zurück.
     * 
     * @return Wert des Attributs password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setzt den Wert des Attributs <code>password</code>.
     * 
     * @param password
     *                Wert für das Attribut password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gibt den Wert des Attributs <code>url</code> zurück.
     * 
     * @return Wert des Attributs url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setzt den Wert des Attributs <code>url</code>.
     * 
     * @param url
     *                Wert für das Attribut url.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gibt den Wert des Attributs <code>usehttpProxy</code> zurück.
     * 
     * @return Wert des Attributs usehttpProxy.
     */
    public boolean isUsehttpProxy() {
        return usehttpProxy;
    }

    /**
     * Setzt den Wert des Attributs <code>usehttpProxy</code>.
     * 
     * @param usehttpProxy
     *                Wert für das Attribut usehttpProxy.
     */
    public void setUsehttpProxy(boolean usehttpProxy) {
        this.usehttpProxy = usehttpProxy;
    }

    /**
     * Gibt den Wert des Attributs <code>usehttpsProxy</code> zurück.
     * 
     * @return Wert des Attributs usehttpsProxy.
     */
    public boolean isUsehttpsProxy() {
        return usehttpsProxy;
    }

    /**
     * Setzt den Wert des Attributs <code>usehttpsProxy</code>.
     * 
     * @param usehttpsProxy
     *                Wert für das Attribut usehttpsProxy.
     */
    public void setUsehttpsProxy(boolean usehttpsProxy) {
        this.usehttpsProxy = usehttpsProxy;
    }

    /**
     * Gibt den Wert des Attributs <code>user</code> zurück.
     * 
     * @return Wert des Attributs user.
     */
    public String getUser() {
        return user;
    }

    /**
     * Setzt den Wert des Attributs <code>user</code>.
     * 
     * @param user
     *                Wert für das Attribut user.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Gibt den Wert des Attributs <code>defaultEncoding</code> zurück.
     * 
     * @return Wert des Attributs defaultEncoding.
     */
    public String getDefaultEncoding() {
        return defaultEncoding;
    }

    /**
     * Setzt den Wert des Attributs <code>defaultEncoding</code>.
     * 
     * @param defaultEncoding
     *                Wert für das Attribut defaultEncoding.
     */
    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    /**
     * @see de.mogwai.common.webservice.rest.WebserviceConnectionService#performRequest(de.mogwai.common.webservice.rest.WebserviceRequest)
     */
    public WebserviceResponse performRequest(LoginCredentialsProvider aCredentialsProvider,
            ProxyConfigurationProvider aProvider, String aURL, WebserviceRequest aRequest) throws WebserviceException {

        logger.logInfo("Trying to request url " + aURL);

        logger.logInfo("Initializing HttpClient");

        HttpClient theHTTPClient = new HttpClient();

        boolean usesAuthentication = false;

        String theUser = aCredentialsProvider.getUser();
        String thePassword = aCredentialsProvider.getPassword();

        if ((theUser != null) && (!"".equals(theUser))) {

            // Authentifizierung erforderlich
            logger.logInfo("Configuring authentication for user " + theUser);

            theHTTPClient.getState().setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(theUser, thePassword));

            usesAuthentication = true;
        }

        if (aURL.startsWith("https")) {

            Protocol.registerProtocol("https", new Protocol("https", new WeakSSLSocketFactory(), 443));

            if (aProvider.isUsehttpsProxy()) {

                String theProxyHost = aProvider.getHttpsProxyHost();
                int theProxyPort = aProvider.getHttpsProxyPort();
                String theProxyUser = aProvider.getHttpsProxyUser();
                String theProxyPassword = aProvider.getHttpsProxyPassword();

                logger.logInfo("Using https proxy " + theProxyHost + ":" + theProxyPort);

                theHTTPClient.getHostConfiguration().setProxy(theProxyHost, theProxyPort);

                if ((theProxyUser != null) && (!"".equals(theProxyUser))) {

                    logger.logInfo("Authenticating at proxy with user " + theProxyUser);

                    theHTTPClient.getState().setProxyCredentials(AuthScope.ANY,
                            new UsernamePasswordCredentials(theProxyUser, theProxyPassword));
                }
            }

        } else {

            if (aProvider.isUsehttpProxy()) {

                String theProxyHost = aProvider.getHttpProxyHost();
                int theProxyPort = aProvider.getHttpProxyPort();
                String theProxyUser = aProvider.getHttpProxyUser();
                String theProxyPassword = aProvider.getHttpProxyPassword();

                logger.logInfo("Using http proxy " + theProxyHost + ":" + theProxyPort);

                theHTTPClient.getHostConfiguration().setProxy(theProxyHost, theProxyPort);

                if ((theProxyUser != null) && (!"".equals(theProxyUser))) {

                    logger.logInfo("Authenticating at proxy with user " + theProxyUser);

                    theHTTPClient.getState().setProxyCredentials(AuthScope.ANY,
                            new UsernamePasswordCredentials(theProxyUser, theProxyPassword));
                }

            }
        }

        String theUrl = aURL;
        PostMethod theMethod = null;

        try {

            while (true) {
                theMethod = new PostMethod(theUrl);
                theMethod.setDoAuthentication(usesAuthentication);

                logger.logInfo("Sending data to client");

                if (aRequest instanceof MultipartWebserviceRequest) {

                    MultipartWebserviceRequest theMultipartRequest = (MultipartWebserviceRequest) aRequest;

                    Part[] theParts = new Part[theMultipartRequest.getParameterCount()];
                    int i = 0;
                    for (Iterator<String> theParamIterator = theMultipartRequest.keyIterator(); theParamIterator
                            .hasNext();) {
                        String theParamName = theParamIterator.next();
                        Object theValue = theMultipartRequest.getParameter(theParamName);

                        if (theValue instanceof File) {

                            logger.logDebug("Sending parameter " + theParamName + " as file");

                            theParts[i] = new FilePart(theParamName, (File) theValue);
                        }

                        if (theValue instanceof String) {

                            logger.logDebug("Sending parameter " + theParamName + " as text");

                            theParts[i] = new StringPart(theParamName, (String) theValue);
                        }

                        i++;
                    }

                    RequestEntity theRequestEntity = new MultipartRequestEntity(theParts, theMethod.getParams());
                    theMethod.setRequestEntity(theRequestEntity);

                } else {

                    String theData = aRequest.getAsString();

                    logger.logDebug("Request data is " + theData);

                    RequestEntity theRequestEntity = new StringRequestEntity(theData);
                    theMethod.setRequestEntity(theRequestEntity);
                }

                int theResult = theHTTPClient.executeMethod(theMethod);

                logger.logInfo("Response code is " + theResult + " " + HttpStatus.getStatusText(theResult));

                switch (theResult) {

                case HttpStatus.SC_OK: 

                    String theResponseBody = theMethod.getResponseBodyAsString();

                    logger.logDebug("Server response is" + theResponseBody);

                    return new WebserviceResponse(theResponseBody);

                default: 
                    throw new WebserviceException("Error sending request : " + theResult + " "
                            + HttpStatus.getStatusText(theResult), null);
                }

            }
        } catch (WebserviceException e1) {
            throw e1;
        } catch (Exception e) {
            throw new WebserviceException("Fehler beim Senden", e);
        } finally {
            theMethod.releaseConnection();
        }
    }

    public WebserviceResponse performRequest(WebserviceRequest aRequest) throws WebserviceException {
        return this.performRequest(this, this, url, aRequest);
    }

    public WebserviceResponse performRequest(String aUrl, WebserviceRequest aRequest) throws WebserviceException {
        return this.performRequest(this, this, aUrl, aRequest);
    }
}
