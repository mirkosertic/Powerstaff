package de.mogwai.common.web.component.common;

import java.util.Vector;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Automatically Generated By JSFCodeGen. DO NOT ALTER ! Generated at Sun Dec 16
 * 11:38:15 CET 2007
 */
public abstract class LinkComponentBase extends javax.faces.component.UIComponentBase {

    public static final String FAMILY_NAME = "MogwaiGroupingFamily";

    public static final String COMPONENT_TYPE = "LinkComponent";

    public static final String RENDERER_TYPE = "LinkRenderer";

    protected String _rel;

    protected String _type;

    protected String _href;

    @Override
    public String getFamily() {
        return FAMILY_NAME;
    }

    public void setRel(String aValue) {
        _rel = aValue;
    }

    public String getRel() {
        if (_rel != null) {
            return _rel;
        }
        ValueBinding theBinding = getValueBinding("rel");
        if (theBinding == null) {
            return null;
        }
        String theValue = (String) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return null;
        }
        return theValue;
    }

    public void setType(String aValue) {
        _type = aValue;
    }

    public String getType() {
        if (_type != null) {
            return _type;
        }
        ValueBinding theBinding = getValueBinding("type");
        if (theBinding == null) {
            return null;
        }
        String theValue = (String) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return null;
        }
        return theValue;
    }

    public void setHref(String aValue) {
        _href = aValue;
    }

    public String getHref() {
        if (_href != null) {
            return _href;
        }
        ValueBinding theBinding = getValueBinding("href");
        if (theBinding == null) {
            return null;
        }
        String theValue = (String) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return null;
        }
        return theValue;
    }

    @Override
    public Object saveState(FacesContext aContext) {
        Vector<Object> theResult = new Vector<Object>();
        theResult.add(super.saveState(aContext));
        theResult.add(_rel);
        theResult.add(_type);
        theResult.add(_href);
        return theResult.toArray();
    }

    @Override
    public void restoreState(FacesContext aContext, Object aState) {
        Object[] theResult = (Object[]) aState;
        super.restoreState(aContext, theResult[0]);
        _rel = (String) theResult[1];
        _type = (String) theResult[2];
        _href = (String) theResult[3];
    }
}
