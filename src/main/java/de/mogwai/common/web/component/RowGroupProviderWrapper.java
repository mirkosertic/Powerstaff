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
package de.mogwai.common.web.component;

/**
 * Helper class to build grouped lists easily.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:09:55 $
 * @param <G>
 *                Typ des Gruppierungsobjektes
 * @param <T>
 *                Typ des Datenobjektes
 */
public class RowGroupProviderWrapper<G, T> implements RowGroupProvider<G> {

    private G groupObject;

    private T valueObject;

    public RowGroupProviderWrapper(G aGroupObject, T aValueObject) {
        groupObject = aGroupObject;
        valueObject = aValueObject;
    }

    public G getGroupingObject() {
        return groupObject;
    }

    public T getValue() {
        return valueObject;
    }
}