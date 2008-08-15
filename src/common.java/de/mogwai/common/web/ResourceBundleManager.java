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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 * 
 * Service-Klasse, die den Zugriff auf das Resource-Bundle zentralisiert.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:10:56 $
 */
public class ResourceBundleManager {
    private static final String BUNDLE_BASE_NAME = "messages";

    /**
     * Ueber diese Methode wird auf das Resource-Bundle zugeriffen werden.
     * 
     * @return das lokalisierte Resource-Bundle
     */
    public static ResourceBundle getBundle() {
        return getBundle(BUNDLE_BASE_NAME, FacesContext.getCurrentInstance().getViewRoot().getLocale());

    }

    /**
     * Ueber diese Methode wird auf das Resource-Bundle zugeriffen werden.
     * 
     * @param aLocale
     *                Das Locale
     * @return das Bundle
     */
    public static ResourceBundle getBundle(Locale aLocale) {
        return ResourceBundle.getBundle(BUNDLE_BASE_NAME, aLocale);
    }

    /**
     * Ueber diese Methode wird auf das Resource-Bundle zugeriffen werden.
     * 
     * @param aBundleName
     *                Der Name des ResourceBundles
     * @param aLocale
     *                das Locale
     * @return das Bundle
     */
    public static ResourceBundle getBundle(String aBundleName, Locale aLocale) {
        return ResourceBundle.getBundle(aBundleName, aLocale);
    }

    /**
     * Ermitteln eines ResourceValues anhand eines Keys.
     * 
     * Gleichzeitig wird ein MessageFormat an das Ergebnis angewendet und die
     * Platzhalter aus der Resource mit den Werten aus aValues ersetzt.
     * 
     * @param aKey
     *                der Resourcenschlüssel
     * @param aValues
     *                die Werte für die Platzhalter
     * @return das formatierte Ergebnis
     */
    public static String getFormattedResource(String aKey, Object[] aValues) {

        return getFormattedResource(FacesContext.getCurrentInstance().getViewRoot().getLocale(), aKey, aValues);
    }

    /**
     * Ermitteln eines ResourceValues anhand eines Keys.
     * 
     * Gleichzeitig wird ein MessageFormat an das Ergebnis angewendet und die
     * Platzhalter aus der Resource mit den Werten aus aValues ersetzt.
     * 
     * @param aLocale
     *                das Locale
     * @param aKey
     *                der Resourcenschlüssel
     * @param aValues
     *                die Werte für die Platzhalter
     * @return das formatierte Ergebnis
     */
    public static String getFormattedResource(Locale aLocale, String aKey, Object[] aValues) {

        ResourceBundle theBundle = getBundle(aLocale);

        String thePattern = theBundle.getString(aKey);
        MessageFormat theMessageFormat = new MessageFormat(thePattern, aLocale);

        return theMessageFormat.format(aValues);
    }

}
