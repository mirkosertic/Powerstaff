/**
 * Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.powerstaff.business.dao.hibernate;

import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import de.mogwai.common.dao.hibernate.GenericDaoHibernateImpl;
import de.powerstaff.business.dao.ProfileSearchDAO;
import de.powerstaff.business.entity.SavedProfileSearch;
import de.powerstaff.business.entity.User;

public class ProfileSearchDAOHibernateImpl extends GenericDaoHibernateImpl implements ProfileSearchDAO {

    public void deleteSavedSearchesFor(final User aUser) {
        getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) {

                Query theQuery = aSession.createQuery("from SavedProfileSearch item where item.user = :user");
                theQuery.setEntity("user", aUser);
                for (Iterator theIt = theQuery.iterate(); theIt.hasNext();) {
                    aSession.delete(theIt.next());
                }
                return null;
            }
        });
    }

    public SavedProfileSearch getSavedSearchFor(final User aUser) {
        return (SavedProfileSearch) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) {

                Query theQuery = aSession.createQuery("from SavedProfileSearch item where item.user = :user");
                theQuery.setEntity("user", aUser);
                for (Iterator theIt = theQuery.iterate(); theIt.hasNext();) {
                    return theIt.next();
                }
                return null;
            }
        });
    }
}
