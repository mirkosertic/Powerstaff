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
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import de.powerstaff.business.dao.FreelancerDAO;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.dto.ProfileSearchInfoDetail;
import de.powerstaff.business.dto.ProfileSearchRequest;
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.Freelancer;

public class FreelancerDAOHibernateImpl extends PersonDAOHibernateImpl<Freelancer> implements FreelancerDAO {

    private static final String[] DISPLAYPROPERTIES = new String[] { "name1", "name2", "availabilityAsDate", "sallaryLong", "skills" };

    private static final String[] SEARCHPROPERTIES = new String[] { "name1", "name2", "company", "street", "country", "plz", "city",
            "comments", "workplace", "sallaryLong", "code", "contactPerson", "contactType",
            "contactReason", "lastContact", "skills", "gulpID" };

    private static final String[] ORDERBYPROPERTIES = new String[] { "name1", "name2" };
    
    @Override
    protected Freelancer createNew() {
        return new Freelancer();
    }

    @Override
    protected Class getEntityClass() {
        return Freelancer.class;
    }

    public ProfileSearchInfoDetail findByCode(final String aCode, final ProfileSearchRequest request) {
        return (ProfileSearchInfoDetail) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) throws SQLException {

                SimpleDateFormat theFormat = new SimpleDateFormat("dd.MM.yyyy");
                String theQueryString = "select item.name1, item.name2, item.availabilityAsDate, item.id , item.sallaryLong, item.country, item.plz from Freelancer item where item.code = :code ";
                if (request != null) {
                    Long theSallaryStart = request.getStundensatzVon();
                    if (theSallaryStart != null) {
                        theQueryString += "and item.sallaryLong >= " + theSallaryStart;
                    }

                    Long theSallaryEnd = request.getStundensatzBis();
                    if (theSallaryEnd != null) {
                        theQueryString += "and item.sallaryLong <= " + theSallaryEnd;
                    }

                    String thePlz = request.getPlz();
                    if (!StringUtils.isEmpty(thePlz)) {
                        theQueryString += "and item.plz like '" + thePlz + "'";
                    }

                }
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

    public List<GenericSearchResult> performQBESearch(Freelancer aObject, int aMaxSearchResult) {

        return performQBESearch(aObject, DISPLAYPROPERTIES, SEARCHPROPERTIES, ORDERBYPROPERTIES, MATCH_LIKE,
                aMaxSearchResult);
    }

    public List<String> getCodeSuggestions(final String aSuggest) {
        return (List<String>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) throws SQLException {
                List<String> theResult = new Vector<String>();

                Query theQuery = aSession.createQuery("select item.code from Freelancer item where item.code like '"
                        + aSuggest.trim() + "%') order by item.code");
                for (Iterator theIterator = theQuery.iterate(); theIterator.hasNext();) {
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
        return (Freelancer) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) {
                Query theQuery = aSession.createQuery("from Freelancer item where item.code = :code");
                theQuery.setString("code", aCode);
                for (Iterator theIt = theQuery.iterate(); theIt.hasNext();) {
                    return theIt.next();
                }
                return null;
            }
            
        });
    }

    public Collection<GenericSearchResult> performSearchByContact(String aContact, ContactType aContactType, int aMax) {
        return performSearchByContact(aContact, aContactType, DISPLAYPROPERTIES, ORDERBYPROPERTIES, aMax);
    }
}