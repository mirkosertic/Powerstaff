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
package de.mogwai.common.usercontext;

import java.util.HashMap;
import java.util.Locale;

/**
 * Informationen des aktuellen Benutzercontextes.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:12:09 $
 */
public class UserContext {

    public static final String SESSION_ID = "session_id";

    public static final String LOCALE = "locale";

    private Authenticatable authenticatable;

    private HashMap<Object, Object> sessionValues = new HashMap<Object, Object>();

    private boolean logout;

    /**
     * Initialisierung des Contextes mit einem Benutzer.
     * 
     * @param aAuthentication
     *                der angemeldete Benutzer
     */
    UserContext(Authenticatable aAuthentication) {
        authenticatable = aAuthentication;

        sessionValues.put(SESSION_ID, authenticatable.getUserId() + "@" + (Math.random() * 10000) + "_"
                + System.currentTimeMillis());
    }

    /**
     * Getter für den aktuellen Benutzer.
     * 
     * @return der aktuelle Benutzer
     */
    public Authenticatable getAuthenticatable() {
        return authenticatable;

    }

    /**
     * Getter für einen Wert aus der aktuellen Session.
     * 
     * @param aKey
     *                der Schlüssel
     * @return der Wert
     */
    public Object getSessionValue(Object aKey) {
        return sessionValues.get(aKey);
    }

    /**
     * Setzen eines Wertes in der aktuellen Session.
     * 
     * @param aKey
     *                der Schlüssel
     * @param aValue
     *                der Wert
     */
    public void setSessionValue(Object aKey, Object aValue) {
        sessionValues.put(aKey, aValue);
    }

    /**
     * Getter für die aktuelle Session ID.
     * 
     * @return die Session ID
     */
    public String getSessionId() {
        return (String) sessionValues.get(SESSION_ID);
    }

    /**
     * Getter für die aktuelle Locale.
     * 
     * @return die Locale.
     */
    public Locale getLocale() {
        return (Locale) sessionValues.get(LOCALE);
    }

    /**
     * Gibt den Wert des Attributs <code>logout</code> zurück.
     * 
     * @return Wert des Attributs logout.
     */
    public boolean isLogout() {
        return logout;
    }

    /**
     * Setzt den Wert des Attributs <code>logout</code>.
     * 
     * @param logout
     *                Wert für das Attribut logout.
     */
    public void setLogout(boolean logout) {
        this.logout = logout;
    }

    /**
     * @return the sessionValues
     */
    public HashMap<Object, Object> getSessionValues() {
        return sessionValues;
    }
}
