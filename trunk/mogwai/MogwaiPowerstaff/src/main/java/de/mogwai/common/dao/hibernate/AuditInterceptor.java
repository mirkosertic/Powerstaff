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

import java.io.Serializable;
import java.sql.Timestamp;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import de.mogwai.common.business.entity.Entity;
import de.mogwai.common.business.entity.Historizable;
import de.mogwai.common.business.entity.HistoryEntity;
import de.mogwai.common.logging.Logger;
import de.mogwai.common.usercontext.Authenticatable;
import de.mogwai.common.usercontext.UserContext;
import de.mogwai.common.usercontext.UserContextHolder;

/**
 * Interceptor für das Audit - Log von Entities.
 * 
 * Sobald ein Entity die Eigenschaften
 * "creationUserID","creationDATE","lastModificationUserID" oder
 * "lastModificationDATE" hat, werden diese durch den Interceptor auf den
 * aktuell angemeldeten Benutzer bzw. das aktuelle Systemdatum gesetzt.
 * 
 * Zusätzlich unterstützt dieser Interceptor das Schreiben eines Change - Logs
 * von best. Entitäten in sep. Tabellen. Zu diesem Zweck müssen diese Entitäten
 * die Schnittstelle "Historizable" implementieren. Der Interceptor erstellt
 * beim Ändern dieser Entitäten einen neuen Eintrag in der DB. Das Schreiben
 * erfolgt dabei über Hibernate nach folgender Logik:
 * 
 * Entity - Name History - Entity KontaktPerson KontaktPersonHist Traegerschaft
 * TraegerschaftHist
 * 
 * Es wird also immer ein "Hist" an den Klassennamen der Entität angehängt, und
 * eine neue Instanz dieser Klasse erzeugt und mittels Hibernate persistiert.
 * Diese "Hist" Klassen müssen unterklassen von "HistoryEntity" sein.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:24 $
 */
public class AuditInterceptor extends EmptyInterceptor implements BeanFactoryAware {

    private final static Logger LOGGER = new Logger(AuditInterceptor.class);

    private BeanFactory beanFactory;

    private String sessionFactoryName;

    protected void createHistoryEntityFor(Historizable aEntity, String aReason, Authenticatable aAuthenticatable) {

        try {
            String theHistoryClassName = aEntity.getClass().getName() + "Hist";

            LOGGER.logDebug("Trying to create history entry of type " + theHistoryClassName + " for "
                    + aEntity.getClass());

            HistoryEntity theEntity = (HistoryEntity) Class.forName(theHistoryClassName).newInstance();

            theEntity.setModificationDate(new Timestamp(System.currentTimeMillis()));
            theEntity.setModificationUserID(aAuthenticatable.getUserId());
            theEntity.setReason(aReason);
            theEntity.setData((Entity) aEntity);

            // Hier wird eine temporäre Session erzeugt, da ein Interceptor
            // nicht auf die aktuelle Session
            // zugreifen kann.
            SessionFactory aSessionFactory = (SessionFactory) beanFactory.getBean(sessionFactoryName);
            Session theSession = aSessionFactory.openSession(aSessionFactory.openSession().connection());

            // Die aktuelle Entität speichern
            theSession.save(theEntity);

            // Und nicht vergessen, den Cache der Temporärsession zu löschen
            theSession.flush();

        } catch (Exception e) {

            LOGGER.logDebug("Error saving history entry", e);

            throw new RuntimeException(e);
        }
    }

    /**
     * @see org.hibernate.Interceptor#onSave(java.lang.Object,
     *      java.io.Serializable, java.lang.Object[], java.lang.String[],
     *      org.hibernate.type.Type[])
     */
    @Override
    public boolean onSave(Object aEntity, Serializable aID, Object[] aStates, String[] aPropertyNames, Type[] aTypes) {

        Authenticatable theCurrentUser = null;
        UserContext theContext = UserContextHolder.getUserContext();
        if (theContext != null) {
            theCurrentUser = theContext.getAuthenticatable();
        }

        if (theCurrentUser != null) {

            if (aEntity instanceof Historizable) {

                // Die Entität soll historisiert werden, also einen History -
                // Eintrag erzeugen
                createHistoryEntityFor((Historizable) aEntity, HistoryEntity.REASON_INSERT, theCurrentUser);
            }

            for (int i = 0; i < aPropertyNames.length; i++) {

                String thePropertyName = aPropertyNames[i];
                if ("creationDate".equals(thePropertyName)) {
                    aStates[i] = new Timestamp(System.currentTimeMillis());
                }
                if ("creationUserID".equals(thePropertyName)) {
                    aStates[i] = theCurrentUser.getUserId();
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

        Authenticatable theCurrentUser = null;
        UserContext theContext = UserContextHolder.getUserContext();
        if (theContext != null) {
            theCurrentUser = theContext.getAuthenticatable();
        }

        if (theCurrentUser != null) {

            if (aEntity instanceof Historizable) {

                // Die Entität soll historisiert werden, also einen History -
                // Eintrag erzeugen
                createHistoryEntityFor((Historizable) aEntity, HistoryEntity.REASON_UPDATE, theCurrentUser);
            }

            for (int i = 0; i < aPropertyNames.length; i++) {

                String thePropertyName = aPropertyNames[i];
                if ("lastModificationDate".equals(thePropertyName)) {
                    aCurrentState[i] = new Timestamp(System.currentTimeMillis());
                }
                if ("lastModificationUserID".equals(thePropertyName)) {
                    aCurrentState[i] = theCurrentUser.getUserId();
                }

            }

            // Indicate hibernate that the object was changed
            return true;
        }

        return false;
    }

    /**
     * @see org.hibernate.EmptyInterceptor#onDelete(java.lang.Object,
     *      java.io.Serializable, java.lang.Object[], java.lang.String[],
     *      org.hibernate.type.Type[])
     */
    @Override
    public void onDelete(Object aEntity, Serializable aID, Object[] aCurrentState, String[] aPropertyNames,
            Type[] aTypes) {

        Authenticatable theCurrentUser = null;
        UserContext theContext = UserContextHolder.getUserContext();
        if (theContext != null) {
            theCurrentUser = theContext.getAuthenticatable();
        }

        if (theCurrentUser != null) {

            if (aEntity instanceof Historizable) {

                // Die Entität soll historisiert werden, also einen History -
                // Eintrag erzeugen
                createHistoryEntityFor((Historizable) aEntity, HistoryEntity.REASON_DELETE, theCurrentUser);
            }

        }

        super.onDelete(aEntity, aID, aCurrentState, aPropertyNames, aTypes);
    }

    public String getSessionFactoryName() {
        return sessionFactoryName;
    }

    public void setSessionFactoryName(String sessionFactoryName) {
        this.sessionFactoryName = sessionFactoryName;
    }

    public void setBeanFactory(BeanFactory aFactory) {
        beanFactory = aFactory;
    }
}
