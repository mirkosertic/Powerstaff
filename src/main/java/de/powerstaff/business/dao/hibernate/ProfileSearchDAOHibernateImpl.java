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

import de.mogwai.common.dao.hibernate.GenericDaoHibernateImpl;
import de.powerstaff.business.dao.ProfileSearchDAO;
import de.powerstaff.business.entity.SavedProfileSearch;
import org.bouncycastle.asn1.isismtt.x509.Restriction;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

public class ProfileSearchDAOHibernateImpl extends GenericDaoHibernateImpl implements ProfileSearchDAO {

    public SavedProfileSearch getSavedSearchForUser(final String aUsername) {
        return (SavedProfileSearch) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) {

                Criteria theCriteria = aSession.createCriteria(SavedProfileSearch.class);
                theCriteria.add(Restrictions.isNull("project")).createCriteria("user").add(Restrictions.eq("name", aUsername));
                for (Object theRow : theCriteria.list()) {
                    return theRow;
                }
                return null;
            }
        });
    }

    @Override
    public SavedProfileSearch getSavedSearchById(Long aId) {
        return getHibernateTemplate().get(SavedProfileSearch.class, aId);
    }
}