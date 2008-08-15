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

import java.util.Vector;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;

import de.mogwai.common.web.component.common.FacetComponent;

/**
 * Hilfsklasse für den Umgabe mit UIData Komponenten.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:03:38 $
 */
public class ListAwareUtils {

    public static Vector<UIColumn> getColumns(UIData aComponent) {

        Vector<UIColumn> theColumns = new Vector<UIColumn>();
        for (int i = 0; i < aComponent.getChildCount(); i++) {
            UIComponent theComponent = (UIComponent) aComponent.getChildren().get(i);
            if ((theComponent instanceof UIColumn) && (!(theComponent instanceof FacetComponent))) {
                theColumns.add((UIColumn) theComponent);
            }
        }

        return theColumns;
    }

}
