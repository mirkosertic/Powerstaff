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
import de.powerstaff.business.entity.Freelancer;

public class FreelancerDAOHibernateImpl extends NavigatingDAOHibernateImpl<Freelancer> implements FreelancerDAO {

    @Override
    protected Freelancer createNew() {
        return new Freelancer();
    }

    @Override
    protected Class getEntityClass() {
        return Freelancer.class;
    }

    public ProfileSearchInfoDetail findByCode(final String aCode) {
        return (ProfileSearchInfoDetail) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) throws SQLException {
                
                SimpleDateFormat theFormat = new SimpleDateFormat("dd.MM.yyyy");
                Query theQuery = aSession
                        .createQuery("select item.name1, item.name2, item.availabilityAsDate, item.id , item.sallaryLong, item.country, item.plz from Freelancer item where item.code = :code");
                theQuery.setString("code", aCode);
                Iterator theIterator = theQuery.list().iterator();
                if (theIterator.hasNext()) {
                    Object[] theRow = (Object[]) theIterator.next();

                    ProfileSearchInfoDetail theDetail = new ProfileSearchInfoDetail();
                    theDetail.setName1((String) theRow[0]);
                    theDetail.setName2((String) theRow[1]);
                    
                    Date theAvailable = (Date) theRow[2];
                    if (theAvailable != null) {
                        theDetail.setAvailability(theFormat.format(theAvailable));
                    }
                    theDetail.setId((Long) theRow[3]);
                    theDetail.setStundensatz((Long) theRow[4]);

                    String theCountry = (String) theRow[5];
                    String thePlz = (String) theRow[6];
                    if (!StringUtils.isEmpty(thePlz)) {
                        if (StringUtils.isEmpty(theCountry)) {
                            theDetail.setPlz(thePlz);
                        } else {
                            theDetail.setPlz(theCountry + "-" + thePlz);
                        }
                    }

                    return theDetail;
                }

                return null;
            }

        });
    }

    public List<GenericSearchResult> performQBESearch(Freelancer aObject, int aMaxSearchResult) {

        String[] theDisplayProperties = new String[] { "name1", "name2", "availability", "sallary", "skills" };

        String[] theSearchProperties = new String[] { "name1", "name2", "company", "street", "country", "plz", "city",
                "comments", "workplace", "availability", "sallary", "code", "contactPerson", "contactType",
                "contactReason", "lastContact", "skills", "gulpID" };

        String[] theOrderByProperties = new String[] { "name1", "name2" };

        return performQBESearch(aObject, theDisplayProperties, theSearchProperties, theOrderByProperties, MATCH_LIKE,
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

}
