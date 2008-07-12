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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Hilfsklasse für die Berechnung von Datumsfeldern.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:10:13 $
 */
public final class DateUtils {

    private DateUtils() {
    }

    /**
     * Ermittlung des aktuellen Systemdatums.
     * 
     * @return das aktuelle Systemdatum
     */
    public static Date now() {
        return new java.sql.Date(System.currentTimeMillis());
    }

    /**
     * Eine bestimmte Anzahl an Jahren auf ein Datum aufaddieren.
     * 
     * @param aLocale
     *                das zu werdendende Locale
     * @param aDate
     *                das Datum
     * @param aAmount
     *                die Anzahl
     * @return das neu berechnete Datum
     */
    public static Date addYears(Locale aLocale, Date aDate, int aAmount) {
        Calendar theCalendar = Calendar.getInstance(aLocale);
        theCalendar.setTime(aDate);
        theCalendar.add(Calendar.YEAR, aAmount);

        return theCalendar.getTime();
    }

    /**
     * Eine bestimmte Anzahl an Tagen auf ein Datum aufaddieren.
     * 
     * @param aLocale
     *                das zu werdendende Locale
     * @param aDate
     *                das Datum
     * @param aAmount
     *                die Anzahl
     * @return das neu berechnete Datum
     */
    public static Date addDays(Locale aLocale, Date aDate, int aAmount) {
        Calendar theCalendar = Calendar.getInstance(aLocale);
        theCalendar.setTime(aDate);
        theCalendar.add(Calendar.DAY_OF_YEAR, aAmount);

        return theCalendar.getTime();
    }

    /**
     * Eine bestimmte Anzahl an Monaten auf ein Datum aufaddieren.
     * 
     * @param aLocale
     *                das zu werdendende Locale
     * @param aDate
     *                das Datum
     * @param aAmount
     *                die Anzahl
     * @return das neu berechnete Datum
     */
    public static Date addMonths(Locale aLocale, Date aDate, int aAmount) {
        Calendar theCalendar = Calendar.getInstance(aLocale);
        theCalendar.setTime(aDate);
        theCalendar.add(Calendar.MONTH, aAmount);

        return theCalendar.getTime();
    }
}
