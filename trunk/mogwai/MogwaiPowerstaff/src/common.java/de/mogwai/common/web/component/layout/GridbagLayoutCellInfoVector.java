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

import java.util.Vector;

/**
 * Vector of CellInfo objects.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:04:25 $
 */
@SuppressWarnings("serial")
public class GridbagLayoutCellInfoVector extends Vector<GridbagLayoutCellInfo> {

    /**
     * Find a cell at a given position.
     * 
     * @param x
     *                the column
     * @param y
     *                the row
     * @return the cell, else null
     */
    public GridbagLayoutCellInfo findCellInfo(int x, int y) {

        for (GridbagLayoutCellInfo theCellInfo : this) {
            if ((x >= theCellInfo.getX()) && (y >= theCellInfo.getY())
                    && (x < theCellInfo.getX() + theCellInfo.getWidth())
                    && (y < theCellInfo.getY() + theCellInfo.getHeight())) {
                return theCellInfo;
            }

        }
        return null;
    }
}
