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
package de.mogwai.common.exception;

/**
 * Exceptionklasse für fachliche Fehler. Die Meldungen werden dem Benutzer in
 * lokalisierter Form präsentiert. Die Lokalisierung wird in der
 * Präsentationsschicht vorgenommen.
 * 
 * Für Exeptions, die Systemfehler behandeln ist eine Unterklasse von
 * SystemException zu verwenden!
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:19 $
 */
public class UserMessageException extends Exception {

    /** Schlüssel der Message Resource. */
    private String messageKey;

    /** Ersatzwerte für die Message Resource. */
    private String[] replacementValues;

    /**
     * @param messageKey
     *                Schlüssel der Message Resource.
     */
    public UserMessageException(String messageKey) {
        this.messageKey = messageKey;
    }

    /**
     * @param messageKey
     *                Schlüssel der Message Resource.
     * @param replacementValues
     *                Ersatzwerte für Platzhalter der Message Resource.
     */
    public UserMessageException(String messageKey, String... replacementValues) {

        this.messageKey = messageKey;
        this.replacementValues = replacementValues;
    }

    /**
     * Zugriff auf den Schlüssel der Message Resource.
     * 
     * @return Schlüssel der Message Resource.
     */
    public String getMessageKey() {
        return this.messageKey;
    }

    /**
     * Zugriff auf die Ersatzwerte für die Message Resource.
     * 
     * @return Ersatzwerte für die Message Resource.
     */
    public String[] getReplacementValues() {
        return this.replacementValues;
    }

}