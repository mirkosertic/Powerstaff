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

import de.mogwai.common.web.backingbean.BackingBeanDataModel;

public class MultiPageDataModel extends BackingBeanDataModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5470274594852268572L;

	private String value1;

    private String value2;

    private String value3;

    /**
     * Gibt den Wert des Attributs <code>value1</code> zurück.
     * 
     * @return Wert des Attributs value1.
     */
    public String getValue1() {
        return value1;
    }

    /**
     * Setzt den Wert des Attributs <code>value1</code>.
     * 
     * @param value1
     *                Wert für das Attribut value1.
     */
    public void setValue1(String value1) {
        this.value1 = value1;
    }

    /**
     * Gibt den Wert des Attributs <code>value2</code> zurück.
     * 
     * @return Wert des Attributs value2.
     */
    public String getValue2() {
        return value2;
    }

    /**
     * Setzt den Wert des Attributs <code>value2</code>.
     * 
     * @param value2
     *                Wert für das Attribut value2.
     */
    public void setValue2(String value2) {
        this.value2 = value2;
    }

    /**
     * Gibt den Wert des Attributs <code>value3</code> zurück.
     * 
     * @return Wert des Attributs value3.
     */
    public String getValue3() {
        return value3;
    }

    /**
     * Setzt den Wert des Attributs <code>value3</code>.
     * 
     * @param value3
     *                Wert für das Attribut value3.
     */
    public void setValue3(String value3) {
        this.value3 = value3;
    }

}
