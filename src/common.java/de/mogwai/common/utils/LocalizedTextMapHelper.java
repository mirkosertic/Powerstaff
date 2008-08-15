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
package de.mogwai.common.utils;

import java.util.Locale;
import java.util.Map;

/**
 * Hilfsklasse für die lokalisierte Speicherung von Texten.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:05:32 $
 */
public final class LocalizedTextMapHelper {

    private LocalizedTextMapHelper() {
    }

    /**
     * Ermittlung des Textes für ein Locale.
     * 
     * @param aMap
     *                die Map
     * @param aLocale
     *                das Locale
     * @return der Text oder null wenn nichts gefunden wurde
     */
    public static String getForLocale(Map<String, String> aMap, Locale aLocale) {
        String theKey = aLocale.getLanguage();
        return aMap.get(theKey);
    }

    /**
     * Setzen des Textes für ein Locale.
     * 
     * @param aMap
     *                die Map
     * @param aLocale
     *                das Locale
     * @param aText
     *                der Text
     */
    public static void setForLocale(Map<String, String> aMap, Locale aLocale, String aText) {
        String theKey = aLocale.getLanguage();
        aMap.put(theKey, aText);
    }
}
