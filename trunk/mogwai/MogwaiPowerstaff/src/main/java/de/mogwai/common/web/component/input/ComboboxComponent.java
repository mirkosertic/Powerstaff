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
 * Combobox component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:09:43 $
 */
public class ComboboxComponent extends ComboboxComponentBase {

    public static final String VALUES_BINDING_NAME = "values";

    public ComboboxComponent() {
    }

    public List getValues() {
        ValueBinding theBinding = getValueBinding(VALUES_BINDING_NAME);
        if (theBinding != null) {

            Object theResult = theBinding.getValue(FacesContext.getCurrentInstance());
            if (!(theResult instanceof List)) {
                throw new RuntimeException("Combobox can only be bound to java.util.List properties! Expression is "
                        + theBinding.getExpressionString());
            }

            return (List) theResult;
        }

        return null;
    }

    @Override
    public boolean isRequired() {
        return !isNullable();
    }

    @Override
    protected Object getConvertedValue(FacesContext aContext, Object aSubmittedValue) {

        // Wenn die Komponente ein Pflichtfeld ist, diese überprüfen
        if (isRequired()) {

            if ((aSubmittedValue == null) || ("".equals(aSubmittedValue))) {

                // Sie ist nicht gefüllt, also ist diese Komponente Invalid
                addMissingRequiredFieldMessage(aContext);

                setValid(false);
                return null;
            }
        }

        if ((aSubmittedValue == null) || ("".equals(aSubmittedValue))) {

            return null;

        }

        return aSubmittedValue;
    }

}