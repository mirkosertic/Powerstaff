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
package de.mogwai.common.business.service;

import de.mogwai.common.business.entity.Entity;

/**
 * Schnittstelle für den LockService.
 * 
 * Über den LockService können Gesperrt - Flags für einzelne Objekte innerhalb
 * der Anwendung pro Benutzer gesetzt und abgefragt werden.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:27:59 $
 */
public interface LockService extends Service {

    /**
     * Überprüfung, ob ein Objekt von einem Benutzer gesperrt wurde.
     * 
     * @param aEntity
     *                das Objekt
     * @return true wenn es gesperrt wurde, sonst false
     */
    boolean isObjectLocked(Entity aEntity);

    /**
     * Sperren eines Objektes durch einen Benutzer.
     * 
     * @param aUserID
     *                die Benutzer ID
     * @param aEntity
     *                das Objekt
     */
    void lockObject(String aUserID, Entity aEntity);

    /**
     * Sperren eines Objektes durch den aktuell angemeldeten Benutzer.
     * 
     * Der Benutzer wird aus dem aktuellen UserContext ermittelt.
     * 
     * @param aEntity
     *                das Objekt
     */
    void lockObjectByCurrentUser(Entity aEntity);

    /**
     * Entsperren eines Objektes durch einen Benutzer.
     * 
     * @param aUserID
     *                die Benutzer ID
     * @param aEntity
     *                das Objekt
     */
    void unlockObject(String aUserID, Entity aEntity);

    /**
     * Entsperren eines Objektes durch den aktuell angemeldeten Benutzer.
     * 
     * @param aEntity
     *                das Objekt
     */
    void unlockObjectbyCurrentUser(Entity aEntity);

    /**
     * Löschen aller Locks für einen bestimmten Benutzer.
     * 
     * @param aUserID
     *                die Benutzer ID
     */
    void removeLocksForUser(String aUserID);

    /**
     * Löschen aller Locks für einen bestimmten Benutzer.
     * 
     * @param aSessionID
     *                die session ID
     */
    void removeLocksForSession(String aSessionID);

    /**
     * Löschen aller Locks für einen bestimmten Benutzer.
     * 
     * Es wird die aktuelle Session aus dem UserContext verwendet.
     */
    void removeLocksForCurrentSession();

}
