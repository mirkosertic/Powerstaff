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
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.beanutils.BeanUtils;

import de.mogwai.common.utils.ObjectProvider;
import de.mogwai.common.web.component.TableBuilder;
import de.mogwai.common.web.component.input.BaseInputComponent;
import de.mogwai.common.web.component.input.ModalComponentUtils;
import de.mogwai.common.web.component.input.RadioListComponent;
import de.mogwai.common.web.utils.JSFJavaScriptFactory;
import de.mogwai.common.web.utils.JSFJavaScriptUtilities;

/**
 * radio list renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:57 $
 */
public class RadioListRenderer extends BaseInputRenderer {

    public static final String ENABLED_CLASS = "mogwaiRadio";

    public static final String DISABLED_CLASS = "mogwaiRadioDisabled";

    public static final String VALID_CLASS = "mogwaiRadiolistValid";

    public static final String INVALID_CLASS = "mogwaiRadiolistInvalid";

    public RadioListRenderer() {
    }

    @Override
    protected void encodeDisabledAttributes(BaseInputComponent aComponent, ResponseWriter aWriter) throws IOException {
        if (aComponent.isDisabled()) {
            aWriter.writeAttribute("disabled", "disabled", null);
        }
    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {

        ResponseWriter theWriter = aContext.getResponseWriter();
        RadioListComponent theComponent = (RadioListComponent) aComponent;

        String theDisplay = theComponent.getDisplay();

        boolean isVertical = RadioListComponent.FLOW_VERTICAL.equals(theComponent.getFlow());

        Object theCurrentValue = ModalComponentUtils.getCurrentComponentValue(theComponent);

        theWriter.startElement("div", theComponent);
        theWriter.writeAttribute("class", theComponent.isValid() ? VALID_CLASS : INVALID_CLASS, null);

        TableBuilder theTableBuilder = new TableBuilder(theWriter, theComponent.getColumns(), theComponent);
        theTableBuilder.start();

        List theValues = theComponent.getValues();
        if (theValues != null) {
            for (int count = 0; count < theValues.size(); count++) {
                Object theEntry = theValues.get(count);

                StringWriter theStringWriter = new StringWriter();
                ResponseWriter theStringResponseWriter = theWriter.cloneWithWriter(theStringWriter);

                theStringResponseWriter.startElement("input", aComponent);
                theStringResponseWriter.writeAttribute("name", theComponent.getClientId(aContext), null);

                theStringResponseWriter.writeAttribute("type", getType(theComponent), null);
                theStringResponseWriter.writeAttribute("value", "" + count, null);
                theStringResponseWriter.writeAttribute("class", getDisplayClass(theComponent), null);

                encodeSubmitEvent(aContext, theStringResponseWriter, theComponent);

                encodeDisabledAttributes(theComponent, theStringResponseWriter);

                Object theCompareValue = theEntry;
                if (theCompareValue instanceof ObjectProvider) {
                    theCompareValue = ((ObjectProvider) theCompareValue).getProvidedObject();
                }

                if (theCompareValue.equals(theCurrentValue)) {
                    theStringResponseWriter.writeAttribute("checked", "checked", null);
                }

                theStringResponseWriter.endElement("input");

                theStringResponseWriter.flush();

                theTableBuilder.startCell();

                theWriter.write(theStringWriter.toString().trim());

                String theDisplayValue = null;

                if (theDisplay == null) {

                    theDisplayValue = getStringValue(aContext, theComponent, theEntry);

                } else {

                    Object theValue = null;

                    try {
                        theValue = BeanUtils.getProperty(theEntry, theDisplay);
                    } catch (Exception e) {
                        String theMessage = "Invalid display property for combobox component :" + theDisplay;

                        throw new RuntimeException(theMessage, e);
                    }
                    theDisplayValue = getStringValue(aContext, theComponent, theValue);

                }
                theWriter.write(theDisplayValue);

                if ((isVertical) && (!theTableBuilder.isMultiColumn())) {
                    theWriter.startElement("br", theComponent);
                    theWriter.endElement("br");
                }

                theTableBuilder.endCell(count < (theValues.size() - 1));

            }
        }

        theTableBuilder.end();

        theWriter.endElement("div");

        JSFJavaScriptUtilities theUtilities = JSFJavaScriptFactory.getJavaScriptUtilities(aContext);
        theUtilities.encodeSpecialFormSubmitHTML(aContext, theComponent, theWriter);
    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {
    }

    @Override
    public void decode(FacesContext aContext, UIComponent aComponent) {

        RadioListComponent theComponent = (RadioListComponent) aComponent;

        if (isDisabledOrReadOnly(theComponent)) {
            return;
        }

        List theValues = theComponent.getValues();
        if (theValues != null) {
            String theClientID = aComponent.getClientId(aContext);

            Map theParamMap = aContext.getExternalContext().getRequestParameterMap();

            if (theParamMap.containsKey(theClientID)) {

                String theValue = (String) theParamMap.get(theClientID);
                Object theNewValue;
                if (!"-1".equals(theValue)) {
                    theNewValue = theValues.get(Integer.parseInt(theValue));
                } else {
                    theNewValue = null;
                }

                if (theNewValue instanceof ObjectProvider) {
                    theNewValue = ((ObjectProvider) theNewValue).getProvidedObject();
                }

                theComponent.setSubmittedValue(theNewValue);

            } else {

                theComponent.setSubmittedValue("");
            }
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

    @Override
    protected String getType(BaseInputComponent aComponent) {
        return "radio";
    }
}