package de.mogwai.common.web.component.taglib.jsp.common;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagUtils;

/**
 * Automatically Generated By JSFCodeGen. DO NOT ALTER !
 */
public abstract class ImageTagBase extends javax.faces.webapp.UIComponentTag {

    protected String _source;

    protected String _sourceProvider;

    protected String _width;

    protected String _height;

    @Override
    public String getComponentType() {
        return "ImageComponent";
    }

    @Override
    public String getRendererType() {
        return "ImageRenderer";
    }

    public void setSource(String aValue) {
        _source = aValue;
    }

    public void setSourceProvider(String aValue) {
        _sourceProvider = aValue;
    }

    public void setWidth(String aValue) {
        _width = aValue;
    }

    public void setHeight(String aValue) {
        _height = aValue;
    }

    @Override
    protected void setProperties(UIComponent aComponent) {
        super.setProperties(aComponent);
        UIComponentTagUtils.setStringProperty(getFacesContext(), aComponent, "source", _source);
        UIComponentTagUtils.setValueBinding(getFacesContext(), aComponent, "sourceProvider", _sourceProvider);
        UIComponentTagUtils.setIntegerProperty(getFacesContext(), aComponent, "width", _width);
        UIComponentTagUtils.setIntegerProperty(getFacesContext(), aComponent, "height", _height);
    }

    @Override
    public void release() {
        super.release();
        _source = null;
        _sourceProvider = null;
        _width = null;
        _height = null;
    }
}