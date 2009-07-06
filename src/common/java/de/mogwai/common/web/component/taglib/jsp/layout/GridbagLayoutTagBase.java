package de.mogwai.common.web.component.taglib.jsp.layout;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagUtils;

/**
 * Automatically Generated By JSFCodeGen. DO NOT ALTER !
 */
public abstract class GridbagLayoutTagBase extends de.mogwai.common.web.component.taglib.jsp.LogableTag {

    protected String _border;

    @Override
    public String getComponentType() {
        return "GridbagLayoutComponent";
    }

    @Override
    public String getRendererType() {
        return "GridbagLayoutRenderer";
    }

    public void setBorder(String aValue) {
        _border = aValue;
    }

    @Override
    protected void setProperties(UIComponent aComponent) {
        super.setProperties(aComponent);
        UIComponentTagUtils.setIntegerProperty(getFacesContext(), aComponent, "border", _border);
    }

    @Override
    public void release() {
        super.release();
        _border = null;
    }
}