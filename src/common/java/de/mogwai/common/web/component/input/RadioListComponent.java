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
package de.mogwai.common.web.component.input;

import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * radio list component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:17:38 $
 */
public class RadioListComponent extends RadioListComponentBase {

    public static final String FLOW_HORIZONTAL = "horizontal";

    public static final String FLOW_VERTICAL = "vertical";

    public static final String VALUES_BINDING_NAME = "values";

    public RadioListComponent() {
    }

    public List getValues() {
        ValueBinding theBinding = getValueBinding(VALUES_BINDING_NAME);
        if (theBinding != null) {

            Object theResult = theBinding.getValue(FacesContext.getCurrentInstance());
            if ((theResult != null) && (!(theResult instanceof List))) {
                throw new RuntimeException("Combobox can only be bound to java.util.List properties! Expression is "
                        + theBinding.getExpressionString());
            }

            return (List) theResult;
        }

        return null;
    }

    /**
     * Radiobuttons that belong to a group must have the same name on client
     * side. This is forced by overriding this method.
     * 
     * @see javax.faces.component.UIComponentBase#getClientId(javax.faces.context.FacesContext)
     * @param aContext
     *                der Kontext
     * @return the client id
     */
    @Override
    public String getClientId(FacesContext aContext) {

        ValueBinding theBinding = getValueBinding("value");
        if (theBinding != null) {
            return super.getClientId(aContext) + "::" + theBinding.getExpressionString();
        }

        return super.getClientId(aContext);
    }
}