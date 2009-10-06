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
package de.mogwai.common.web;

import javax.faces.context.FacesContext;

public class Configuration {

    private static final String DISABLE_GUID_CHECK = "mogwaicomponents.disable_guid_check";

    public static boolean isGUIDCheckDisabled() {
        FacesContext theContext = FacesContext.getCurrentInstance();

        Object theValue = theContext.getExternalContext().getInitParameter(DISABLE_GUID_CHECK);

        return "true".equals(theValue);
    }
}