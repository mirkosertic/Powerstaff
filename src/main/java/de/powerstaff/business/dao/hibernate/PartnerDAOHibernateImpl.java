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

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import de.powerstaff.business.dao.PartnerDAO;
import de.powerstaff.business.entity.Partner;

public class PartnerDAOHibernateImpl extends NavigatingDAOHibernateImpl<Partner> implements PartnerDAO {

	@Override
	protected Partner createNew() {
		return new Partner();
	}

	@Override
	protected Class getEntityClass() {
		return Partner.class;
	}

	public List<Partner> performQBESearch(Partner aObject) {
		
		String[] theSearchProperties = new String[] { "name1", "name2", "company", "street", "country", "plz", "city", "comments", 
		};

		String[] theOrderByProperties = new String[] { "name1", "name2" };
		
		return performQBESearch(aObject, theSearchProperties, theOrderByProperties, MATCH_LIKE);
	}

	public List<Partner> findAll() {
		return (List<Partner>) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session aSession) throws HibernateException, SQLException {
				List<Partner> theResult = new Vector<Partner>();
				theResult.addAll(aSession.createQuery("from Partner item order by item.name1, item.name2").list());
				return theResult;
			}
			
		});

	}
}
