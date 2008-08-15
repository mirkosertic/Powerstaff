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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Adapter - Klasse für den einfacheren Umgang mit lokalisierten Texten.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:05:31 $
 */
public class LocalizedText implements Serializable {

    private Map<String, String> map = new HashMap<String, String>();

    public String getTextD() {
        return LocalizedTextMapHelper.getForLocale(map, Locale.GERMAN);
    }

    public void setTextD(String aText) {
        LocalizedTextMapHelper.setForLocale(map, Locale.GERMAN, aText);
    }

    public String getTextI() {
        return LocalizedTextMapHelper.getForLocale(map, Locale.ITALIAN);
    }

    public void setTextI(String aText) {
        LocalizedTextMapHelper.setForLocale(map, Locale.ITALIAN, aText);
    }

    public String getTextF() {
        return LocalizedTextMapHelper.getForLocale(map, Locale.FRENCH);
    }

    public void setTextF(String aText) {
        LocalizedTextMapHelper.setForLocale(map, Locale.FRENCH, aText);
    }

    public String getTextE() {
        return LocalizedTextMapHelper.getForLocale(map, Locale.ENGLISH);
    }

    public void setTextE(String aText) {
        LocalizedTextMapHelper.setForLocale(map, Locale.ENGLISH, aText);
    }

    /**
     * Kopieren aller Daten in einen anderen Locale - Adapter.
     * 
     * @param aLocaleAdapter
     */
    public void copyTo(LocalizedText aLocaleAdapter) {
        for (String theKey : map.keySet()) {
            String theValue = map.get(theKey);
            aLocaleAdapter.map.put(theKey, theValue);
        }
    }

    /**
     * Ermittlung des Textes für ein Locale.
     * 
     * @param aLocale
     *                das Locale
     * @return der lokalisierte Text
     */
    public String getForLocale(Locale aLocale) {
        return LocalizedTextMapHelper.getForLocale(map, aLocale);
    }
}
