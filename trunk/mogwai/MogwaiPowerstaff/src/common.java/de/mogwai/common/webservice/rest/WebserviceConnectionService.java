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
package de.mogwai.common.webservice.rest;

import de.mogwai.common.business.service.Service;
import de.mogwai.common.net.ProxyConfigurationProvider;
import de.mogwai.common.utils.LoginCredentialsProvider;

/**
 * Schnittstelle zu einem Rest - Webservice.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:05:01 $
 */
public interface WebserviceConnectionService extends Service {

    /**
     * Perform a request.
     * 
     * @param aRequest
     *                the request
     * @return the response
     * @throws WebserviceException
     *                 will be thrown in case of an error
     */
    WebserviceResponse performRequest(WebserviceRequest aRequest) throws WebserviceException;

    /**
     * Perform a request.
     * 
     * @param aUrl
     *                the URL to sent the request to
     * @param aRequest
     *                the request
     * @return the response
     * @throws WebserviceException
     *                 will be thrown in case of an error
     */
    WebserviceResponse performRequest(String aUrl, WebserviceRequest aRequest) throws WebserviceException;

    /**
     * Perform a request.
     * 
     * @param aConfigurationProvider
     *                the Proxy - Configuration to be used
     * @param aUrl
     *                the URL to sent the request to
     * @param aRequest
     *                the request
     * @return the response
     * @throws WebserviceException
     *                 will be thrown in case of an error
     */
    WebserviceResponse performRequest(LoginCredentialsProvider aLoginCredentialsProvider,
            ProxyConfigurationProvider aConfigurationProvider, String aUrl, WebserviceRequest aRequest)
            throws WebserviceException;

}
