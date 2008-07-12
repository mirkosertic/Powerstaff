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
package de.mogwai.common.logging;

import org.apache.log4j.NDC;

/**
 * Logging Klasse. Sämtliche Log Aufrufe werden an Jakarta Commons Logging
 * delegiert, welches diese wiederum an Log4j delegiert.
 * 
 * Wichtig: Der Benutzer wird bei den Log-Methoden jeweils nicht mitgegeben. Die
 * Übergabe des Benutzers erfolgt über den Log4j Nested Diagnostic Context. Dies
 * muss in der Basis Struts Action der Applikation erfolgen.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:55 $
 */
public class Logger {

    private org.apache.log4j.Logger log;

    /**
     * Instanzierung eines benannten Loggers. Der Name des Loggers entspricht
     * dem Namen der übergebenen Klasse. Sinn: Beim Einsatz der Apache Log4j API
     * können über das Konfig File Package oder Klassenfilter gesetzt werden.
     * 
     * @param clazz
     *                Class for which a log name will be derived.
     */
    public Logger(Class clazz) {

        // Log Instanz über Factory holen.
        log = org.apache.log4j.Logger.getLogger(clazz);
    }

    /**
     * Logt einen Fehler der die Stabilität des Programms beeinflusst.
     * 
     * @param message
     *                Fehlermeldung.
     */
    public void logFatal(String message) {
        log.fatal(message);
    }

    /**
     * Logt einen Fehler der die Stabilität des Programms beeinflusst.
     * 
     * @param message
     *                Fehlermeldung.
     * @param t
     *                Ursache des Fehlers.
     */
    public void logFatal(String message, Throwable t) {
        log.fatal(message, t);
    }

    /**
     * Logt einen Fehler, der nicht automatisch behoben werden kann.
     * 
     * @param message
     *                Fehlermeldung.
     */
    public void logError(String message) {
        log.error(message);
    }

    /**
     * Logt einen Fehler, der nicht automatisch behoben werden kann.
     * 
     * @param message
     *                Fehlermeldung.
     * @param t
     *                Ursache des Fehlers.
     */
    public void logError(String message, Throwable t) {
        log.error(message, t);
    }

    /**
     * Logt einen Fehler, der behoben oder übergangen werden konnte.
     * 
     * @param message
     *                Meldung.
     */
    public void logWarning(String message) {
        log.warn(message);
    }

    /**
     * Logt einen Fehler, der behoben oder übergangen werden konnte.
     * 
     * @param message
     *                Meldung.
     * @param t
     *                Ursache des Warnung.
     */
    public void logWarning(String message, Throwable t) {
        log.warn(message, t);
    }

    /**
     * Logt eine Information zum Programmablauf.
     * 
     * @param message
     *                Meldung.
     */
    public void logInfo(String message) {
        log.info(message);
    }

    /**
     * Logt eine Information zum Programmablauf.
     * 
     * @param message
     *                Meldung.
     * @param t
     *                Ursache.
     */
    public void logInfo(String message, Throwable t) {
        log.info(message, t);
    }

    /**
     * Logt eine Information zum Nachvollziehen des Programmstatus.
     * 
     * @param message
     *                Meldung.
     */
    public void logDebug(String message) {
        log.debug(message);
    }

    /**
     * Logt eine Information zum Nachvollziehen des Programmstatus.
     * 
     * @param message
     *                Meldung.
     * @param t
     *                Ursache.
     */
    public void logDebug(String message, Throwable t) {
        log.debug(message, t);
    }

    /**
     * Seten des aktuellen Debug - Contextes ( Log4J ).
     * 
     * @param theRemoteUser
     *                das aktuell angemeldete Benutzer
     */
    public void setDebugContext(String theRemoteUser) {
        NDC.push(theRemoteUser);
    }

    /**
     * Freigabe des aktuellen Debug - Contextes ( Log4J ).
     */
    public void unsetDebugContext() {
        NDC.pop();
        NDC.remove();
    }
}
