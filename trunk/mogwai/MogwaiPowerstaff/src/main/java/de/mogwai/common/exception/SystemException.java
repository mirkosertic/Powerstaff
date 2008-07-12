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
 * Abstrakte Oberklasse für technische Laufzeitfehler. Da auf diese in der Regel
 * applikatorisch nicht sinnvoll reagiert werden kann (es sind in diesem Sinne
 * Ausnahmefehler), erbt die Klasse von <code>java.lang.RuntimeException</code>.
 * Systemfehler werden zentral abgefangen und geloggt. Da der Benutzer mit
 * Systemfehler nichts anfangen kann, werden sie ihm nicht angezeigt und werden
 * daher auch nicht lokalisiert.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:18 $
 */
public abstract class SystemException extends RuntimeException {

    /**
     * @param message
     *                zu loggende Meldung in deutscher Sprache (keine
     *                Lokalisierung!).
     */
    public SystemException(String message) {
        super(message);
    }

    /**
     * @param message
     *                zu loggende Meldung in deutscher Sprache (keine
     *                Lokalisierung!)
     * @param cause
     *                zu loggendes Throwable Objekt.
     */
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

}
