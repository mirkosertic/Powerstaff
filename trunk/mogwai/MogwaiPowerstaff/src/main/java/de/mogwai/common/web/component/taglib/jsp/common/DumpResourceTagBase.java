package de.mogwai.common.web.component.taglib.jsp.common;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagUtils;

/**
 * Automatically Generated By JSFCodeGen. DO NOT ALTER !
 */
public abstract class DumpResourceTagBase extends javax.faces.webapp.UIComponentTag {

    protected String _key;

    @Override
    public String getComponentType() {
        return "DumpResourceComponent";
    }

    @Override
    public String getRendererType() {
        return "DumpResourceRenderer";
    }

    public void setKey(String aValue) {
        _key = aValue;
    }

    @Override
    protected void setProperties(UIComponent aComponent) {
        super.setProperties(aComponent);
        UIComponentTagUtils.setStringProperty(getFacesContext(), aComponent, "key", _key);
    }

    @Override
    public void release() {
        super.release();
        _key = null;
    }
}
