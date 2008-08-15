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

/**
 * Checkbox component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:09:50 $
 */
public class CheckboxComponent extends CheckboxComponentBase {

    public CheckboxComponent() {
    }

    public boolean isSelected() {

        Object theValue = ModalComponentUtils.getCurrentComponentValue(this);
        if (theValue != null) {

            boolean theBooleanValue = false;
            if (theValue instanceof Boolean) {
                theBooleanValue = ((Boolean) theValue).booleanValue();
            } else {
                throw new RuntimeException("Checkboxes can only be bound to boolean properties!");
            }

            return theBooleanValue;
        }

        return false;

    }

    public void setSelected(boolean aSelected) {
        setSubmittedValue(aSelected ? Boolean.TRUE : Boolean.FALSE);
    }
}
