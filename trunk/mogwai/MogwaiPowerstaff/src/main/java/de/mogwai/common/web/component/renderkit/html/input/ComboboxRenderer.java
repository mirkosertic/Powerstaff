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
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.beanutils.BeanUtils;

import de.mogwai.common.logging.Logger;
import de.mogwai.common.utils.ObjectProvider;
import de.mogwai.common.web.component.input.BaseInputComponent;
import de.mogwai.common.web.component.input.ComboboxComponent;
import de.mogwai.common.web.component.input.ModalComponentUtils;
import de.mogwai.common.web.utils.JSFJavaScriptFactory;
import de.mogwai.common.web.utils.JSFJavaScriptUtilities;

/**
 * Combobox renderer.
 * 
 * Durch einen BrowserBug in IE<7 werden Select - Elemente als Windowed -
 * Elemente dargestellt. Falls nun ein Modaler Dialog auf dieser Seite
 * erscheint, werden die Select - Elemente immer über dem modalen Dialog
 * angezeigt. Aus diesem Grunde werden alle Select - Elemente, sobald ein
 * sichtbarer modaler Dialog angezeigt wird, nur noch als disabled Textfelder
 * gerendert, um diesen Bug zu umgehen.
 * 
 * Microsoft - What evil shall we do today?
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:57 $
 */
public class ComboboxRenderer extends BaseInputRenderer {

    private final static Logger LOGGER = new Logger(ComboboxRenderer.class);

    public static final String ENABLED_CLASS = "mogwaiCombobox";

    public static final String DISABLED_CLASS = "mogwaiComboboxDisabled";

    public ComboboxRenderer() {
    }

    protected void encodeDisabledAttributesSelect(BaseInputComponent aComponent, ResponseWriter aWriter)
            throws IOException {
        if (aComponent.isDisabled()) {
            aWriter.writeAttribute("disabled", "disabled", null);
        }
    }

    protected void encodeDisabledAttributesInput(BaseInputComponent aComponent, ResponseWriter aWriter)
            throws IOException {
        if (aComponent.isDisabled()) {
            aWriter.writeAttribute("readonly", "readonly", null);
        }
    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {

        boolean theRenderAsSelect = ModalComponentUtils.isInModalDialogOrOnSimplePage(aContext, aComponent);

        if (theRenderAsSelect) {
            encodeEndAsSelect(aContext, aComponent);
        } else {
            encodeEndAsInput(aContext, aComponent);
        }
    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {
    }

    protected void encodeEndAsInput(FacesContext aContext, UIComponent aComponent) throws IOException {

        ResponseWriter theWriter = aContext.getResponseWriter();

        ComboboxComponent theComponent = (ComboboxComponent) aComponent;

        String theClientID = aComponent.getClientId(aContext);

        String theDisplayValue = "";
        Object theCurrentValue = ModalComponentUtils.getCurrentComponentValue(theComponent);

        String theDisplay = theComponent.getDisplay();

        List theValues = theComponent.getValues();
        if (theValues != null) {
            for (int count = 0; count < theValues.size(); count++) {
                Object theEntry = theValues.get(count);

                Object theCompareValue = theEntry;
                if (theCompareValue instanceof ObjectProvider) {
                    theCompareValue = ((ObjectProvider) theCompareValue).getProvidedObject();
                }

                if (theCompareValue.equals(theCurrentValue)) {

                    if (theDisplay == null) {

                        theDisplayValue = getStringValue(aContext, theComponent, theEntry);

                    } else {

                        try {

                            Object theValue = BeanUtils.getProperty(theEntry, theDisplay);
                            theDisplayValue = getStringValue(aContext, theComponent, theValue);

                        } catch (Exception e) {

                            String theMessage = "Invalid display property for combobox component :" + theDisplay;

                            throw new RuntimeException(theMessage, e);
                        }
                    }
                }
            }
        }

        theWriter.startElement("div", aComponent);
        theWriter.writeAttribute("id", theClientID, null);
        theWriter.writeAttribute("style", "position:relative;", null);
        theWriter.startElement("input", aComponent);

        String theType = getType(theComponent);
        if (theType != null) {
            theWriter.writeAttribute("type", theType, null);
        }

        // Damit die Komponente in diesem Zustand nicht dekodiert wird, wird ein
        // fiktiver Komponentenname vergeben!
        theWriter.writeAttribute("name", theClientID + "_disabled", null);

        theWriter.writeAttribute("value", theDisplayValue, "value");
        theWriter.writeAttribute("class", getDisplayClass(theComponent), null);

        encodeDisabledAttributesInput(theComponent, theWriter);

        encodeSubmitEvent(aContext, theWriter, theComponent);

        setWidthIfInGridBag(theWriter, aComponent, null);

        theWriter.endElement("input");

        renderInvalidMarker(theWriter, theComponent);

        theWriter.endElement("div");

        JSFJavaScriptUtilities theUtilities = JSFJavaScriptFactory.getJavaScriptUtilities(aContext);
        theUtilities.encodeSpecialFormSubmitHTML(aContext, theComponent, theWriter);
    }

    protected void encodeEndAsSelect(FacesContext aContext, UIComponent aComponent) throws IOException {

        ResponseWriter theWriter = aContext.getResponseWriter();
        ComboboxComponent theComponent = (ComboboxComponent) aComponent;

        String theClientID = aComponent.getClientId(aContext);

        theWriter.startElement("div", aComponent);
        theWriter.writeAttribute("id", theClientID, null);
        theWriter.writeAttribute("style", "position:relative;", null);

        theWriter.startElement("select", aComponent);
        setWidthIfInGridBag(theWriter, aComponent, getHeightIfInGridGag(theComponent));
        theWriter.writeAttribute("name", theClientID, null);

        // Autocomplete feature
        theWriter.writeAttribute("onkeypress", "mogwaiComboboxAutoComplete();", null);

        theWriter.writeAttribute("class", getDisplayClass(theComponent), null);
        theWriter.writeAttribute("size", "1", null);

        encodeDisabledAttributesSelect(theComponent, theWriter);

        encodeSubmitEvent(aContext, theWriter, theComponent);

        String theDisplay = theComponent.getDisplay();

        Object theCurrentValue = ModalComponentUtils.getCurrentComponentValue(theComponent);
        if ("".equals(theCurrentValue)) {
            theCurrentValue = null;
        }

        if ((theComponent.isNullable()) || ((theCurrentValue == null) && (theComponent.isDisabled()))) {
            theWriter.startElement("option", aComponent);
            theWriter.writeAttribute("value", "-1", null);
            theWriter.endElement("option");
        }

        List theValues = theComponent.getValues();
        if (theValues != null) {
            for (int count = 0; count < theValues.size(); count++) {
                Object theEntry = theValues.get(count);

                theWriter.startElement("option", aComponent);
                theWriter.writeAttribute("value", count, null);

                Object theCompareValue = theEntry;
                if (theCompareValue instanceof ObjectProvider) {
                    theCompareValue = ((ObjectProvider) theCompareValue).getProvidedObject();
                }

                if (theCompareValue != null) {
                    if (theCompareValue.equals(theCurrentValue)) {
                        theWriter.writeAttribute("selected", "selected", null);
                    }
                } else {
                    if (theCurrentValue == null) {
                        theWriter.writeAttribute("selected", "selected", null);
                    }
                }

                String theDisplayValue;
                if (theDisplay == null) {

                    theDisplayValue = getStringValue(aContext, theComponent, theEntry);

                } else {
                    try {
                        Object theValue = BeanUtils.getProperty(theEntry, theDisplay);

                        theDisplayValue = getStringValue(aContext, theComponent, theValue);

                    } catch (Exception e) {

                        String theMessage = "Invalid display property for combobox component :" + theDisplay;
                        LOGGER.logError(theMessage, e);

                        throw new RuntimeException(theMessage, e);
                    }
                }

                theWriter.write(theDisplayValue);

                theWriter.endElement("option");
            }
        }

        theWriter.endElement("select");

        renderInvalidMarker(theWriter, theComponent);

        theWriter.endElement("div");

        JSFJavaScriptUtilities theUtilities = JSFJavaScriptFactory.getJavaScriptUtilities(aContext);
        theUtilities.encodeSpecialFormSubmitHTML(aContext, theComponent, theWriter);
    }

    @Override
    public void decode(FacesContext aContext, UIComponent aComponent) {

        ComboboxComponent theComponent = (ComboboxComponent) aComponent;

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

                if (theNewValue == null) {
                    theNewValue = "";
                }

                theComponent.setSubmittedValue(theNewValue);
                theComponent.setLocalValueSet(true);

            } else {

                theComponent.setSubmittedValue("");
                theComponent.setLocalValueSet(true);

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
        return null;
    }

}