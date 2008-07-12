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
import java.util.List;
import java.util.Vector;

import de.mogwai.common.utils.StringPresentationProvider;
import de.mogwai.common.web.backingbean.BackingBean;

public class CheckboxlistInputBackingBean extends BackingBean {

    private List<String> value1 = new Vector<String>();

    private List<String> value2 = new Vector<String>();

    private List<ObjectWrapper> value3 = new Vector<ObjectWrapper>();

    private List<String> value4 = new Vector<String>();

    private List<String> value5 = new Vector<String>();

    private List<String> value6 = new Vector<String>();

    public static class ObjectWrapper implements StringPresentationProvider, Serializable {

        private String value;

        public ObjectWrapper(String aValue) {
            value = aValue;
        }

        public String getStringPresentation() {
            return value;
        }

        @Override
        public boolean equals(Object aObject) {
            if (aObject == null) {
                return false;
            }

            if (!(aObject instanceof ObjectWrapper)) {
                return false;
            }

            return value.equals(((ObjectWrapper) aObject).value);
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    private Vector<String> values = new Vector<String>();

    private Vector<ObjectWrapper> values2 = new Vector<ObjectWrapper>();

    public CheckboxlistInputBackingBean() {
        for (int i = 0; i < 4; i++) {
            values.add("Wert " + i);
            values2.add(new ObjectWrapper("Wert " + i));
        }

    }

    public String submit() {
        return null;
    }

    public List<String> getValue1() {
        return value1;
    }

    public void setValue1(List<String> value1) {
        this.value1 = value1;
    }

    public List<String> getValue2() {
        return value2;
    }

    public void setValue2(List<String> value2) {
        this.value2 = value2;
    }

    public List<ObjectWrapper> getValue3() {
        return value3;
    }

    public void setValue3(List<ObjectWrapper> value3) {
        this.value3 = value3;
    }

    public List<String> getValue4() {
        return value4;
    }

    public void setValue4(List<String> value4) {
        this.value4 = value4;
    }

    public List<String> getValue5() {
        return value5;
    }

    public void setValue5(List<String> value5) {
        this.value5 = value5;
    }

    public List<String> getValue6() {
        return value6;
    }

    public void setValue6(List<String> value6) {
        this.value6 = value6;
    }

    public Vector<String> getValues() {
        return values;
    }

    public void setValues(Vector<String> values) {
        this.values = values;
    }

    public Vector<ObjectWrapper> getValues2() {
        return values2;
    }

    public void setValues2(Vector<ObjectWrapper> values2) {
        this.values2 = values2;
    }
}
