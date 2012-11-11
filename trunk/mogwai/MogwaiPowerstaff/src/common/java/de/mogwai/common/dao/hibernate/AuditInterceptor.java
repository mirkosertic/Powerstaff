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
package de.mogwai.common.dao.hibernate;

import de.mogwai.common.usercontext.UserContext;
import de.mogwai.common.usercontext.UserContextHolder;
import de.powerstaff.business.entity.User;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Interceptor für das Audit - Log von Entities.
 * <p/>
 * Sobald ein Entity die Eigenschaften
 * "creationUserID","creationDATE","lastModificationUserID" oder
 * "lastModificationDATE" hat, werden diese durch den Interceptor auf den
 * aktuell angemeldeten Benutzer bzw. das aktuelle Systemdatum gesetzt.
 * <p/>
 * Zusätzlich unterstützt dieser Interceptor das Schreiben eines Change - Logs
 * von best. Entitäten in sep. Tabellen. Zu diesem Zweck müssen diese Entitäten
 * die Schnittstelle "Historizable" implementieren. Der Interceptor erstellt
 * beim Ändern dieser Entitäten einen neuen Eintrag in der DB. Das Schreiben
 * erfolgt dabei über Hibernate nach folgender Logik:
 * <p/>
 * Entity - Name History - Entity KontaktPerson KontaktPersonHist Traegerschaft
 * TraegerschaftHist
 * <p/>
 * Es wird also immer ein "Hist" an den Klassennamen der Entität angehängt, und
 * eine neue Instanz dieser Klasse erzeugt und mittels Hibernate persistiert.
 * Diese "Hist" Klassen müssen unterklassen von "HistoryEntity" sein.
 *
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:25:24 $
 */
public class AuditInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = 6801708632533685578L;

    /**
     * @see org.hibernate.Interceptor#onSave(java.lang.Object,
     *      java.io.Serializable, java.lang.Object[], java.lang.String[],
     *      org.hibernate.type.Type[])
     */
    @Override

    public boolean onSave(Object aEntity, Serializable aID, Object[] aStates, String[] aPropertyNames, Type[] aTypes) {

        User theCurrentUser = null;
        UserContext theUserContext = UserContextHolder.getUserContext();
        if (theUserContext != null) {
            theCurrentUser = (User) theUserContext.getAuthenticatable();
        }

        if (theCurrentUser != null) {

            for (int i = 0; i < aPropertyNames.length; i++) {

                String thePropertyName = aPropertyNames[i];
                if ("creationDate".equals(thePropertyName)) {
                    aStates[i] = new Timestamp(System.currentTimeMillis());
                }
                if ("creationUserID".equals(thePropertyName)) {
                    aStates[i] = theCurrentUser.getUsername();
                }

            }

            // Indicate hibernate that the object was changed
            return true;
        }

        return false;
    }

    /**
     * @see org.hibernate.Interceptor#onFlushDirty(java.lang.Object,
     *      java.io.Serializable, java.lang.Object[], java.lang.Object[],
     *      java.lang.String[], org.hibernate.type.Type[])
     */
    @Override
    public boolean onFlushDirty(Object aEntity, Serializable aID, Object[] aCurrentState, Object[] aPreviousState,
                                String[] aPropertyNames, Type[] aTypes) {

        User theCurrentUser = null;
        UserContext theUserContext = UserContextHolder.getUserContext();
        if (theUserContext != null) {
            theCurrentUser = (User) theUserContext.getAuthenticatable();
        }

        if (theCurrentUser != null) {

            for (int i = 0; i < aPropertyNames.length; i++) {

                String thePropertyName = aPropertyNames[i];
                if ("lastModificationDate".equals(thePropertyName)) {
                    aCurrentState[i] = new Timestamp(System.currentTimeMillis());
                }
                if ("lastModificationUserID".equals(thePropertyName)) {
                    aCurrentState[i] = theCurrentUser.getUsername();
                }

            }

            // Indicate hibernate that the object was changed
            return true;
        }

        return false;
    }
}
