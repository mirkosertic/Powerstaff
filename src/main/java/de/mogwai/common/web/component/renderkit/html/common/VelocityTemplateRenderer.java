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
package de.mogwai.common.web.component.renderkit.html.common;

import java.io.IOException;
import java.util.HashMap;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import de.mogwai.common.web.component.common.VelocityTemplateComponent;
import de.mogwai.common.web.component.renderkit.html.VelocityRenderer;

public class VelocityTemplateRenderer extends VelocityRenderer {

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {

        VelocityTemplateComponent theComponent = (VelocityTemplateComponent) aComponent;

        HashMap<String, Object> theValues = new HashMap<String, Object>();

        for (Object theKey : theComponent.getFacets().keySet()) {
            String theName = (String) theKey;
            theValues.put(theName, renderComponentToString(aContext, theComponent.getFacet(theName)));
        }

        try {
            renderTemplate(theValues, theComponent.getTemplate(), aContext.getResponseWriter());
        } catch (Exception e) {
            throw new RuntimeException("Error generating template", e);
        }
    }
}
