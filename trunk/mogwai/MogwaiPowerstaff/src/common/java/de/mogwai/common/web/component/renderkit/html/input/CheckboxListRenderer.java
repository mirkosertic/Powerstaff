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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.beanutils.BeanUtils;

import de.mogwai.common.utils.ObjectProvider;
import de.mogwai.common.web.component.TableBuilder;
import de.mogwai.common.web.component.input.BaseInputComponent;
import de.mogwai.common.web.component.input.CheckboxListComponent;
import de.mogwai.common.web.component.input.ModalComponentUtils;
import de.mogwai.common.web.utils.JSFJavaScriptFactory;
import de.mogwai.common.web.utils.JSFJavaScriptUtilities;

/**
 * Checkbox list renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:25:12 $
 */
public class CheckboxListRenderer extends BaseInputRenderer {

    public static final String ENABLED_CLASS = "mogwaiCheckbox";

    public static final String DISABLED_CLASS = "mogwaiCheckboxDisabled";

    public static final String VALID_CLASS = "mogwaiCheckboxlistValid";

    public static final String INVALID_CLASS = "mogwaiCheckboxlistInvalid";

    public CheckboxListRenderer() {
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
        CheckboxListComponent theComponent = (CheckboxListComponent) aComponent;

        String theDisplay = theComponent.getDisplay();

        boolean isVertical = CheckboxListComponent.FLOW_VERTICAL.equals(theComponent.getFlow());

        Collection theSelectedItems = (Collection) ModalComponentUtils.getCurrentComponentValue(theComponent);

        theWriter.startElement("div", theComponent);
        theWriter.writeAttribute("id", aComponent.getClientId(aContext), null);
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
                theStringResponseWriter.writeAttribute("name", theComponent.getClientId(aContext) + "::" + count, null);

                theStringResponseWriter.writeAttribute("type", getType(theComponent), null);
                theStringResponseWriter.writeAttribute("value", "checked", null);
                theStringResponseWriter.writeAttribute("class", getDisplayClass(theComponent), null);

                encodeSubmitEvent(aContext, theStringResponseWriter, theComponent);

                encodeDisabledAttributes(theComponent, theStringResponseWriter);

                Object theCompareValue = theEntry;
                if (theCompareValue instanceof ObjectProvider) {
                    theCompareValue = ((ObjectProvider) theCompareValue).getProvidedObject();
                }

                // Wenn die aktuelle Collection diesen Wert enthält, so wird es
                // ausgewählt
                if ((theSelectedItems != null) && (theSelectedItems.contains(theCompareValue))) {
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

                if (isVertical) {
                    theWriter.startElement("br", theComponent);
                    theWriter.endElement("br");
                }

                theTableBuilder.endCell(count < theValues.size() - 1);

            }
        }

        theTableBuilder.end();
    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {

        ResponseWriter theWriter = aContext.getResponseWriter();

        theWriter.endElement("div");

        JSFJavaScriptUtilities theUtilities = JSFJavaScriptFactory.getJavaScriptUtilities(aContext);
        theUtilities.encodeSpecialFormSubmitHTML(aContext, aComponent, theWriter);
    }

    @Override
    @SuppressWarnings("all")
    public void decode(FacesContext aContext, UIComponent aComponent) {

        CheckboxListComponent theComponent = (CheckboxListComponent) aComponent;

        if (isDisabledOrReadOnly(theComponent)) {
            return;
        }

        Collection theSelectedItems = (Collection) theComponent.getValue();
        if (theSelectedItems != null) {
            Map theParamMap = aContext.getExternalContext().getRequestParameterMap();

            List theValues = theComponent.getValues();
            if (theValues != null) {
                for (int count = 0; count < theValues.size(); count++) {

                    Object theEntry = theValues.get(count);

                    Object theCompareValue = theEntry;
                    if (theCompareValue instanceof ObjectProvider) {
                        theCompareValue = ((ObjectProvider) theCompareValue).getProvidedObject();
                    }

                    String theClientID = aComponent.getClientId(aContext) + "::" + count;

                    if (theParamMap.containsKey(theClientID)) {
                        if (!theSelectedItems.contains(theCompareValue)) {
                            theSelectedItems.add(theCompareValue);
                        }
                    } else {
                        theSelectedItems.remove(theCompareValue);
                    }
                }
            }

            theComponent.setSubmittedValue(theSelectedItems);
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
        return "checkbox";
    }
}