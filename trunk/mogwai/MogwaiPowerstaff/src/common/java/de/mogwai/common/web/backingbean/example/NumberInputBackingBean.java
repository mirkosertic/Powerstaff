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

import de.mogwai.common.web.backingbean.BackingBean;

public class NumberInputBackingBean extends BackingBean {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3245379088195751260L;

	private Integer intValue = 42;

    private Double doubleValue = 42.12;

    private Double formattedDoubleValue = 42.12;

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public Double getFormattedDoubleValue() {
        return formattedDoubleValue;
    }

    public void setFormattedDoubleValue(Double formattedDoubleValue) {
        this.formattedDoubleValue = formattedDoubleValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public String submit() {
        return null;
    }

}
