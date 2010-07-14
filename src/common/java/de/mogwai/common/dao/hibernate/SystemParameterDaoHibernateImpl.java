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

import java.util.List;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import de.mogwai.common.business.entity.SystemParameter;
import de.mogwai.common.business.enums.BaseEnumItemEnum;
import de.mogwai.common.dao.SystemParameterDao;

/**
 * Hibernate Implementation des Systemparameter Data Access Objects.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:25:25 $
 */
public class SystemParameterDaoHibernateImpl extends GenericDaoHibernateImpl
		implements SystemParameterDao {

	@Override
	public void save(Object aObject) {
		getHibernateTemplate().save(aObject);
	}

	public void update(Object aObject) {
		getHibernateTemplate().update(aObject);
	}

	/**
	 * @see de.mogwai.common.dao.SystemParameterDao#getAll()
	 */
	public List getAll() {

		return getHibernateTemplate().find("from SystemParameter order by id");
	}

	public Boolean getBoolean(final BaseEnumItemEnum aEnumItem) {
		return (Boolean) getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session aSession) {

						SystemParameter theParameter = (SystemParameter) aSession
								.get(SystemParameter.class, aEnumItem.getId());
						if (theParameter != null) {
							return Boolean.TRUE.toString().equals(
									theParameter.getValue());
						}
						return Boolean.FALSE;
					}
				});
	}

	public String getString(final BaseEnumItemEnum aEnumItem) {
		return (String) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session aSession) {

				SystemParameter theParameter = (SystemParameter) aSession.get(
						SystemParameter.class, aEnumItem.getId());
				if (theParameter != null) {
					return theParameter.getValue();
				}
				return null;
			}
		});
	}

	public void setBoolean(final BaseEnumItemEnum aEnumItem,
			final String aName, final Boolean aValue) {
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session aSession) {

				String theStrValue = Boolean.FALSE.toString();
				if (Boolean.TRUE.equals(aValue)) {
					theStrValue = Boolean.TRUE.toString();
				}

				SystemParameter theParameter = (SystemParameter) aSession.get(
						SystemParameter.class, aEnumItem.getId());
				if (theParameter != null) {

					theParameter.setValue(theStrValue);
					aSession.update(theParameter);

				} else {
					theParameter = new SystemParameter();
					theParameter.setName(aName);
					theParameter.setId(aEnumItem.getId());
					theParameter.setValue(theStrValue);

					aSession.save(theParameter);
				}
				return null;
			}
		});
	}

	public void setString(final BaseEnumItemEnum aEnumItem, final String aName,
			final String aValue) {
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session aSession) {

				SystemParameter theParameter = (SystemParameter) aSession.get(
						SystemParameter.class, aEnumItem.getId());
				if (theParameter != null) {

					theParameter.setValue(aValue);
					aSession.update(theParameter);

				} else {
					theParameter = new SystemParameter();
					theParameter.setName(aName);
					theParameter.setId(aEnumItem.getId());
					theParameter.setValue(aValue);

					aSession.save(theParameter);
				}
				return null;
			}
		});
	}

	public int getInt(final BaseEnumItemEnum aEnumItem) {
		return (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session aSession) {

						SystemParameter theParameter = (SystemParameter) aSession
								.get(SystemParameter.class, aEnumItem.getId());
						if (theParameter != null) {
							return Integer.parseInt(theParameter.getValue());
						}
						return Integer.valueOf(0);
					}
				});
	}

	public void setInt(final BaseEnumItemEnum aEnumItem, final String aName,
			final int aValue) {
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session aSession) {

				SystemParameter theParameter = (SystemParameter) aSession.get(
						SystemParameter.class, aEnumItem.getId());
				if (theParameter != null) {

					theParameter.setValue("" + aValue);
					aSession.update(theParameter);

				} else {
					theParameter = new SystemParameter();
					theParameter.setName(aName);
					theParameter.setId(aEnumItem.getId());
					theParameter.setValue("" + aValue);

					aSession.save(theParameter);
				}
				return null;
			}
		});
	}
}
