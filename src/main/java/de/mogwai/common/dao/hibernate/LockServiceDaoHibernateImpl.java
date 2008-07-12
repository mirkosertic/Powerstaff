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

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import de.mogwai.common.business.entity.Entity;
import de.mogwai.common.business.entity.LockEntry;
import de.mogwai.common.dao.LockServiceDao;

/**
 * Implementation des LockService DAO für Hibernate.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:24 $
 */
public class LockServiceDaoHibernateImpl extends GenericDaoHibernateImpl implements LockServiceDao {

    public String getObjectName(Object aObject) {
        return aObject.getClass().getName();
    }

    public void removeLocksForUser(final String aUserID) {

        getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) throws SQLException {

                Query theQuery = aSession.createQuery("delete from LockEntry item where item.lockUser = :user");
                theQuery.setString("user", aUserID);

                theQuery.executeUpdate();

                return null;
            }

        });
    }

    public LockEntry getLockEntryFor(final Entity aEntity) {

        return (LockEntry) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) throws SQLException {

                Query theQuery = aSession
                        .createQuery("from LockEntry item where item.objectId = :id and item.objectName = :name");
                theQuery.setLong("id", aEntity.getId());
                theQuery.setString("name", getObjectName(aEntity));

                List theResult = theQuery.list();
                if (theResult.size() == 0) {
                    return null;
                } else {
                    LockEntry theEntry = (LockEntry) theResult.get(0);

                    return theEntry;
                }
            }

        });
    }

    public void removeLocksForSession(final String aSessionID) {
        getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) throws SQLException {

                Query theQuery = aSession.createQuery("delete from LockEntry item where item.sessionId = :sessionId");
                theQuery.setString("sessionId", aSessionID);

                theQuery.executeUpdate();

                return null;
            }

        });
    }
}
