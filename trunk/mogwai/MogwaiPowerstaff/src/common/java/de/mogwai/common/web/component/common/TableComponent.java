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
package de.mogwai.common.web.component.common;

import java.util.Vector;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import de.mogwai.common.web.component.DynamicContentComponent;
import de.mogwai.common.web.component.layout.GridbagLayoutSizeDefinitionVector;

/**
 * Table component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:32:13 $
 */
public class TableComponent extends TableComponentBase implements DynamicContentComponent {

    public static final String FUNCTIONS_FACET = "functions";

    public static final String NODATA_FACET = "noData";

    public static final String EMPTYCELL_FACET = "emptyCell";

    public static final String GROUPHEADER_FACET = "groupHeader";

    public static final String HEADER_VAR_ATTRIBUTE = "groupHeader";

    private GridbagLayoutSizeDefinitionVector cols;

    public TableComponent() {
    }

    public GridbagLayoutSizeDefinitionVector getCols() {
        return cols;
    }

    public void setCols(GridbagLayoutSizeDefinitionVector cols) {
        this.cols = cols;
    }

    public void setFunctions(UIComponent aFunctions) {
        getFacets().put(FUNCTIONS_FACET, aFunctions);
    }

    public UIComponent getFunctions() {
        return (UIComponent) getFacets().get(FUNCTIONS_FACET);
    }

    public UIComponent getNoData() {
        return getFacet(NODATA_FACET);
    }

    public void setNoData(UIComponent aFunctions) {
        getFacets().put(NODATA_FACET, aFunctions);
    }

    public boolean hasFunctions() {
        return getFunctions() != null;
    }

    @Override
    public void restoreState(FacesContext aContext, Object aState) {

        Object[] theState = (Object[]) aState;

        super.restoreState(aContext, theState[0]);
        cols = (GridbagLayoutSizeDefinitionVector) theState[1];
    }

    @Override
    public Object saveState(FacesContext aContext) {

        Vector<Object> theState = new Vector<Object>();
        theState.add(super.saveState(aContext));
        theState.add(cols);
        return theState.toArray();
    }

}
