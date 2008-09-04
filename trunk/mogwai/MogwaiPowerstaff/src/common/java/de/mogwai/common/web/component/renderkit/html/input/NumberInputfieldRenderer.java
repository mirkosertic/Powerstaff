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

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;

import de.mogwai.common.web.ResourceBundleManager;
import de.mogwai.common.web.component.input.BaseInputComponent;
import de.mogwai.common.web.component.input.NumberInputfieldComponent;

/**
 * Renderer for number input fields.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:24:56 $
 */
public class NumberInputfieldRenderer extends BaseInputRenderer {

    private static final String VALIDATION_ERROR = "validator.invalidnumber";

    private static final String ENABLED_CLASS = "mogwaiNumberInputfield";

    private static final String DISABLED_CLASS = "mogwaiNumberInputfieldDisabled";

    public NumberInputfieldRenderer() {
    }

    @Override
    protected String getType(BaseInputComponent aComponent) {
        return aComponent.getType();
    }

    @Override
    public void decode(FacesContext aContext, UIComponent aComponent) {
        super.decode(aContext, aComponent);

        NumberInputfieldComponent theBaseComponent = (NumberInputfieldComponent) aComponent;

        if (isDisabledOrReadOnly(theBaseComponent)) {
            return;
        }

        Map theParamMap = aContext.getExternalContext().getRequestParameterMap();
        String theInputClientId = theBaseComponent.getInputComponent().getClientId(aContext);

        if (theParamMap.containsKey(theInputClientId)) {

            theBaseComponent.setSubmittedValue(theParamMap.get(theInputClientId));

        }

        super.decode(aContext, aComponent);
    }

    @Override
    public Object getConvertedValue(FacesContext aContext, UIComponent aComponent, Object aSubmittedValue) {

        NumberInputfieldComponent theComponent = (NumberInputfieldComponent) aComponent;

        try {

            if ((aSubmittedValue == null) || ("".equals(aSubmittedValue))) {
                return null;
            }

            ValueBinding theBinding = theComponent.getValueBinding("value");
            if (theBinding != null) {

                theComponent.setValid(true);
                Class theType = theBinding.getType(aContext);

                String theNumberFormat = theComponent.getNumberFormatResourceKey();
                if (theNumberFormat == null) {

                    if ((theType.equals(int.class)) || (theType.equals(Integer.class))) {
                        return Integer.parseInt((String) aSubmittedValue);
                    }
                    if ((theType.equals(long.class)) || (theType.equals(Long.class))) {
                        return Long.parseLong((String) aSubmittedValue);
                    }
                    if ((theType.equals(double.class)) || (theType.equals(Double.class))) {
                        return Double.parseDouble((String) aSubmittedValue);
                    }
                    if (theType.equals(BigDecimal.class)) {
                        return new BigDecimal((String) aSubmittedValue);
                    }

                    throw new IllegalArgumentException(
                            "NumberInputField does only support int,long and double! Expression is "
                                    + theBinding.getExpressionString());
                } else {

                    NumberFormat theFormat = theComponent.createNumberFormat();
                    try {

                        // Formatierung wieder zurück, damit überflüssige
                        // Nachkommastellen abgeschnitten werden
                        Number theNumber = theFormat.parse((String) aSubmittedValue);
                        aSubmittedValue = theFormat.format(theNumber);
                        theNumber = theFormat.parse((String) aSubmittedValue);

                        if ((theType.equals(int.class)) || (theType.equals(Integer.class))) {
                            return theNumber.intValue();
                        }
                        if ((theType.equals(long.class)) || (theType.equals(Long.class))) {
                            return theNumber.longValue();
                        }
                        if ((theType.equals(double.class)) || (theType.equals(Double.class))) {
                            return theNumber.doubleValue();
                        }
                        if (theType.equals(BigDecimal.class)) {
                            return new BigDecimal(theNumber.doubleValue());
                        }

                        throw new IllegalArgumentException(
                                "NumberInputField does only support int,long and double! Expression is "
                                        + theBinding.getExpressionString());

                    } catch (Exception e) {

                        theComponent.setValid(false);
                        ResourceBundle theBundle = ResourceBundleManager.getBundle();

                        String theLabel = theComponent.getDescribingLabel();

                        String theMessage = MessageFormat.format(theBundle.getString(VALIDATION_ERROR),
                                new Object[] { theLabel });
                        FacesMessage theFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, theMessage, "");

                        throw new ConverterException(theFacesMessage);

                    }

                }
            }

            return aSubmittedValue;

        } catch (Exception e) {

            theComponent.setValid(false);

            ResourceBundle theBundle = ResourceBundleManager.getBundle();

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

    @Override
    public String getStringDisplayValue(UIInput aComponent) {

        NumberInputfieldComponent theComponent = (NumberInputfieldComponent) aComponent;

        String aNumberFormatResourceKey = theComponent.getNumberFormatResourceKey();

        if (aNumberFormatResourceKey == null) {
            return super.getStringDisplayValue(aComponent);
        } else {
            if (aComponent.isValid()) {

                Number theNumber = (Number) aComponent.getValue();
                if (theNumber == null) {
                    return "";
                }

                NumberFormat theFormat = theComponent.createNumberFormat();
                return theFormat.format(theNumber);

            } else {
                return (String) theComponent.getSubmittedValue();
            }
        }
    }
}
