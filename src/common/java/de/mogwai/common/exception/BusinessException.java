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
 * Systemfehler der Businesslogik-Schicht. Systemfehler werden zentral
 * abgefangen und geloggt. Da der Benutzer mit Systemfehler nichts anfangen
 * kann, werden sie ihm nicht angezeigt und werden daher auch nicht lokalisiert.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:38:51 $
 */
public class BusinessException extends SystemException {

	private static final long serialVersionUID = -7663943401518505155L;

	/**
	 * @param message
	 *            zu loggende Meldung in deutscher Sprache (keine
	 *            Lokalisierung!).
	 */
	public BusinessException(String message) {
		super(message);
	}
}
