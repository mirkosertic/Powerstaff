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
package de.mogwai.common.web.utils;

import java.io.Serializable;
import java.util.Map;

/**
 * Eintrag für ein MapDataModel.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:09:52 $
 */
@SuppressWarnings("serial")
public class MapDataModelEntry implements Serializable {

    private Map owner;

    private Object key;

    public MapDataModelEntry(Map aOwner, Object aKey) {
        owner = aOwner;
        key = aKey;
    }

    /**
     * Gibt den Wert des Attributs <code>key</code> zurück.
     * 
     * @return Wert des Attributs key.
     */
    public Object getKey() {
        return key;
    }

    /**
     * Setzt den Wert des Attributs <code>key</code>.
     * 
     * @param key
     *                Wert für das Attribut key.
     */
    public void setKey(Object key) {
        this.key = key;
    }

    /**
     * Ermittlung des Wertes.
     * 
     * @return der Wert
     */
    public Object getValue() {
        return owner.get(key);
    }

    /**
     * Setzen des Wertes.
     * 
     * @param aValue
     *                der Wert
     */
    public void setValue(Object aValue) {
        owner.put(key, aValue);
    }

}
