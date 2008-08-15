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
package de.mogwai.common.business.service.impl;

import de.mogwai.common.business.service.ProxyConfigurerService;

/**
 * Service zum Konfigurieren von Systemeinstellungen wie z.B. Proxy -
 * Konfiguration.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:09:58 $
 */
public class ProxyConfigurerServiceImpl extends LogableService implements ProxyConfigurerService {

    private String httpProxySet = "false";

    private String httpProxyHost = "";

    private String httpProxyPort = "";

    private String httpsProxySet = "false";

    private String httpsProxyHost = "";

    private String httpsProxyPort = "";

    private boolean debugSSL = false;

    public String getDebugSSL() {
        return "" + debugSSL;
    }

    public void setDebugSSL(String debugSSL) {
        this.debugSSL = Boolean.getBoolean(debugSSL);
    }

    public String getHttpProxyHost() {
        return httpProxyHost;
    }

    public void setHttpProxyHost(String httpProxyHost) {
        this.httpProxyHost = httpProxyHost;
    }

    public String getHttpProxyPort() {
        return httpProxyPort;
    }

    public void setHttpProxyPort(String httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }

    public String getHttpProxySet() {
        return httpProxySet;
    }

    public void setHttpProxySet(String httpProxySet) {
        this.httpProxySet = httpProxySet;
    }

    public String getHttpsProxyHost() {
        return httpsProxyHost;
    }

    public void setHttpsProxyHost(String httpsProxyHost) {
        this.httpsProxyHost = httpsProxyHost;
    }

    public String getHttpsProxyPort() {
        return httpsProxyPort;
    }

    public void setHttpsProxyPort(String httpsProxyPort) {
        this.httpsProxyPort = httpsProxyPort;
    }

    public String getHttpsProxySet() {
        return httpsProxySet;
    }

    public void setHttpsProxySet(String httpsProxySet) {
        this.httpsProxySet = httpsProxySet;
    }

    public void initialize() {

        System.setProperty("https.proxySet", httpsProxySet);
        System.setProperty("https.proxyHost", httpsProxyHost);
        System.setProperty("https.proxyPort", httpsProxyPort);
        System.setProperty("http.proxySet", httpProxySet);
        System.setProperty("http.proxyHost", httpProxyHost);
        System.setProperty("http.proxyPort", httpProxyPort);

        if (debugSSL) {

            logger.logDebug("SSL debugging enabled");
            System.setProperty("javax.net.debug", "ssl,handshake,data,trustmanager ");
        }

        logger.logDebug("https.proxySet=" + System.getProperty("https.proxySet"));
        logger.logDebug("https.proxyHost=" + System.getProperty("https.proxyHost"));
        logger.logDebug("https.proxyPort=" + System.getProperty("https.proxyPort"));
        logger.logDebug("http.proxySet=" + System.getProperty("http.proxySet"));
        logger.logDebug("http.proxyHost=" + System.getProperty("http.proxyHost"));
        logger.logDebug("http.proxyPort=" + System.getProperty("http.proxyPort"));
    }
}
