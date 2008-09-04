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
package de.mogwai.common.web.component.renderkit.html.input;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import de.mogwai.common.web.component.input.BaseInputComponent;
import de.mogwai.common.web.component.input.InputfieldComponent;

/**
 * Inputfield renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:24:57 $
 */
public class InputfieldRenderer extends BaseInputRenderer {

    public static final String ENABLED_CLASS = "mogwaiInputfield";

    public static final String DISABLED_CLASS = "mogwaiInputfieldDisabled";

    public InputfieldRenderer() {
    }

    @Override
    protected String getType(BaseInputComponent aComponent) {
        return aComponent.getType();
    }

    @Override
    public void decode(FacesContext aContext, UIComponent aComponent) {
        super.decode(aContext, aComponent);

        InputfieldComponent theBaseComponent = (InputfieldComponent) aComponent;

        if (isDisabledOrReadOnly(theBaseComponent)) {
            return;
        }

        Map theParamMap = aContext.getExternalContext().getRequestParameterMap();
        String clientId = theBaseComponent.getInputComponent().getClientId(aContext);

        if (theParamMap.containsKey(clientId)) {

            theBaseComponent.setValid(true);
            theBaseComponent.setSubmittedValue(theParamMap.get(clientId));

        }

        super.decode(aContext, aComponent);
    }

    @Override
    protected String getDisabledClass() {
        return DISABLED_CLASS;
    }

    @Override
    protected String getEnabledClass() {
        return ENABLED_CLASS;
    }
}