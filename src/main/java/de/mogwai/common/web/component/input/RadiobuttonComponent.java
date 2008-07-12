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

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Radiobutton component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:09:46 $
 */
public class RadiobuttonComponent extends RadiobuttonComponentBase {

    public RadiobuttonComponent() {
    }

    @Override
    public boolean isSelected() {

        ValueBinding theValueBinding = getValueBinding("value");

        String theSelectedIf = getSelectedif();
        if ((theValueBinding != null) && (theSelectedIf != null)) {

            String theStringResult = null;
            Object theResult = ModalComponentUtils.getCurrentComponentValue(this);
            if (theResult != null) {
                theStringResult = theResult.toString();
            }

            return theSelectedIf.equals(theStringResult);
        }

        return false;
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
            return "radio::" + theBinding.getExpressionString();
        }

        return super.getClientId(aContext);
    }
}
