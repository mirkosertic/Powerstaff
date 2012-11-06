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
package de.powerstaff.business.entity;

import java.util.HashMap;
import java.util.Map;

public class Customer extends Person<CustomerContact, CustomerHistory> implements UDFSupport {

    /**
     *
     */
    private static final long serialVersionUID = -246495433294061147L;
    private Map<String, UserDefinedField> udf = new HashMap<String, UserDefinedField>();

    public Customer() {
    }

    public Map<String, UserDefinedField> getUdf() {
        return udf;
    }

    public void setUdf(Map<String, UserDefinedField> udf) {
        this.udf = udf;
    }
}