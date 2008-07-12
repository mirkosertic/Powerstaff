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

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import de.mogwai.common.web.component.input.BaseInputComponent;
import de.mogwai.common.web.component.input.CheckboxComponent;
import de.mogwai.common.web.utils.JSFJavaScriptFactory;
import de.mogwai.common.web.utils.JSFJavaScriptUtilities;

/**
 * Checkbox renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:58 $
 */
public class CheckboxRenderer extends BaseInputRenderer {

    public static final String ENABLED_CLASS = "mogwaiCheckbox";

    public static final String DISABLED_CLASS = "mogwaiCheckboxDisabled";

    public CheckboxRenderer() {
    }

    @Override
    protected void encodeDisabledAttributes(BaseInputComponent aComponent, ResponseWriter aWriter) throws IOException {
        if (aComponent.isDisabled()) {
            aWriter.writeAttribute("disabled", "disabled", null);
        }
    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {
        CheckboxComponent theComponent = (CheckboxComponent) aComponent;

        ResponseWriter theWriter = aContext.getResponseWriter();

        String theClientID = aComponent.getClientId(aContext);

        StringWriter theStringWriter = new StringWriter();
        ResponseWriter theStringResponseWriter = theWriter.cloneWithWriter(theStringWriter);

        theStringResponseWriter.startElement("input", aComponent);
        theStringResponseWriter.writeAttribute("id", theClientID, null);
        theStringResponseWriter.writeAttribute("name", theClientID, null);
        theStringResponseWriter.writeAttribute("value", "true", null);
        theStringResponseWriter.writeAttribute("type", getType(theComponent), null);
        theStringResponseWriter.writeAttribute("class", getDisplayClass(theComponent), null);

        encodeSubmitEvent(aContext, theStringResponseWriter, theComponent);

        encodeDisabledAttributes(theComponent, theWriter);

        if (theComponent.isSelected()) {
            theStringResponseWriter.writeAttribute("checked", "checked", null);
        }

        theStringResponseWriter.endElement("input");

        theStringResponseWriter.flush();

        theWriter.write(theStringWriter.toString().trim());

        theWriter.write(theComponent.getLabel());

        JSFJavaScriptUtilities theUtilities = JSFJavaScriptFactory.getJavaScriptUtilities(aContext);
        theUtilities.encodeSpecialFormSubmitHTML(aContext, theComponent, theWriter);
    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {
    }

    @Override
    public void decode(FacesContext aContext, UIComponent aComponent) {

        CheckboxComponent theCheckboxComponent = (CheckboxComponent) aComponent;

        if (isDisabledOrReadOnly(theCheckboxComponent)) {
            return;
        }

        Map theParamMap = aContext.getExternalContext().getRequestParameterMap();
        String theClientId = aComponent.getClientId(aContext);

        theCheckboxComponent.setSelected(theParamMap.containsKey(theClientId));

        super.decode(aContext, aComponent);
    }

    @Override
    protected String getType(BaseInputComponent aComponent) {
        return "checkbox";
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
