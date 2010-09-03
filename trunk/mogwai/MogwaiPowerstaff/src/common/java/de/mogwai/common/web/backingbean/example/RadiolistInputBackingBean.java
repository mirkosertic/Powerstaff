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

import java.io.Serializable;
import java.util.Vector;

import de.mogwai.common.utils.StringPresentationProvider;
import de.mogwai.common.web.backingbean.BackingBean;

public class RadiolistInputBackingBean extends BackingBean {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4151846732553192513L;

	private String value1;

    private String value2;

    private String value4;

    private String value5;

    private String value6;

    private ObjectWrapper value3;

    public static class ObjectWrapper implements StringPresentationProvider, Serializable {

        /**
		 * 
		 */
		private static final long serialVersionUID = -519723083794411837L;
		private String value;

        public ObjectWrapper(String aValue) {
            value = aValue;
        }

        public String getStringPresentation() {
            return value;
        }
    }

    private Vector<String> values = new Vector<String>();

    private Vector<ObjectWrapper> values2 = new Vector<ObjectWrapper>();

    public RadiolistInputBackingBean() {
        for (int i = 0; i < 4; i++) {
            values.add("Wert " + i);
            values2.add(new ObjectWrapper("Wert " + i));
        }

    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    /**
     * Gibt den Wert des Attributs <code>value3</code> zurück.
     * 
     * @return Wert des Attributs value3.
     */
    public ObjectWrapper getValue3() {
        return value3;
    }

    /**
     * Setzt den Wert des Attributs <code>value3</code>.
     * 
     * @param value3
     *                Wert für das Attribut value3.
     */
    public void setValue3(ObjectWrapper value3) {
        this.value3 = value3;
    }

    /**
     * Gibt den Wert des Attributs <code>values2</code> zurück.
     * 
     * @return Wert des Attributs values2.
     */
    public Vector<ObjectWrapper> getValues2() {
        return values2;
    }

    /**
     * Setzt den Wert des Attributs <code>values2</code>.
     * 
     * @param values2
     *                Wert für das Attribut values2.
     */
    public void setValues2(Vector<ObjectWrapper> values2) {
        this.values2 = values2;
    }

    public Vector<String> getValues() {
        return values;
    }

    public void setValues(Vector<String> values) {
        this.values = values;
    }

    public String submit() {
        return null;
    }

    public String getValue4() {
        return value4;
    }

    public void setValue4(String value4) {
        this.value4 = value4;
    }

    public String getValue5() {
        return value5;
    }

    public void setValue5(String value5) {
        this.value5 = value5;
    }

    public String getValue6() {
        return value6;
    }

    public void setValue6(String value6) {
        this.value6 = value6;
    }

}
