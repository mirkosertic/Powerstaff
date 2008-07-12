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

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;

import de.mogwai.common.web.component.common.FacetComponent;

/**
 * Base list aware component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:09:55 $
 */
public class ListawareComponent extends UIData {

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        setRowIndex(-1);

        super.encodeBegin(context);
    }

    public FacetComponent getMogwaiFacet(String aName) {

        for (int i = 0; i < getChildCount(); i++) {
            UIComponent theComponent = (UIComponent) getChildren().get(i);
            if ((theComponent instanceof FacetComponent)) {
                FacetComponent theFacetComponent = (FacetComponent) theComponent;
                if (theFacetComponent.getName().equals(aName)) {
                    return theFacetComponent;
                }
            }

        }

        return null;
    }
}
