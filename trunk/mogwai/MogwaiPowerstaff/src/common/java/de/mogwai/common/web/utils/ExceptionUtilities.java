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
package de.mogwai.common.web.utils;

import javax.servlet.ServletException;

/**
 * Utility - Klasse zum Umgang mit Exceptions.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:16:04 $
 */
public final class ExceptionUtilities {

    private ExceptionUtilities() {
    }

    /**
     * Ermittlung der Cause einer Exception.
     * 
     * @param aException
     *                die Exception
     * @return die Cause
     */
    public static Exception getCause(Exception aException) {

        if (aException instanceof ServletException) {

            // A servlet excaption has an root cause
            Exception theCause = (Exception) ((ServletException) aException).getRootCause();

            // Which might also be a ServletException
            if (theCause instanceof ServletException) {
                ServletException theException = (ServletException) theCause;
                if (theException.getRootCause() != null) {
                    return getCause((Exception) theException.getRootCause());
                }
                return theException;
            }

            if (theCause != null) {
                if (theCause.getCause() != null) {
                    return getCause((Exception) theCause.getCause());
                } else {
                    return theCause;
                }
            } else {
                return aException;
            }

        }

        if ((aException.getCause() == null) || (aException.getCause() == aException)) {
            return aException;
        }

        return (Exception) aException.getCause();
    }
}
