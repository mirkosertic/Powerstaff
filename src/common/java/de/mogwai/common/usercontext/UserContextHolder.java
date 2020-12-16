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

/**
 * Zugriffsklasse auf den aktuellen UserContext.
 * 
 * Der UserContext dient zum Zugriff auf den aktuell angemeldeten Benutzer, wie
 * auch zum Zugriff auf den aktuellen Zustand der Applikation. Er bietet eine
 * Art Session Mechanismus an, ähnlich wie die HttpSession. Es wird nicht direkt
 * die HttpSession verwendet, da der UserContext auch von BackEnd Logik
 * verwendet wird, und die View - Logik nicht direkt mit der Anwendungslogik
 * vermischt werden soll.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:33:55 $
 */
public final class UserContextHolder {

    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    /**
     * Private Konstruktor, da statischer Zugriff.
     */
    private UserContextHolder() {
    }

    /**
     * Holt den Benutzer des aktuellen Threads (Request).
     * 
     * @return Benutzer des aktuellen Threads (Request).
     */
    public static UserContext getUserContext() {
        return userContext.get();
    }

    /**
     * Initialisierung eines neuen UserContextes mit einem angemeldeten
     * Benutzer.
     * 
     * @param currentBenutzer
     *                zu setzender Benutzer.
     * @return der neu erzeugte Context
     */
    public static UserContext initContextWithAuthenticatable(Authenticatable currentBenutzer) {
        userContext.set(new UserContext(currentBenutzer));

        return getUserContext();
    }

    /**
     * Setzen des aktuellen UserContextes.
     * 
     * @param aContext
     */
    public static void setUserContext(UserContext aContext) {
        userContext.set(aContext);
    }

    /**
     * Löschen der aktuellen Anmeldeinformationen.
     */
    public static void clean() {
        userContext.set(null);
    }
}
