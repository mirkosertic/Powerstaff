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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.dao.PersonDAO;
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.Person;

public abstract class PersonDAOHibernateImpl<T extends Person> extends NavigatingDAOHibernateImpl<T> implements
        PersonDAO<T> {

    protected Collection<GenericSearchResult> performSearchByContact(final String aContact,
            final ContactType aContactType, final String[] aDisplayProperties, final String[] aOrderByProperties,
            final int aMax) {
        return (Collection<GenericSearchResult>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) throws SQLException {
                Criteria theCriteria = aSession.createCriteria(getEntityClass());

                ProjectionList theList = Projections.projectionList();
                theList.add(Projections.property("id"));
                for (String theProperty : aDisplayProperties) {
                    theList.add(Projections.property(theProperty));
                }

                theCriteria.setProjection(theList);
                
                for (String theProperty : aOrderByProperties) {
                    theCriteria.addOrder(Order.asc(theProperty));
                }
                
                Criteria theContacts = theCriteria.createCriteria("contacts");
                theContacts.add(Restrictions.eq("type", aContactType));
                theContacts.add(Restrictions.ilike("value", "%" + aContact + "%"));

                Collection<GenericSearchResult> theResult = new ArrayList<GenericSearchResult>();

                theCriteria.setMaxResults(aMax);
                for (Iterator it = theCriteria.list().iterator(); it.hasNext();) {
                    Object[] theRow = (Object[]) it.next();
                    GenericSearchResult theRowObject = new GenericSearchResult();
                    theRowObject.put(GenericSearchResult.OBJECT_ID_KEY, theRow[0]);
                    for (int i = 0; i < aDisplayProperties.length; i++) {
                        theRowObject.put(aDisplayProperties[i], theRow[i + 1]);
                    }
                    theResult.add(theRowObject);
                }

                return theResult;

            }

        });
    }

}