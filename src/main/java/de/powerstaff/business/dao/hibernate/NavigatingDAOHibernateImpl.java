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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import de.mogwai.common.business.entity.Entity;
import de.mogwai.common.dao.hibernate.GenericDaoHibernateImpl;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.dao.NavigatingDAO;
import de.powerstaff.business.service.RecordInfo;

public abstract class NavigatingDAOHibernateImpl<T extends Entity> extends GenericDaoHibernateImpl implements
        NavigatingDAO<T> {

    protected static final int MATCH_LIKE = 1;

    protected static final int MATCH_EXACT = 2;

    protected abstract T createNew();

    protected abstract Class getEntityClass();

    public List<GenericSearchResult> performQBESearch(final Entity aObject, final String[] aProperties,
            final String[] aSearchProperties, final String[] aOrderByProperties, final int aMatchMode,
            final int aMaxSearchResult) {

        final Class aType = aObject.getClass();
        return (List) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) throws SQLException {

                List<GenericSearchResult> theResult = new ArrayList<GenericSearchResult>();

                String theQueryString = "select item.id";
                for (String aProperty : aProperties) {
                    if (!aProperty.startsWith("+")) {
                        theQueryString += " ,item." + aProperty;
                    } else {
                        theQueryString += " ,j_" + aProperty.substring(1).toLowerCase();
                    }
                }
                theQueryString += " from " + aType.getName() + " item ";
                for (String aProperty : aProperties) {
                    if (aProperty.startsWith("+")) {
                        String theRealName = aProperty.substring(1);
                        theQueryString += " left outer join item." + theRealName + " as j_" + theRealName.toLowerCase();
                    }
                }

                HashMap<String, Object> theParams = new HashMap<String, Object>();

                boolean theFirst = true;
                for (int i = 0; i < aSearchProperties.length; i++) {
                    String thePropertyName = aSearchProperties[i];
                    Object theValue = null;
                    try {
                        theValue = PropertyUtils.getProperty(aObject, thePropertyName);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if ((theValue != null) && (!"".equals(theValue))) {
                        if (theFirst) {
                            theQueryString += " where ";
                        }
                        if (!theFirst) {
                            theQueryString += " and ";
                        }

                        if (theValue instanceof String) {
                            if (aMatchMode == MATCH_LIKE) {
                                theQueryString += "lower(item." + thePropertyName + ") like :" + thePropertyName;
                                theParams.put(thePropertyName, "%" + theValue.toString().toLowerCase() + "%");
                            } else {

                                theQueryString += "item." + thePropertyName + " = :" + thePropertyName;
                                theParams.put(thePropertyName, theValue);

                            }
                        } else {
                            theQueryString += "item." + thePropertyName + " = :" + thePropertyName;
                            theParams.put(thePropertyName, theValue);
                        }

                        theFirst = false;
                    }
                }

                if ((aOrderByProperties != null) && (aOrderByProperties.length > 0)) {
                    theQueryString += " order by ";
                    for (int i = 0; i < aOrderByProperties.length; i++) {
                        String propertyName = aOrderByProperties[i];
                        if (i > 0) {
                            theQueryString += ",";
                        }
                        theQueryString += " item." + propertyName;
                    }
                }

                Query theQuery = aSession.createQuery(theQueryString);
                for (String theKey : theParams.keySet()) {
                    Object theValue = theParams.get(theKey);
                    theQuery.setParameter(theKey, theValue);
                }
                theQuery.setMaxResults(aMaxSearchResult);
                for (Iterator it = theQuery.list().iterator(); it.hasNext();) {
                    Object[] theRow = (Object[]) it.next();
                    GenericSearchResult theRowObject = new GenericSearchResult();
                    theRowObject.put(GenericSearchResult.OBJECT_ID_KEY, theRow[0]);
                    for (int i = 0; i < aProperties.length; i++) {
                        String thePropertyName = aProperties[i];
                        if (thePropertyName.startsWith(".")) {
                            thePropertyName = thePropertyName.substring(1);
                        }
                        theRowObject.put(thePropertyName, theRow[i + 1]);
                    }
                    theResult.add(theRowObject);
                }

                return theResult;
            }

        });
    }

    public T findById(Long aId) {
        return (T) getHibernateTemplate().get(getEntityClass(), aId);
    }

    public T findFirst() {

        final String theTypeName = getEntityClass().getName();

        return (T) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) throws SQLException {
                Iterator resultIterator = aSession.createQuery(
                        "from " + theTypeName + " i where i.id = (select min(id) from " + theTypeName + ")").list()
                        .iterator();
                if (resultIterator.hasNext()) {
                    return resultIterator.next();
                }

                return createNew();
            }
        });
    }

    public T findLast() {

        final String theTypeName = getEntityClass().getName();

        return (T) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) throws SQLException {
                Iterator resultIterator = aSession.createQuery(
                        "from " + theTypeName + " i where i.id = (select max(id) from " + theTypeName + ")").list()
                        .iterator();
                if (resultIterator.hasNext()) {
                    return resultIterator.next();
                }

                return createNew();
            }
        });
    }

    public T findNext(final T aObject) {

        final String theTypeName = getEntityClass().getName();

        if ((aObject == null) || (aObject.getId() == null)) {
            return findLast();
        }

        return (T) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) throws SQLException {
                Query theQuery = aSession.createQuery("from " + theTypeName + " i where i.id = (select min(j.id) from "
                        + theTypeName + " j where j.id > :currentID)");
                theQuery.setLong("currentID", aObject.getId());
                Iterator resultIterator = theQuery.list().iterator();
                if (resultIterator.hasNext()) {
                    return resultIterator.next();
                }

                return findLast();
            }
        });
    }

    public T findPrior(final T aObject) {

        final String theTypeName = getEntityClass().getName();

        if ((aObject == null) || (aObject.getId() == null)) {
            return findFirst();
        }

        return (T) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) throws SQLException {
                Query theQuery = aSession.createQuery("from " + theTypeName + " i where i.id = (select max(j.id) from "
                        + theTypeName + " j where j.id < :currentID)");
                theQuery.setLong("currentID", aObject.getId());
                Iterator resultIterator = theQuery.list().iterator();
                if (resultIterator.hasNext()) {
                    return resultIterator.next();
                }

                return findFirst();
            }
        });
    }

    public RecordInfo getRecordInfo(final T aObject) {

        final String theTypeName = getEntityClass().getName();

        return (RecordInfo) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) {
                RecordInfo theInfo = new RecordInfo();

                Query theQuery = aSession.createQuery("select count(item.id) from " + theTypeName + " item");
                theInfo.setCount((Long) theQuery.uniqueResult());

                if (aObject.getId() != null) {
                    theQuery = aSession.createQuery("select count(item.id) from " + theTypeName
                            + " item where item.id<= :number");
                    theQuery.setLong("number", aObject.getId());
                    theInfo.setNumber((Long) theQuery.uniqueResult());
                }

                return theInfo;
            }
        });
    }

    public T findByRecordNumber(final Long aNumber) {
        if (aNumber == null) {
            return findFirst();
        }

        return (T) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) {
                Query theQuery = aSession.createQuery("from " + getEntityClass().getName());
                theQuery.setFirstResult(aNumber.intValue() - 1);
                theQuery.setMaxResults(1);
                Iterator theIt = theQuery.iterate();
                if (theIt.hasNext()) {
                    return theIt.next();
                }
                return findFirst();
            }

        });
    }
}
