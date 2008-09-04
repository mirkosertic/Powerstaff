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
package de.mogwai.common.web.component.taglib.jsp.input;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagUtils;

import de.mogwai.common.web.component.input.BaseInputComponent;
import de.mogwai.common.web.component.taglib.jsp.LogableTag;

/**
 * Base text input tag.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:18:52 $
 */
public abstract class BaseInputTag extends LogableTag {

    private String value;

    private String action;

    private String type = "text";

    private boolean required = false;

    private boolean submitOnChange = false;

    private String labelComponentId;

    private String submitEvent = "onblur";

    private String disabled;

    private boolean immediate = false;

    private int maxLength = -1;

    private boolean redisplay = true;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getLabelComponentId() {
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSubmitEvent() {
        return submitEvent;
    }

    public void setSubmitEvent(String submitEvent) {
        this.submitEvent = submitEvent;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public boolean isImmediate() {
        return immediate;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public int getMaxLength() {
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
    protected void setProperties(UIComponent aComponent) {

        BaseInputComponent theComponent = (BaseInputComponent) aComponent;
        theComponent.initInputComponent();

        super.setProperties(aComponent);

        theComponent.initValueBindingForChildren();

        if (action != null) {

            theComponent.initCommandComponent();

            UIComponentTagUtils.setActionProperty(getFacesContext(), theComponent, action);

            theComponent.getCommandComponent().setImmediate(immediate);
            theComponent.setSubmitOnChange(true);
            theComponent.setImmediate(immediate);

        } else {
            theComponent.setSubmitOnChange(submitOnChange);
        }

        UIComponentTagUtils.setBooleanProperty(getFacesContext(), theComponent, "disabled", disabled);

        theComponent.setType(type);

        UIComponentTagUtils.setValueProperty(getFacesContext(), theComponent, value);

        theComponent.setRequired(required);
        theComponent.setLabelComponentId(labelComponentId);
        theComponent.setSubmitEvent(submitEvent);
        theComponent.setMaxLength(maxLength);
        theComponent.setRedisplay(redisplay);
    }

}
