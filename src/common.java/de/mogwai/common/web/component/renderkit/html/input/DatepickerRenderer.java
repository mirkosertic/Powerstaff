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
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import de.mogwai.common.web.ResourceBundleManager;
import de.mogwai.common.web.component.input.BaseInputComponent;
import de.mogwai.common.web.component.input.DatepickerComponent;
import de.mogwai.common.web.component.input.ModalComponentUtils;
import de.mogwai.common.web.utils.JSFJavaScriptFactory;
import de.mogwai.common.web.utils.JSFJavaScriptUtilities;

/**
 * Datepicker renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:12:00 $
 */
public class DatepickerRenderer extends BaseInputRenderer {

    private static final String DATE_FORMAT_RESOURCE_NAME = "global.dateformat.short";

    private static final String VALIDATION_ERROR = "validator.invaliddate";

    private static final String ENABLED_CLASS = "w8em divider-dot format-d-m-y no-transparency mogwaiDatePicker";

    private static final String DISABLED_CLASS = "mogwaiDatePickerDisabled";

    public DatepickerRenderer() {
    }

    @Override
    protected String getType(BaseInputComponent aComponent) {
        return "text";
    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {

        ResponseWriter theWriter = aContext.getResponseWriter();

        DatepickerComponent theComponent = (DatepickerComponent) aComponent;
        String theValue;
        if (theComponent.isValid()) {
            ResourceBundle theBundle = ResourceBundleManager.getBundle();
            SimpleDateFormat theFormat = new SimpleDateFormat(theBundle.getString(DATE_FORMAT_RESOURCE_NAME));

            Object theObjectValue = ModalComponentUtils.getCurrentComponentValue(theComponent);

            Converter theConverter = theComponent.getConverter();
            if (theConverter == null) {

                // Wenn es keinen Converter gibt, so wird der Datentyp Date
                // angenommen
                if (theObjectValue instanceof Date) {
                    Date theDateValue = (Date) theObjectValue;

                    if (theDateValue == null) {
                        theValue = "";
                    } else {
                        theValue = theFormat.format(theDateValue);
                    }
                } else {
                    theValue = (String) theObjectValue;
                }
            } else {
                // Wenn es einen Converter gibt, so soll dieser den Job
                // übernehmen
                theValue = theConverter.getAsString(aContext, aComponent, theObjectValue);
            }

        } else {
            theValue = (String) theComponent.getSubmittedValue();
        }

        String theClientID = aComponent.getClientId(aContext);

        theWriter.startElement("div", aComponent);

        HashMap<String, String> theStyles = new HashMap<String, String>();
        theStyles.put("position", "relative");

        setWidthIfInGridBag(theWriter, aComponent, theStyles);

        theWriter.startElement("input", aComponent);
        theWriter.writeAttribute("type", getType(theComponent), null);
        theWriter.writeAttribute("name", theClientID, null);
        theWriter.writeAttribute("id", theClientID, null);
        theWriter.writeAttribute("size", "10", null);
        theWriter.writeAttribute("value", theValue, "value");
        theWriter.writeAttribute("class", getDisplayClass(theComponent), null);

        encodeSubmitEvent(aContext, theWriter, theComponent);

        encodeDisabledAttributes(theComponent, theWriter);

        theWriter.endElement("input");

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

        DatepickerComponent theBaseComponent = (DatepickerComponent) aComponent;

        if (isDisabledOrReadOnly(theBaseComponent)) {
            return;
        }

        Map theParamMap = aContext.getExternalContext().getRequestParameterMap();
        String theClientId = aComponent.getClientId(aContext);

        if (theParamMap.containsKey(theClientId)) {

            // Force a revalidation here !
            theBaseComponent.setValid(true);

            String theValue = (String) theParamMap.get(theClientId);
            theBaseComponent.setSubmittedValue(theValue);
        }

        super.decode(aContext, aComponent);
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) {

        DatepickerComponent theComponent = (DatepickerComponent) component;
        ResourceBundle theBundle = ResourceBundleManager.getBundle();

        try {

            if ((submittedValue == null) || ("".equals(submittedValue))) {
                return null;
            }

            Converter theConverter = theComponent.getConverter();
            if (theConverter == null) {

                // Wenn es keinen Converter gibt, so wird angenommen, es wird
                // ein String in ein Date konvertiert
                SimpleDateFormat theFormat = new SimpleDateFormat(theBundle.getString(DATE_FORMAT_RESOURCE_NAME));

                theComponent.setValid(true);
                return theFormat.parseObject((String) submittedValue);

            } else {

                // Sonst übernimmt der Converter den Job
                theComponent.setValid(true);
                return theConverter.getAsObject(context, component, (String) submittedValue);
            }

        } catch (Exception e) {

            theComponent.setValid(false);

            String theLabel = theComponent.getDescribingLabel();

            String theMessage = MessageFormat.format(theBundle.getString(VALIDATION_ERROR), new Object[] { theLabel });
            FacesMessage theFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, theMessage, "");

            throw new ConverterException(theFacesMessage);

        }
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