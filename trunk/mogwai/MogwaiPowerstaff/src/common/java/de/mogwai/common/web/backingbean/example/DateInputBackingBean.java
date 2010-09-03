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
package de.mogwai.common.web.backingbean.example;

import java.util.Date;

import de.mogwai.common.web.backingbean.BackingBean;

public class DateInputBackingBean extends BackingBean {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6009078012460453773L;

	private Date datum;

    private Date datum2;

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Date getDatum2() {
        return datum2;
    }

    public void setDatum2(Date datum2) {
        this.datum2 = datum2;
    }

    public String submit() {
        return null;
    }
}
