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

import java.util.Collection;
import java.util.List;

import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.dao.PartnerDAO;
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.Partner;

public class PartnerDAOHibernateImpl extends PersonDAOHibernateImpl<Partner> implements PartnerDAO {

    private static final String[] DISPLAYPROPERTIES = new String[] { "name1", "name2", "company", "city", "comments" };

    private static final String[] SEARCHPROPERTIES = new String[] { "name1", "name2", "company", "street", "country", "plz", "city",
            "comments", "kreditorNr", "debitorNr"};

    private static final String[] ORDERBYPROPERTIES = new String[] { "name1", "name2" };

    @Override
    protected Partner createNew() {
        return new Partner();
    }

    @Override
    protected Class getEntityClass() {
        return Partner.class;
    }

    public List<GenericSearchResult> performQBESearch(Partner aObject, int aMaxSearchResult) {
        return performQBESearch(aObject, DISPLAYPROPERTIES, SEARCHPROPERTIES, ORDERBYPROPERTIES, MATCH_LIKE,
                aMaxSearchResult);
    }

    public Collection<GenericSearchResult> performSearchByContact(String aContact, ContactType aContactType, int aMax) {
        return performSearchByContact(aContact, aContactType, DISPLAYPROPERTIES, ORDERBYPROPERTIES, aMax);
    }
}
