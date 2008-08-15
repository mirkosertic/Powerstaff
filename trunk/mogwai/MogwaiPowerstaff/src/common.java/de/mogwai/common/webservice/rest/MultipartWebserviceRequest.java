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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.mogwai.common.utils.XMLException;

/**
 * Implementierung eines Multipart - Webservicerequests.
 * 
 * Anders als ein normaler WebserviceRequest besteht dieser nicht aus einem
 * String ( meistens ein XML ), der zum Server geschickt wird, sondern aus
 * mehreren Name - Wert Paaren. Diese Name - Wert Paare werden als Multipart -
 * Post zum Server geschickt.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:05:00 $
 */
public class MultipartWebserviceRequest extends WebserviceRequest {

    private Map<String, Object> parameters = new HashMap<String, Object>();

    public void addParameter(String aKey, Object aValue) {
        parameters.put(aKey, aValue);
    }

    public int getParameterCount() {
        return parameters.keySet().size();
    }

    public Iterator<String> keyIterator() {
        return parameters.keySet().iterator();
    }

    public Object getParameter(String aKey) {
        return parameters.get(aKey);
    }

    @Override
    public String getAsString() throws XMLException {
        return null;
    }
}
