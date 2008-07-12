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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * The GridbagLayout component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:33 $
 */
public class GridbagLayoutComponent extends GridbagLayoutComponentBase {

    private GridbagLayoutSizeDefinitionVector cols;

    private GridbagLayoutSizeDefinitionVector rows;

    public GridbagLayoutComponent() {
    }

    protected GridbagLayoutCellComponent findNestingCellComponent() {
        UIComponent theComponent = getParent();
        while (theComponent != null) {

            if (theComponent instanceof GridbagLayoutCellComponent) {
                return (GridbagLayoutCellComponent) theComponent;
            }

            theComponent = theComponent.getParent();
        }

        return null;
    }

    public int computeWidth(int aStart, int aWidth) {

        GridbagLayoutSizeDefinitionVector theDefVector = getCols();

        int theSize = 0;

        for (int i = aStart - 1; i < aStart + aWidth - 1; i++) {
            GridbagLayoutSizeDefinition theDef = theDefVector.get(i);

            String theStrSize = theDef.getSize();
            if (!GridBagLayoutUtils.INHERITED_SIZE_MARKER.equals(theStrSize)) {
                theSize += Integer.parseInt(theStrSize);
            } else {

                GridbagLayoutCellComponent theParent = findNestingCellComponent();
                int theParentWidth = theParent.computeWidth();

                for (GridbagLayoutSizeDefinition theDefinition : theDefVector) {
                    theStrSize = theDefinition.getSize();
                    if (!GridBagLayoutUtils.INHERITED_SIZE_MARKER.equals(theStrSize)) {
                        theParentWidth -= Integer.parseInt(theStrSize);
                    }
                }

                theSize += theParentWidth;
            }
        }

        return theSize;

    }

    public int computeHeight(int aStart, int aHeight) {

        GridbagLayoutSizeDefinitionVector theDefVector = getRows();

        int theSize = 0;

        for (int i = aStart - 1; i < aStart + aHeight - 1; i++) {
            GridbagLayoutSizeDefinition theDef = theDefVector.get(i);

            theSize += Integer.parseInt(theDef.getSize());
        }

        return theSize;

    }

    public GridbagLayoutSizeDefinitionVector getCols() {
        return cols;
    }

    public void setCols(GridbagLayoutSizeDefinitionVector cols) {
        this.cols = cols;
    }

    public GridbagLayoutSizeDefinitionVector getRows() {
        return rows;
    }

    public void setRows(GridbagLayoutSizeDefinitionVector rows) {
        this.rows = rows;
    }

    @Override
    public void restoreState(FacesContext aContext, Object aState) {

        Object[] theState = (Object[]) aState;

        super.restoreState(aContext, theState[0]);
        cols = (GridbagLayoutSizeDefinitionVector) theState[1];
        rows = (GridbagLayoutSizeDefinitionVector) theState[2];
    }

    @Override
    public Object saveState(FacesContext aContext) {

        Vector<Object> theState = new Vector<Object>();
        theState.add(super.saveState(aContext));
        theState.add(cols);
        theState.add(rows);
        return theState.toArray();
    }
}