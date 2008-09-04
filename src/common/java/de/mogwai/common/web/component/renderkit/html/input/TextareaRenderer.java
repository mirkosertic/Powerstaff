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
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import de.mogwai.common.web.component.input.BaseInputComponent;
import de.mogwai.common.web.component.input.TextareaComponent;
import de.mogwai.common.web.utils.JSFJavaScriptFactory;
import de.mogwai.common.web.utils.JSFJavaScriptUtilities;

/**
 * Textarea renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:25:00 $
 */
public class TextareaRenderer extends BaseInputRenderer {

    public static final String ENABLED_CLASS = "mogwaiTextarea";

    public static final String DISABLED_CLASS = "mogwaiTextareaDisabled";

    public TextareaRenderer() {
    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {

        ResponseWriter theWriter = aContext.getResponseWriter();

        BaseInputComponent theComponent = (BaseInputComponent) aComponent;

        String theClientID = aComponent.getClientId(aContext);

        theWriter.startElement("div", aComponent);
        theWriter.writeAttribute("id", theClientID, null);

        HashMap<String, String> theStyles = new HashMap<String, String>();
        theStyles.put("position", "relative");

        setWidthIfInGridBag(theWriter, aComponent, theStyles);

        theWriter.startElement("textarea", aComponent);
        theWriter.writeAttribute("name", theClientID, null);
        theWriter.writeAttribute("class", getDisplayClass(theComponent), null);

        int theMaxLength = theComponent.getMaxLength();
        if (theMaxLength != -1) {
            theWriter.writeAttribute("onkeypress", "return validateTextAreaLength(this," + theMaxLength + ",event);",
                    null);
            theWriter.writeAttribute("onblur", "stripTextAreaLength(this," + theMaxLength + ",event);", null);
        }

        encodeSubmitEvent(aContext, theWriter, theComponent);

        encodeDisabledAttributes(theComponent, theWriter);

        setHeightIfInGridBag(theWriter, aComponent);

        String theValue = getStringDisplayValue(theComponent);
        if (theValue != null) {
            theWriter.writeText(theValue, null);
        }

        theWriter.endElement("textarea");

        renderInvalidMarker(theWriter, theComponent);

        theWriter.endElement("div");

        JSFJavaScriptUtilities theUtilities = JSFJavaScriptFactory.getJavaScriptUtilities(aContext);
        theUtilities.encodeSpecialFormSubmitHTML(aContext, theComponent, theWriter);
    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {
    }

    @Override
    public void decode(FacesContext aContext, UIComponent aComponent) {

        TextareaComponent theComponent = (TextareaComponent) aComponent;

        if (isDisabledOrReadOnly(theComponent)) {
            return;
        }

        Map theParamMap = aContext.getExternalContext().getRequestParameterMap();
        String clientId = aComponent.getClientId(aContext);

        if (theParamMap.containsKey(clientId)) {

            theComponent.setSubmittedValue(theParamMap.get(clientId));

        }

        super.decode(aContext, aComponent);
    }

    @Override
    protected String getType(BaseInputComponent aComponent) {
        return null;
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