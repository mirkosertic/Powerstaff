package de.mogwai.common.web.component.taglib.jsp.common;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagUtils;

/**
 * Automatically Generated By JSFCodeGen. DO NOT ALTER !
 */
public abstract class LabelTagBase extends javax.faces.webapp.UIComponentTag {

    protected String _label;

    protected String _key;

    protected String _styleClass;

    protected String _addColon;

    @Override
    public String getComponentType() {
        return "LabelComponent";
    }

    @Override
    public String getRendererType() {
        return "LabelRenderer";
    }

    public void setLabel(String aValue) {
        _label = aValue;
    }

    public void setKey(String aValue) {
        _key = aValue;
    }

    public void setStyleClass(String aValue) {
        _styleClass = aValue;
    }

    public void setAddColon(String aValue) {
        _addColon = aValue;
    }

    @Override
    protected void setProperties(UIComponent aComponent) {
        super.setProperties(aComponent);
        UIComponentTagUtils.setStringProperty(getFacesContext(), aComponent, "label", _label);
        UIComponentTagUtils.setStringProperty(getFacesContext(), aComponent, "key", _key);
        UIComponentTagUtils.setStringProperty(getFacesContext(), aComponent, "styleClass", _styleClass);
        UIComponentTagUtils.setBooleanProperty(getFacesContext(), aComponent, "addColon", _addColon);
    }

    @Override
    public void release() {
        super.release();
        _label = null;
        _key = null;
        _styleClass = null;
        _addColon = null;
    }
}
