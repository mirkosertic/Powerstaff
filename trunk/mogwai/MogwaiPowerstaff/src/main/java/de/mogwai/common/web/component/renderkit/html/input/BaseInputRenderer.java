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

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;

import de.mogwai.common.utils.StringPresentationProvider;
import de.mogwai.common.web.component.input.BaseInputComponent;
import de.mogwai.common.web.component.input.ModalComponentUtils;
import de.mogwai.common.web.component.renderkit.html.BaseRenderer;
import de.mogwai.common.web.utils.JSFJavaScriptFactory;
import de.mogwai.common.web.utils.JSFJavaScriptUtilities;

/**
 * Inputfield renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:58 $
 */
public abstract class BaseInputRenderer extends BaseRenderer {

    protected static final String REQUIRED_MARKER_CLASS = "mogwaiRequiredMarker";

    protected static final String INVALID_MARKER_CLASS = "mogwaiInvalidMarker";

    protected BaseInputRenderer() {
    }

    protected boolean isDisabledOrReadOnly(BaseInputComponent aComponent) {
        return aComponent.isDisabled();
    }

    protected abstract String getType(BaseInputComponent aComponent);

    protected abstract String getEnabledClass();

    protected abstract String getDisabledClass();

    protected final String getDisplayClass(BaseInputComponent aComponent) {
        if (aComponent.isDisabled()) {
            return getDisabledClass();
        }

        return getEnabledClass();
    }

    protected void renderInvalidMarker(ResponseWriter aWriter, BaseInputComponent aComponent) throws IOException {

        if (!aComponent.isValid()) {
            aWriter.startElement("div", aComponent);
            aWriter.writeAttribute("class", INVALID_MARKER_CLASS, null);
            aWriter.write("!");
            aWriter.endElement("div");
        } else {
            aWriter.startElement("div", aComponent);
            aWriter.writeAttribute("class", REQUIRED_MARKER_CLASS, null);
            aWriter.endElement("div");
        }
    }

    protected void encodeSubmitEvent(FacesContext aContext, ResponseWriter aWriter, BaseInputComponent aComponent)
            throws IOException {
        if (aComponent.isSubmitOnChange()) {

            JSFJavaScriptUtilities theUtilities = JSFJavaScriptFactory.getJavaScriptUtilities(aContext);

            String theAttribute = aComponent.getSubmitEvent();
            String theValue = theUtilities.getJavaScriptSubmitCommand(aContext, aComponent.getCommandComponent(),
                    aComponent.getSubmitEvent());

            aWriter.writeAttribute(theAttribute, theValue, null);
        }
    }

    protected void encodeDisabledAttributes(BaseInputComponent aComponent, ResponseWriter aWriter) throws IOException {
        if (aComponent.isDisabled()) {
            aWriter.writeAttribute("readonly", "readonly", null);
        }
    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {
        ResponseWriter theWriter = aContext.getResponseWriter();

        BaseInputComponent theComponent = (BaseInputComponent) aComponent;

        String theClientID = aComponent.getClientId(aContext);

        String theInputClientId = theComponent.getInputComponent().getClientId(aContext);

        theWriter.startElement("div", aComponent);
        theWriter.writeAttribute("id", theClientID, null);
        theWriter.writeAttribute("style", "position:relative;", null);
        theWriter.startElement("input", aComponent);
        theWriter.writeAttribute("id", theInputClientId, null);

        String theType = getType(theComponent);
        if (theType != null) {
            theWriter.writeAttribute("type", theType, null);
        }

        theWriter.writeAttribute("name", theInputClientId, null);

        if (theComponent.isRedisplay()) {
            theWriter.writeAttribute("value", getStringDisplayValue(theComponent), "value");
        }

        theWriter.writeAttribute("class", getDisplayClass(theComponent), null);

        int theMaxLength = theComponent.getMaxLength();
        if (theMaxLength != -1) {
            theWriter.writeAttribute("size", "" + theMaxLength, null);
            theWriter.writeAttribute("maxlength", "" + theMaxLength, null);
        }

        encodeDisabledAttributes(theComponent, theWriter);

        encodeSubmitEvent(aContext, theWriter, theComponent);

        setWidthIfInGridBag(theWriter, aComponent, null);
    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {

        ResponseWriter theWriter = aContext.getResponseWriter();

        BaseInputComponent theComponent = (BaseInputComponent) aComponent;

        theWriter.endElement("input");

        renderInvalidMarker(theWriter, theComponent);

        theWriter.endElement("div");
    }

    public String getStringDisplayValue(UIInput aInputComponent) {

        Object theValue = ModalComponentUtils.getCurrentComponentValue(aInputComponent);

        if (theValue == null) {
            return "";
        }

        Converter theConverter = aInputComponent.getConverter();
        if (theConverter != null) {
            return theConverter.getAsString(FacesContext.getCurrentInstance(), aInputComponent, theValue);
        }

        if (theValue instanceof StringPresentationProvider) {
            return ((StringPresentationProvider) theValue).getStringPresentation();
        }

        return theValue.toString();
    }
}
