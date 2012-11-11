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

import de.powerstaff.business.dao.FreelancerDAO;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.dto.ProfileSearchInfoDetail;
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.ProjectPosition;
import de.powerstaff.business.entity.SavedProfileSearch;
import de.powerstaff.business.service.ReferenceExistsException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;
import java.util.*;

public class FreelancerDAOHibernateImpl extends
        PersonDAOHibernateImpl<Freelancer> implements FreelancerDAO {

    private static final String[] DISPLAYPROPERTIES = new String[]{"name1",
            "name2", "availabilityAsDate", "sallaryLong", "skills", "code"};

    private static final String[] SEARCHPROPERTIES = new String[]{"name1",
            "name2", "company", "street", "country", "plz", "city", "comments",
            "workplace", "sallaryLong", "code", "contactPerson", "contactType",
            "contactReason", "lastContact", "skills", "gulpID", "code",
            "kreditorNr", "debitorNr", "titel", "nationalitaet"};

    private static final String[] ORDERBYPROPERTIES = new String[]{"name1",
            "name2"};

    @Override
    protected Freelancer createNew() {
        return new Freelancer();
    }

    @Override
    protected Class getEntityClass() {
        return Freelancer.class;
    }

    public List<String> findCodesBy(final SavedProfileSearch request) {
        return (List<String>) getHibernateTemplate().execute(
                new HibernateCallback() {

                    public Object doInHibernate(Session aSession)
                            throws SQLException {

                        String theQueryString = "select item.code from Freelancer item where true=true ";

                        Long theSallaryStart = request.getStundensatzVon();
                        if (theSallaryStart != null) {
                            theQueryString += "and item.sallaryLong >= "
                                    + theSallaryStart;
                        }

                        Long theSallaryEnd = request.getStundensatzBis();
                        if (theSallaryEnd != null) {
                            theQueryString += "and item.sallaryLong <= "
                                    + theSallaryEnd;
                        }

                        String thePlz = request.getPlz();
                        if (!StringUtils.isEmpty(thePlz)) {
                            theQueryString += "and item.plz like '" + thePlz
                                    + "'";
                        }

                        Query theQuery = aSession.createQuery(theQueryString);
                        Iterator theIterator = theQuery.list().iterator();
                        List<String> theResult = new ArrayList<String>();
                        if (theIterator.hasNext()) {
                            theResult.add((String) theIterator.next());
                        }

                        return theResult;
                    }

                });
    }

    public ProfileSearchInfoDetail findByCode(final String aCode) {
        return (ProfileSearchInfoDetail) getHibernateTemplate().execute(
                new HibernateCallback() {

                    public Object doInHibernate(Session aSession)
                            throws SQLException {

                        String theQueryString = "select item.name1, item.name2, item.availabilityAsDate, item.id , item.sallaryLong, item.country, item.plz from Freelancer item where item.code = :code ";

                        Query theQuery = aSession.createQuery(theQueryString);
                        theQuery.setString("code", aCode);
                        Iterator theIterator = theQuery.list().iterator();
                        if (theIterator.hasNext()) {
                            Object[] theRow = (Object[]) theIterator.next();

                            ProfileSearchInfoDetail theDetail = new ProfileSearchInfoDetail();
                            theDetail.setName1((String) theRow[0]);
                            theDetail.setName2((String) theRow[1]);

                            theDetail.setAvailability((Date) theRow[2]);
                            theDetail.setId((Long) theRow[3]);
                            theDetail.setStundensatz((Long) theRow[4]);

                            String theCountry = (String) theRow[5];
                            String thePlz = (String) theRow[6];

                            Query theContactQuery = aSession
                                    .createQuery("select c from Freelancer item inner join item.contacts as c where item.code = :code");
                            theContactQuery.setString("code", aCode);
                            theDetail.getContacts().addAll(
                                    theContactQuery.list());

                            if (!StringUtils.isEmpty(theCountry)) {
                                if (StringUtils.isEmpty(thePlz)) {
                                    thePlz = theCountry;
                                } else {
                                    thePlz = theCountry + thePlz;
                                }
                            }
                            theDetail.setPlz(thePlz);

                            return theDetail;
                        }

                        return null;
                    }

                });
    }

    public List<GenericSearchResult> performQBESearch(Freelancer aObject,
                                                      int aMaxSearchResult) {

        return performQBESearch(aObject, DISPLAYPROPERTIES, SEARCHPROPERTIES,
                ORDERBYPROPERTIES, MATCH_LIKE, aMaxSearchResult);
    }

    public List<String> getCodeSuggestions(final String aSuggest) {
        return (List<String>) getHibernateTemplate().execute(
                new HibernateCallback() {

                    public Object doInHibernate(Session aSession)
                            throws SQLException {
                        List<String> theResult = new Vector<String>();

                        Query theQuery = aSession
                                .createQuery("select item.code from Freelancer item where item.code like '"
                                        + aSuggest.trim()
                                        + "%') order by item.code");
                        for (Iterator theIterator = theQuery.iterate(); theIterator
                                .hasNext(); ) {
                            String theCode = (String) theIterator.next();
                            if (!theResult.contains(theCode)) {
                                theResult.add(theCode);
                            }
                        }

                        return theResult;
                    }

                });
    }

    public Freelancer findByCodeReal(final String aCode) {
        return (Freelancer) getHibernateTemplate().execute(
                new HibernateCallback() {

                    public Object doInHibernate(Session aSession) {
                        Query theQuery = aSession
                                .createQuery("from Freelancer item where item.code = :code");
                        theQuery.setString("code", aCode);
                        for (Iterator theIt = theQuery.iterate(); theIt
                                .hasNext(); ) {
                            return theIt.next();
                        }
                        return null;
                    }

                });
    }

    public Collection<GenericSearchResult> performSearchByContact(
            String aContact, ContactType aContactType, int aMax) {
        return performSearchByContact(aContact, aContactType,
                DISPLAYPROPERTIES, ORDERBYPROPERTIES, aMax);
    }

    @Override
    public List<ProjectPosition> findPositionsFor(final Freelancer aFreelancer) {
        return (List<ProjectPosition>) getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session aSession) throws HibernateException, SQLException {
                ArrayList<ProjectPosition> theResult = new ArrayList<ProjectPosition>();

                Criteria theCriteria = aSession.createCriteria(ProjectPosition.class);
                theCriteria.add(Restrictions.eq("freelancerId", aFreelancer.getId()));
                theResult.addAll(theCriteria.list());

                Collections.sort(theResult);

                return theResult;
            }
        });
    }

    @Override
    public void delete(final Object aEntity) throws ReferenceExistsException {

        // Freiberufler dürfen nicht gelöscht werden, wenn sie bereits einem Projekt zugewiesen sind.
        boolean exists = getHibernateTemplate().execute(new HibernateCallback<Boolean>() {
            @Override
            public Boolean doInHibernate(Session aSession) throws HibernateException, SQLException {
                Freelancer theFreelancer = (Freelancer) aEntity;
                Criteria theCriteria = aSession.createCriteria(ProjectPosition.class);
                theCriteria.add(Restrictions.eq("freelancerId", theFreelancer.getId()));
                theCriteria.setProjection(Projections.count("id"));
                Long theCount = (Long) theCriteria.uniqueResult();
                return theCount != 0;
            }
        });

        if (exists) {
            throw new ReferenceExistsException();
        }

        super.delete(aEntity);
    }
}