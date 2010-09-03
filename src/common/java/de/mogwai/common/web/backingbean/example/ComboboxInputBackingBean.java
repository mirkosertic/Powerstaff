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

import java.util.Vector;

import de.mogwai.common.web.backingbean.BackingBean;
import de.mogwai.common.web.component.input.ComboboxComponent;

public class ComboboxInputBackingBean extends BackingBean {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8679197584384874789L;

	private String value1;

    private String value2;

    private String value3;

    private String value4;

    private TestDTO value5;

    private ComboboxComponent combobox;

    private Vector<String> values = new Vector<String>();

    private Vector<String> emptyValues = new Vector<String>();

    private Vector<TestDTO> dtoValues = new Vector<TestDTO>();

    public ComboboxInputBackingBean() {
        for (int i = 0; i < 10; i++) {
            values.add("Wert " + i);
            dtoValues.add(new TestDTO(i));
        }

        emptyValues.add(null);
        emptyValues.add("Lala");
    }

    public String change() {
        System.out.println(combobox.getValue());
        return null;
    }

    /**
     * Gibt den Wert des Attributs <code>combobox</code> zurück.
     * 
     * @return Wert des Attributs combobox.
     */
    public ComboboxComponent getCombobox() {
        return null;
    }

    /**
     * Setzt den Wert des Attributs <code>combobox</code>.
     * 
     * @param combobox
     *                Wert für das Attribut combobox.
     */
    public void setCombobox(ComboboxComponent combobox) {
        this.combobox = combobox;
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

    public Vector<String> getValues() {
        return values;
    }

    public void setValues(Vector<String> values) {
        this.values = values;
    }

    public String submit() {
        return null;
    }

    /**
     * Gibt den Wert des Attributs <code>emptyValues</code> zurück.
     * 
     * @return Wert des Attributs emptyValues.
     */
    public Vector<String> getEmptyValues() {
        return emptyValues;
    }

    /**
     * Setzt den Wert des Attributs <code>emptyValues</code>.
     * 
     * @param emptyValues
     *                Wert für das Attribut emptyValues.
     */
    public void setEmptyValues(Vector<String> emptyValues) {
        this.emptyValues = emptyValues;
    }

    /**
     * Gibt den Wert des Attributs <code>value4</code> zurück.
     * 
     * @return Wert des Attributs value4.
     */
    public String getValue4() {
        return value4;
    }

    /**
     * Setzt den Wert des Attributs <code>value4</code>.
     * 
     * @param value4
     *                Wert für das Attribut value4.
     */
    public void setValue4(String value4) {
        this.value4 = value4;
    }

    /**
     * Gibt den Wert des Attributs <code>dtoValues</code> zurück.
     * 
     * @return Wert des Attributs dtoValues.
     */
    public Vector<TestDTO> getDtoValues() {
        return dtoValues;
    }

    /**
     * Setzt den Wert des Attributs <code>dtoValues</code>.
     * 
     * @param dtoValues
     *                Wert für das Attribut dtoValues.
     */
    public void setDtoValues(Vector<TestDTO> dtoValues) {
        this.dtoValues = dtoValues;
    }

    /**
     * Gibt den Wert des Attributs <code>value5</code> zurück.
     * 
     * @return Wert des Attributs value5.
     */
    public TestDTO getValue5() {
        return value5;
    }

    /**
     * Setzt den Wert des Attributs <code>value5</code>.
     * 
     * @param value5
     *                Wert für das Attribut value5.
     */
    public void setValue5(TestDTO value5) {
        this.value5 = value5;
    }
}
