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
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.ProjectPosition;
import de.powerstaff.business.service.OptimisticLockException;
import de.powerstaff.business.service.ReferenceExistsException;
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
            "contactReason", "skills", "gulpID", "code",
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

    public List<GenericSearchResult> performQBESearch(Freelancer aObject,
                                                      int aMaxSearchResult) {

        List<GenericSearchResult> theSearchResult = performQBESearch(aObject, DISPLAYPROPERTIES, SEARCHPROPERTIES,
                ORDERBYPROPERTIES, MATCH_LIKE, aMaxSearchResult);
        // Tags nachladen...
        for (GenericSearchResult theRow : theSearchResult) {
            Long theID = (Long) theRow.get(GenericSearchResult.OBJECT_ID_KEY);
            theRow.put("tags", findById(theID).getAllTagsSorted());
        }

        return theSearchResult;
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
                theCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                theResult.addAll(theCriteria.list());

                Collections.sort(theResult);

                return theResult;
            }
        });
    }

    @Override
    public void delete(final Object aEntity) throws ReferenceExistsException, OptimisticLockException {

        // Freiberufler d�rfen nicht gel�scht werden, wenn sie bereits einem Projekt zugewiesen sind.
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

    private List<Freelancer> internalFindFreelancerByTagIDs(final Set<Long> aTagIDs, final String aSortByFieldName) {
        return internalFindFreelancerByTagIDs(aTagIDs, aSortByFieldName, false);
    }

    private List<Freelancer> internalFindFreelancerByTagIDs(final Set<Long> aTagIDs, final String aSortByFieldName, final boolean aInverse) {
        final StringBuilder theTagInClause = new StringBuilder();
        for (Long theTagID : aTagIDs) {
            if (theTagInClause.length() > 0) {
                theTagInClause.append(",");
            }
            theTagInClause.append(theTagID);
        }
        return (List<Freelancer>) getHibernateTemplate().execute(
                new HibernateCallback() {

                    public Object doInHibernate(Session aSession)
                            throws SQLException {
                        List<Freelancer> theResult = new ArrayList<Freelancer>();

                        Query theQuery;
                        if (aInverse) {
                            theQuery = aSession
                                    .createQuery("select distinct f from Freelancer f left join f.tags t where t.tag.id in ( " + theTagInClause.toString() + ") order by f." + aSortByFieldName+" desc");
                        } else {
                            theQuery = aSession
                                    .createQuery("select distinct f from Freelancer f left join f.tags t where t.tag.id in ( " + theTagInClause.toString() + ") order by f." + aSortByFieldName);
                        }
                        for (Iterator theIterator = theQuery.iterate(); theIterator
                                .hasNext(); ) {
                            Freelancer theFreelancer = (Freelancer) theIterator.next();
                            if (theFreelancer.hasAllTags(aTagIDs)) {
                                theResult.add(theFreelancer);
                            }
                        }

                        return theResult;
                    }

                });
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDs(final Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "name1");
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDsSortByName1(Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "name1");
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDsSortByName2(Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "name2");
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDsSortByCode(Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "code");
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDsSortByAvailability(Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "availabilityAsDate", true);
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDsSortBySallary(Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "sallaryLong");
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDsSortByPlz(Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "plz");
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDsSortByLastContact(Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "lastContactDate", true);
    }
}