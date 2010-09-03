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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import de.mogwai.common.dao.hibernate.GenericDaoHibernateImpl;
import de.powerstaff.business.dao.StatistikDAO;
import de.powerstaff.business.dto.KontakthistorieEntry;
import de.powerstaff.business.entity.Customer;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.HistoryType;
import de.powerstaff.business.entity.Partner;
import de.powerstaff.business.entity.User;

public class StatistikDAOHibernateImpl extends GenericDaoHibernateImpl
		implements StatistikDAO {

	@Override
	public List<KontakthistorieEntry> kontakthistorie(final Date aDatumVon,
			final Date aDatumBis, final User aBenutzer) {
		return (List<KontakthistorieEntry>) getHibernateTemplate().execute(
				new HibernateCallback() {

					@Override
					public Object doInHibernate(Session aSession)
							throws SQLException {
						List<KontakthistorieEntry> theResult = new ArrayList<KontakthistorieEntry>();

						Conjunction theRestrictions = Restrictions
								.conjunction();
						if (aDatumVon != null) {
							theRestrictions.add(Restrictions.ge(
									"h.creationDate", aDatumVon));
						}
						if (aDatumBis != null) {
							theRestrictions.add(Restrictions.le(
									"h.creationDate", aDatumBis));
						}
						if (aBenutzer != null) {
							theRestrictions.add(Restrictions.eq(
									"h.creationUserID", aBenutzer.getUserId()));
						}

						// Freiberufler
						Criteria theCriteria = aSession.createCriteria(
								Freelancer.class, "p");
						theCriteria.createCriteria("history", "h");
						theCriteria.add(theRestrictions);

						ProjectionList theProjections = Projections
								.projectionList();
						theProjections.add(Projections.property("p.name1"));
						theProjections.add(Projections.property("p.name2"));
						theProjections.add(Projections.property("p.code"));
						theProjections.add(Projections
								.property("h.creationDate"));
						theProjections.add(Projections
								.property("h.creationUserID"));
						theProjections.add(Projections.property("h.type"));
						theProjections.add(Projections
								.property("h.description"));

						theCriteria.setProjection(theProjections);
						for (Object theResultObject : theCriteria.list()) {
							Object[] theResultArray = (Object[]) theResultObject;

							KontakthistorieEntry theEntry = new KontakthistorieEntry();
							theEntry.setName1((String) theResultArray[0]);
							theEntry.setName2((String) theResultArray[1]);
							theEntry.setCode((String) theResultArray[2]);
							Timestamp theTimestamp = (Timestamp) theResultArray[3];
							theEntry.setDatum(new Date(theTimestamp.getTime()));
							theEntry.setUserid((String) theResultArray[4]);
							theEntry.setType((HistoryType) theResultArray[5]);
							theEntry.setDescription((String) theResultArray[6]);

							theResult.add(theEntry);
						}

						// Partner
						theCriteria = aSession.createCriteria(Partner.class,
								"p");
						theCriteria.createCriteria("history", "h");
						theCriteria.add(theRestrictions);

						theProjections = Projections.projectionList();
						theProjections.add(Projections.property("p.name1"));
						theProjections.add(Projections.property("p.name2"));
						theProjections.add(Projections
								.property("h.creationDate"));
						theProjections.add(Projections
								.property("h.creationUserID"));
						theProjections.add(Projections.property("h.type"));
						theProjections.add(Projections
								.property("h.description"));

						theCriteria.setProjection(theProjections);
						for (Object theResultObject : theCriteria.list()) {
							Object[] theResultArray = (Object[]) theResultObject;

							KontakthistorieEntry theEntry = new KontakthistorieEntry();
							theEntry.setName1((String) theResultArray[0]);
							theEntry.setName2((String) theResultArray[1]);
							Timestamp theTimestamp = (Timestamp) theResultArray[2];
							theEntry.setDatum(new Date(theTimestamp.getTime()));
							theEntry.setUserid((String) theResultArray[3]);
							theEntry.setType((HistoryType) theResultArray[4]);
							theEntry.setDescription((String) theResultArray[5]);

							theResult.add(theEntry);
						}

						// Kunden
						theCriteria = aSession.createCriteria(Customer.class,
								"p");
						theCriteria.createCriteria("history", "h");
						theCriteria.add(theRestrictions);

						theProjections = Projections.projectionList();
						theProjections.add(Projections.property("p.name1"));
						theProjections.add(Projections.property("p.name2"));
						theProjections.add(Projections
								.property("h.creationDate"));
						theProjections.add(Projections
								.property("h.creationUserID"));
						theProjections.add(Projections.property("h.type"));
						theProjections.add(Projections
								.property("h.description"));

						theCriteria.setProjection(theProjections);
						for (Object theResultObject : theCriteria.list()) {
							Object[] theResultArray = (Object[]) theResultObject;

							KontakthistorieEntry theEntry = new KontakthistorieEntry();
							theEntry.setName1((String) theResultArray[0]);
							theEntry.setName2((String) theResultArray[1]);
							Timestamp theTimestamp = (Timestamp) theResultArray[2];
							theEntry.setDatum(new Date(theTimestamp.getTime()));
							theEntry.setUserid((String) theResultArray[3]);
							theEntry.setType((HistoryType) theResultArray[4]);
							theEntry.setDescription((String) theResultArray[5]);

							theResult.add(theEntry);
						}

						Collections.sort(theResult, new ReverseComparator(
								new BeanComparator("datum")));

						return theResult;
					}
				});
	}

}
