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

import javax.faces.component.StateHolder;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;

public class SaveStateComponent extends UIParameter {

    public static final String FAMILY_NAME = "javax.faces.Parameter";

    public static final String COMPONENT_TYPE = "SaveStateComponent";

    public static final String RENDERER_TYPE = null;

    @Override
    public String getFamily() {
        return FAMILY_NAME;
    }

    @Override
    public Object saveState(FacesContext aContext) {
        Vector<Object> theResult = new Vector<Object>();
        theResult.add(super.saveState(aContext));

        Object theValue = getValue();
        if (theValue != null) {
            if (theValue instanceof StateHolder) {
                StateHolder theHolder = (StateHolder) theValue;
                theResult.add(theHolder.saveState(aContext));
            } else {
                throw new RuntimeException(theValue + " is not a StateHolder!");
            }
        } else {
            theResult.add(null);
        }

        return theResult.toArray();
    }

    @Override
    public void restoreState(FacesContext aContext, Object aState) {
        Object[] theResult = (Object[]) aState;
        super.restoreState(aContext, theResult[0]);

        Object theValue = getValue();
        if (theValue != null) {
            if (theValue instanceof StateHolder) {
                StateHolder theHolder = (StateHolder) theValue;
                theHolder.restoreState(aContext, theResult[1]);
            } else {
                throw new RuntimeException(theValue + " is not a StateHolder!");
            }
        }
    }
}
