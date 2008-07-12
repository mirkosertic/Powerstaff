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
package de.mogwai.common.utils;

public class KeyValuePair<N, V> implements ObjectProvider, StringPresentationProvider {

    private N key;

    private V value;

    public KeyValuePair() {
    }

    public KeyValuePair(N aKey, V aValue) {
        key = aKey;
        value = aValue;
    }

    public Object getProvidedObject() {
        return key;
    }

    public String getStringPresentation() {
        return value.toString();
    }

    /**
     * @return the key
     */
    public N getKey() {
        return key;
    }

    /**
     * @param key
     *                the key to set
     */
    public void setKey(N key) {
        this.key = key;
    }

    /**
     * @return the value
     */
    public V getValue() {
        return value;
    }

    /**
     * @param value
     *                the value to set
     */
    public void setValue(V value) {
        this.value = value;
    }
}
