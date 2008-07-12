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
package de.mogwai.common.net;

/**
 * Klasse zum Beschreiben der aktuellen Proxy - Konfiguration.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:52 $
 */
public class ProxyConfiguration implements ProxyConfigurationProvider {

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
     * Gibt den Wert des Attributs <code>httpProxyHost</code> zur�ck.
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
     *                Wert f�r das Attribut httpProxyHost.
     */
    public void setHttpProxyHost(String httpProxyHost) {
        this.httpProxyHost = httpProxyHost;
    }

    /**
     * Gibt den Wert des Attributs <code>httpProxyPassword</code> zur�ck.
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
     *                Wert f�r das Attribut httpProxyPassword.
     */
    public void setHttpProxyPassword(String httpProxyPassword) {
        this.httpProxyPassword = httpProxyPassword;
    }

    /**
     * Gibt den Wert des Attributs <code>httpProxyPort</code> zur�ck.
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
     *                Wert f�r das Attribut httpProxyPort.
     */
    public void setHttpProxyPort(int httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }

    /**
     * Gibt den Wert des Attributs <code>httpProxyUser</code> zur�ck.
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
     *                Wert f�r das Attribut httpProxyUser.
     */
    public void setHttpProxyUser(String httpProxyUser) {
        this.httpProxyUser = httpProxyUser;
    }

    /**
     * Gibt den Wert des Attributs <code>httpsProxyHost</code> zur�ck.
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
     *                Wert f�r das Attribut httpsProxyHost.
     */
    public void setHttpsProxyHost(String httpsProxyHost) {
        this.httpsProxyHost = httpsProxyHost;
    }

    /**
     * Gibt den Wert des Attributs <code>httpsProxyPassword</code> zur�ck.
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
     *                Wert f�r das Attribut httpsProxyPassword.
     */
    public void setHttpsProxyPassword(String httpsProxyPassword) {
        this.httpsProxyPassword = httpsProxyPassword;
    }

    /**
     * Gibt den Wert des Attributs <code>httpsProxyPort</code> zur�ck.
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
     *                Wert f�r das Attribut httpsProxyPort.
     */
    public void setHttpsProxyPort(int httpsProxyPort) {
        this.httpsProxyPort = httpsProxyPort;
    }

    /**
     * Gibt den Wert des Attributs <code>httpsProxyUser</code> zur�ck.
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
     *                Wert f�r das Attribut httpsProxyUser.
     */
    public void setHttpsProxyUser(String httpsProxyUser) {
        this.httpsProxyUser = httpsProxyUser;
    }

    /**
     * Gibt den Wert des Attributs <code>usehttpProxy</code> zur�ck.
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
     *                Wert f�r das Attribut usehttpProxy.
     */
    public void setUsehttpProxy(boolean usehttpProxy) {
        this.usehttpProxy = usehttpProxy;
    }

    /**
     * Gibt den Wert des Attributs <code>usehttpsProxy</code> zur�ck.
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
     *                Wert f�r das Attribut usehttpsProxy.
     */
    public void setUsehttpsProxy(boolean usehttpsProxy) {
        this.usehttpsProxy = usehttpsProxy;
    }

}
