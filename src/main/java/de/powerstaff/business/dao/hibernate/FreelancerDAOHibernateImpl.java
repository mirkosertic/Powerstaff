/*
  Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.

  This library is free software; you can redistribute it and/or modify it under
  the terms of the GNU Lesser General Public License as published by the Free
  Software Foundation; either version 2.1 of the License, or (at your option)
  any later version.

  This library is distributed in the hope that it will be useful, but WITHOUT
  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
  FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
  details.

  You should have received a copy of the GNU Lesser General Public License
  along with this library; if not, write to the Free Software Foundation, Inc.,
  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
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
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.util.*;

public class FreelancerDAOHibernateImpl extends
        PersonDAOHibernateImpl<Freelancer> implements FreelancerDAO {

    private static final String[] DISPLAYPROPERTIES = new String[]{"name1",
            "name2", "availabilityAsDate", "sallaryLong", "skills", "code"};

    private static final String[] SEARCHPROPERTIES = new String[]{"name1",
            "name2", "company", "street", "country", "plz", "city", "comments",
            "workplace", "sallaryLong", "code", "contactPerson", "contactType",
            "contactReason", "skills", "gulpID", "code",
            "kreditorNr", "titel", "nationalitaet", "einsatzdetails"};

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

    public List<GenericSearchResult> performQBESearch(final Freelancer aObject,
                                                      final int aMaxSearchResult) {

        final List<GenericSearchResult> theSearchResult = performQBESearch(aObject, DISPLAYPROPERTIES, SEARCHPROPERTIES,
                ORDERBYPROPERTIES, MATCH_LIKE, aMaxSearchResult);
        // Tags nachladen...
        for (final GenericSearchResult theRow : theSearchResult) {
            final Long theID = (Long) theRow.get(GenericSearchResult.OBJECT_ID_KEY);
            theRow.put("tags", findById(theID).getAllTagsSorted());
        }

        return theSearchResult;
    }

    public List<String> getCodeSuggestions(final String aSuggest) {
        return (List<String>) getHibernateTemplate().execute(
                (HibernateCallback) aSession -> {
                    final List<String> theResult = new ArrayList<>();

                    final Query theQuery = aSession
                            .createQuery("select item.code from Freelancer item where item.code like '"
                                    + aSuggest.trim()
                                    + "%') order by item.code");
                    for (final Iterator theIterator = theQuery.iterate(); theIterator
                            .hasNext(); ) {
                        final String theCode = (String) theIterator.next();
                        if (!theResult.contains(theCode)) {
                            theResult.add(theCode);
                        }
                    }

                    return theResult;
                });
    }

    public Freelancer findByCodeReal(final String aCode) {
        return (Freelancer) getHibernateTemplate().execute(
                (HibernateCallback) aSession -> {
                    final Query theQuery = aSession
                            .createQuery("from Freelancer item where item.code = :code");
                    theQuery.setString("code", aCode);
                    for (final Iterator theIt = theQuery.iterate(); theIt
                            .hasNext(); ) {
                        return theIt.next();
                    }
                    return null;
                });
    }

    public Collection<GenericSearchResult> performSearchByContact(
            final String aContact, final ContactType aContactType, final int aMax) {
        return performSearchByContact(aContact, aContactType,
                DISPLAYPROPERTIES, ORDERBYPROPERTIES, aMax);
    }

    @Override
    public List<ProjectPosition> findPositionsFor(final Freelancer aFreelancer) {
        return (List<ProjectPosition>) getHibernateTemplate().execute((HibernateCallback<Object>) aSession -> {

            final Criteria theCriteria = aSession.createCriteria(ProjectPosition.class);
            theCriteria.add(Restrictions.eq("freelancerId", aFreelancer.getId()));
            theCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            final ArrayList<ProjectPosition> theResult = new ArrayList<>(theCriteria.list());

            Collections.sort(theResult);

            return theResult;
        });
    }

    @Override
    public void delete(final Object aEntity) throws ReferenceExistsException, OptimisticLockException {

        // Freiberufler dürfen nicht gelöscht werden, wenn sie bereits einem Projekt zugewiesen sind.
        final boolean exists = getHibernateTemplate().execute(aSession -> {
            final Freelancer theFreelancer = (Freelancer) aEntity;
            final Criteria theCriteria = aSession.createCriteria(ProjectPosition.class);
            theCriteria.add(Restrictions.eq("freelancerId", theFreelancer.getId()));
            theCriteria.setProjection(Projections.count("id"));
            final Long theCount = (Long) theCriteria.uniqueResult();
            return theCount != 0;
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
        for (final Long theTagID : aTagIDs) {
            if (theTagInClause.length() > 0) {
                theTagInClause.append(",");
            }
            theTagInClause.append(theTagID);
        }
        return (List<Freelancer>) getHibernateTemplate().execute(
                (HibernateCallback) aSession -> {
                    final List<Freelancer> theResult = new ArrayList<>();

                    final Query theQuery;
                    if (aInverse) {
                        theQuery = aSession
                                .createQuery("select distinct f from Freelancer f left join f.tags t where t.tag.id in ( " + theTagInClause.toString() + ") order by f." + aSortByFieldName+" desc");
                    } else {
                        theQuery = aSession
                                .createQuery("select distinct f from Freelancer f left join f.tags t where t.tag.id in ( " + theTagInClause.toString() + ") order by f." + aSortByFieldName);
                    }
                    for (final Iterator theIterator = theQuery.iterate(); theIterator
                            .hasNext(); ) {
                        final Freelancer theFreelancer = (Freelancer) theIterator.next();
                        if (theFreelancer.hasAllTags(aTagIDs)) {
                            theResult.add(theFreelancer);
                        }
                    }

                    return theResult;
                });
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDs(final Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "name1");
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDsSortByName1(final Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "name1");
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDsSortByName2(final Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "name2");
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDsSortByCode(final Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "code");
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDsSortByAvailability(final Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "availabilityAsDate", true);
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDsSortBySallary(final Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "sallaryLong");
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDsSortByPlz(final Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "plz");
    }

    @Override
    public List<Freelancer> findFreelancerByTagIDsSortByLastContact(final Set<Long> aTagIDs) {
        return internalFindFreelancerByTagIDs(aTagIDs, "lastContactDate", true);
    }
}