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
package de.mogwai.common.business.entity;

/**
 * Geschäftsobjekt für einen Systemparameter (z.B. Adresse Mailserver)
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:16:36 $
 */
public class SystemParameter extends Entity {

    private String name;

    private String value;

    /**
     * Gibt den Wert des Attributs <code>name</code> zurück.
     * 
     * @return Wert des Attributs name.
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Wert des Attributs <code>name</code>.
     * 
     * @param name
     *                Wert für das Attribut name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Wert des Attributs <code>value</code> zurück.
     * 
     * @return Wert des Attributs value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Setzt den Wert des Attributs <code>value</code>.
     * 
     * @param value
     *                Wert für das Attribut value.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Convenience Methode für Zugriff auf "int-Value".
     * 
     * @param defaultValue
     *                Wert, der zurück gegeben wird, falls <code>value</code>
     *                nicht zu int geparst werden kann.
     * @return int Wert von <code>value</code> oder defaultValue, falls
     *         <code>value</code> nicht zu int geparst werden kann.
     */
    public int getIntValue(int defaultValue) {
        try {
            int intValue = Integer.parseInt(this.value);
            return intValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
