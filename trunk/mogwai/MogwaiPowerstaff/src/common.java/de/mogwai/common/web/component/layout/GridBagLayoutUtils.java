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
package de.mogwai.common.web.component.layout;

import javax.faces.component.UIComponent;

import de.mogwai.common.logging.Logger;

/**
 * The GridbagLayout component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:04:27 $
 */
public final class GridBagLayoutUtils {

    public static final int PREFFERED_SIZE = 21;

    public static final String PREFFERED_SIZE_MARKER = "p";

    public static final String INHERITED_SIZE_MARKER = "*";

    private static Logger logger = new Logger(GridBagLayoutUtils.class);

    private GridBagLayoutUtils() {
    }

    public static GridbagLayoutSizeDefinition getLayoutSizeDefinition(String aDefinition) {
        int p = aDefinition.indexOf(":");
        if (p < 0) {

            if (PREFFERED_SIZE_MARKER.equals(aDefinition)) {
                aDefinition = "" + PREFFERED_SIZE;
            }

            return new GridbagLayoutSizeDefinition(null, aDefinition);
        } else {
            String theAlign = aDefinition.substring(0, p);
            String theDefinition = aDefinition.substring(p + 1);

            if (PREFFERED_SIZE_MARKER.equals(theDefinition)) {
                theDefinition = "" + PREFFERED_SIZE;
            }

            return new GridbagLayoutSizeDefinition(theAlign, theDefinition);
        }
    }

    public static GridbagLayoutCellComponent findSurroundingCell(UIComponent aParent) {
        if (aParent instanceof GridbagLayoutCellComponent) {
            return (GridbagLayoutCellComponent) aParent;
        }
        aParent = aParent.getParent();
        if (aParent != null) {
            return findSurroundingCell(aParent);
        }
        return null;
    }

    public static String getSizeForColumn(UIComponent aParent, GridbagLayoutSizeDefinitionVector aCols, int aColumn) {

        GridbagLayoutSizeDefinition theColDef = aCols.get(aColumn);
        String theWidth = theColDef.getSize();

        if (theColDef.isInheritedSize()) {
            GridbagLayoutCellComponent theSurroundingCell = findSurroundingCell(aParent);
            if (theSurroundingCell != null) {
                int theTotalWidth = aCols.getTotalSizeWithoutStar();
                theWidth = "" + (theSurroundingCell.computeWidth() - theTotalWidth);
            } else {
                logger.logWarning("Gridbaglayout verwendet * ohne umgebende Gridbaglayout - Zelle");
            }
        }

        return theWidth;
    }
}
