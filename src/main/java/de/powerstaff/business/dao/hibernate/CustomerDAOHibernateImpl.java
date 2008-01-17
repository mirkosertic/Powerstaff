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

import java.util.List;

import de.powerstaff.business.dao.CustomerDAO;
import de.powerstaff.business.entity.Customer;

public class CustomerDAOHibernateImpl extends NavigatingDAOHibernateImpl<Customer> implements CustomerDAO {

    @Override
    protected Customer createNew() {
        return new Customer();
    }

    @Override
    protected Class getEntityClass() {
        return Customer.class;
    }

    public List<Customer> performQBESearch(Customer aObject) {

        String[] theSearchProperties = new String[] { "name1", "name2", "company", "street", "country", "plz", "city",
                "comments", };

        String[] theOrderByProperties = new String[] { "name1", "name2" };

        return performQBESearch(aObject, theSearchProperties, theOrderByProperties, MATCH_LIKE);
    }
}
