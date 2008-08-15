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
package de.mogwai.common.web.component.taglib.jsp.input;

import javax.faces.component.UIComponent;

import de.mogwai.common.web.component.input.LabeledComponent;
import de.mogwai.common.web.component.input.LabeledComponentConstants;

/**
 * Base labeled component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:04:54 $
 */
public abstract class LabeledComponentTag extends BaseInputTag implements LabeledComponentConstants {

    private String label;

    private String key;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    protected void setProperties(UIComponent aComponent) {
        super.setProperties(aComponent);

        LabeledComponent theComponent = (LabeledComponent) aComponent;
        theComponent.setLabel(label);
        theComponent.setKey(key);
    }
}
