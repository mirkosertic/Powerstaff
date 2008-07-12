package de.mogwai.common.web.component.common;

import java.util.Vector;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Automatically Generated By JSFCodeGen. DO NOT ALTER ! Generated at Sun Dec 16
 * 11:38:15 CET 2007
 */
public abstract class ImageComponentBase extends javax.faces.component.UIComponentBase {

    public static final String FAMILY_NAME = "MogwaiGroupingFamily";

    public static final String COMPONENT_TYPE = "ImageComponent";

    public static final String RENDERER_TYPE = "ImageRenderer";

    protected String _source;

    protected de.mogwai.common.utils.SourceProvider _sourceProvider;

    protected Integer _width;

    protected Integer _height;

    @Override
    public String getFamily() {
        return FAMILY_NAME;
    }

    public void setSource(String aValue) {
        _source = aValue;
    }

    public String getSource() {
        if (_source != null) {
            return _source;
        }
        ValueBinding theBinding = getValueBinding("source");
        if (theBinding == null) {
            return null;
        }
        String theValue = (String) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return null;
        }
        return theValue;
    }

    public void setSourceProvider(de.mogwai.common.utils.SourceProvider aValue) {
        _sourceProvider = aValue;
    }

    public de.mogwai.common.utils.SourceProvider getSourceProvider() {
        if (_sourceProvider != null) {
            return _sourceProvider;
        }
        ValueBinding theBinding = getValueBinding("sourceProvider");
        if (theBinding == null) {
            return null;
        }
        de.mogwai.common.utils.SourceProvider theValue = (de.mogwai.common.utils.SourceProvider) theBinding
                .getValue(getFacesContext());
        if (theValue == null) {
            return null;
        }
        return theValue;
    }

    public void setWidth(Integer aValue) {
        _width = aValue;
    }

    public Integer getWidth() {
        if (_width != null) {
            return _width;
        }
        ValueBinding theBinding = getValueBinding("width");
        if (theBinding == null) {
            return null;
        }
        Integer theValue = (Integer) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return null;
        }
        return theValue;
    }

    public void setHeight(Integer aValue) {
        _height = aValue;
    }

    public Integer getHeight() {
        if (_height != null) {
            return _height;
        }
        ValueBinding theBinding = getValueBinding("height");
        if (theBinding == null) {
            return null;
        }
        Integer theValue = (Integer) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return null;
        }
        return theValue;
    }

    @Override
    public Object saveState(FacesContext aContext) {
        Vector<Object> theResult = new Vector<Object>();
        theResult.add(super.saveState(aContext));
        theResult.add(_source);
        theResult.add(_sourceProvider);
        theResult.add(_width);
        theResult.add(_height);
        return theResult.toArray();
    }

    @Override
    public void restoreState(FacesContext aContext, Object aState) {
        Object[] theResult = (Object[]) aState;
        super.restoreState(aContext, theResult[0]);
        _source = (String) theResult[1];
        _sourceProvider = (de.mogwai.common.utils.SourceProvider) theResult[2];
        _width = (Integer) theResult[3];
        _height = (Integer) theResult[4];
    }
}
