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

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import de.mogwai.common.dao.hibernate.GenericDaoHibernateImpl;
import de.powerstaff.business.dao.AdditionalDataDAO;
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.HistoryType;

public class AdditionalDataDAOHibernateImpl extends GenericDaoHibernateImpl implements AdditionalDataDAO {

    public List<ContactType> getContactTypes() {
        return (List<ContactType>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) throws SQLException {
                List<ContactType> theResult = new Vector<ContactType>();
                theResult.addAll(aSession.createQuery("from ContactType item order by item.description").list());
                return theResult;
            }

        });
    }

    @Override
    public List<HistoryType> getHistoryTypes() {
        return (List<HistoryType>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) throws SQLException {
                List<HistoryType> theResult = new Vector<HistoryType>();
                theResult.addAll(aSession.createQuery("from HistoryType item order by item.description").list());
                return theResult;
            }

        });
    }

}
