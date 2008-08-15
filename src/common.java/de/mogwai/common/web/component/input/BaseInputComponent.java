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
package de.mogwai.common.web.component.input;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.faces.application.FacesMessage;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import de.mogwai.common.utils.LabelProvider;
import de.mogwai.common.web.FieldInformationProvider;
import de.mogwai.common.web.ResourceBundleManager;
import de.mogwai.common.web.ValueBindingConverter;
import de.mogwai.common.web.component.DynamicContentComponent;
import de.mogwai.common.web.component.action.CommandButtonComponent;

/**
 * Base text input component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:09:43 $
 */
public class BaseInputComponent extends UIInput implements DynamicContentComponent {

    private static final String MISSING_REQUIRED_FIELD_KEY = "validator.missingrequiredfield";

    private static final String NO_VALUE = "";

    private String type;

    private String labelComponentId;

    private boolean required;

    private boolean submitOnChange;

    private boolean disabled;

    private String submitEvent;

    private int maxLength = -1;

    private boolean redisplay = true;

    protected BaseInputComponent() {
    }

    public void initCommandComponent() {

        CommandButtonComponent theCommandComponent = new CommandButtonComponent();
        theCommandComponent.setRendererType("CommandButtonRenderer");

        theCommandComponent.setId(FacesContext.getCurrentInstance().getViewRoot().createUniqueId());
        getChildren().add(theCommandComponent);
    }

    public void initInputComponent() {

        UIInput theInput = new UIInput();
        theInput.setId(getId() + "_value");
        theInput.setRendered(false);
        getChildren().add(theInput);
    }

    public UICommand getCommandComponent() {
        return (UICommand) getChildren().get(1);
    }

    public UIInput getInputComponent() {
        return (UIInput) getChildren().get(0);
    }

    public void initValueBindingForChildren() {
        getInputComponent().setValueBinding("value", getValueBinding("value"));
    }

    /**
     * Ermittlung des Beschreibenden Labels für diese Komponente.
     * 
     * Wenn die labelComponentID gesetzt ist, wird die Komponente mit dieser ID
     * innerhalb des aktuellen NamingContainers gesucht. Wenn die Komponente
     * gefunde wurde, und sie das Interface LabelProvider implementiert, wird
     * mittels getLabel() das Label ermittelt.
     * 
     * Wenn nichts gefunden wurde, wird ein Leerstring zurückgegeben.
     * 
     * @return das Label
     */
    public String getDescribingLabel() {

        String theLabel = "";

        String theLabelComponentId = getLabelComponentId();
        if (theLabelComponentId != null) {
            UIComponent theComponent = findComponent(theLabelComponentId);
            if ((theComponent != null) && (theComponent instanceof LabelProvider)) {
                theLabel = ((LabelProvider) theComponent).getLabel();
            }
        }

        return theLabel;
    }

    protected void addMissingRequiredFieldMessage(FacesContext aContext) {

        // Sie ist nicht gefüllt, also ist diese Komponente Invalid
        ResourceBundle theBundle = ResourceBundleManager.getBundle();

        String theLabel = getDescribingLabel();

        String theMessage = MessageFormat.format(theBundle.getString(MISSING_REQUIRED_FIELD_KEY),
                new Object[] { theLabel });
        FacesMessage theFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, theMessage, "");

        aContext.addMessage(getClientId(aContext), theFacesMessage);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getConvertedValue(FacesContext aContext, Object aSubmittedValue) {

        // Wenn die Komponente ein Pflichtfeld ist, diese überprüfen
        if (isRequired()) {

            if ((aSubmittedValue == null) || (NO_VALUE.equals(aSubmittedValue))) {

                // Sie ist nicht gefüllt, also ist diese Komponente Invalid
                addMissingRequiredFieldMessage(aContext);

                setValid(false);

                return null;
            }
        }
        return super.getConvertedValue(aContext, aSubmittedValue);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean isRequired() {
        ValueBinding theBinding = getValueBinding("value");
        if (theBinding instanceof FieldInformationProvider) {
            FieldInformationProvider theProvider = (FieldInformationProvider) theBinding;
            Boolean theRequiredInfo = theProvider.isRequired(getFacesContext());
            if (theRequiredInfo != null) {
                return theRequiredInfo.booleanValue();
            }
        }
        return required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isDisabled() {

        ValueBinding theBinding = getValueBinding("disabled");
        if (theBinding != null) {

            return ((Boolean) theBinding.getValue(FacesContext.getCurrentInstance())).booleanValue();
        }

        return disabled;
    }

    public void setDisabled(boolean aValue) {

        disabled = aValue;
    }

    public String getLabelComponentId() {

        ValueBinding theBinding = getValueBinding("labelComponentId");
        if (theBinding != null) {
            return (String) theBinding.getValue(getFacesContext());
        }
        return labelComponentId;
    }

    public void setLabelComponentId(String labelComponentId) {
        this.labelComponentId = labelComponentId;
    }

    public boolean isSubmitOnChange() {
        return submitOnChange;
    }

    public void setSubmitOnChange(boolean submitOnChange) {
        this.submitOnChange = submitOnChange;
    }

    public String getSubmitEvent() {
        return submitEvent;
    }

    public void setSubmitEvent(String submitEvent) {
        this.submitEvent = submitEvent;
    }

    public final int getMaxLength() {
        ValueBinding theBinding = getValueBinding("value");
        if (theBinding instanceof FieldInformationProvider) {
            FieldInformationProvider theProvider = (FieldInformationProvider) theBinding;
            Integer theMaxLength = theProvider.getMaxLength(getFacesContext());
            if (theMaxLength != null) {
                return theMaxLength.intValue();
            }
        }
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Gibt den Wert des Attributs <code>redisplay</code> zurück.
     * 
     * @return Wert des Attributs redisplay.
     */
    public boolean isRedisplay() {
        return redisplay;
    }

    /**
     * Setzt den Wert des Attributs <code>redisplay</code>.
     * 
     * @param redisplay
     *                Wert für das Attribut redisplay.
     */
    public void setRedisplay(boolean redisplay) {
        this.redisplay = redisplay;
    }

    @Override
    public void restoreState(FacesContext aContext, Object aState) {

        Object[] theValues = (Object[]) aState;

        super.restoreState(aContext, theValues[0]);

        type = (String) theValues[1];
        required = ((Boolean) theValues[2]).booleanValue();
        labelComponentId = (String) theValues[3];
        submitOnChange = ((Boolean) theValues[4]).booleanValue();
        submitEvent = (String) theValues[5];
        disabled = (Boolean) theValues[6];
        maxLength = (Integer) theValues[7];
        redisplay = (Boolean) theValues[8];
    }

    @Override
    public Object saveState(FacesContext aContext) {

        Vector<Object> theState = new Vector<Object>();
        theState.add(super.saveState(aContext));
        theState.add(type);
        theState.add(required);
        theState.add(labelComponentId);
        theState.add(submitOnChange);
        theState.add(submitEvent);
        theState.add(disabled);
        theState.add(maxLength);
        theState.add(redisplay);

        return theState.toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(FacesContext aContext) {
        if ((ModalComponentUtils.isNotOverlayedByModalDialog(this, aContext)) && (!isDisabled())) {
            super.validate(aContext);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateModel(FacesContext aContext) {
        if ((ModalComponentUtils.isNotOverlayedByModalDialog(this, aContext)) && (!isDisabled())) {
            super.updateModel(aContext);
        }
    }

    @Override
    public void setValueBinding(String aName, ValueBinding aBinding) {
        super.setValueBinding(aName, ValueBindingConverter.convertTo(getFacesContext(), aBinding));
    }

}