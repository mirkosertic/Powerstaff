package de.mogwai.common.web.component.common;

import java.util.Vector;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Automatically Generated By JSFCodeGen. DO NOT ALTER ! Generated at Sun Dec 16
 * 11:38:15 CET 2007
 */
public abstract class LabelComponentBase extends javax.faces.component.UIComponentBase {

    public static final String FAMILY_NAME = "MogwaiInputFamily";

    public static final String COMPONENT_TYPE = "LabelComponent";

    public static final String RENDERER_TYPE = "LabelRenderer";

    protected String _label;

    protected String _key;

    protected String _styleClass;

    protected Boolean _addColon;

    @Override
    public String getFamily() {
        return FAMILY_NAME;
    }

    public void setLabel(String aValue) {
        _label = aValue;
    }

    public String getLabel() {
        if (_label != null) {
            return _label;
        }
        ValueBinding theBinding = getValueBinding("label");
        if (theBinding == null) {
            return null;
        }
        String theValue = (String) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return null;
        }
        return theValue;
    }

    public void setKey(String aValue) {
        _key = aValue;
    }

    public String getKey() {
        if (_key != null) {
            return _key;
        }
        ValueBinding theBinding = getValueBinding("key");
        if (theBinding == null) {
            return null;
        }
        String theValue = (String) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return null;
        }
        return theValue;
    }

    public void setStyleClass(String aValue) {
        _styleClass = aValue;
    }

    public String getStyleClass() {
        if (_styleClass != null) {
            return _styleClass;
        }
        ValueBinding theBinding = getValueBinding("styleClass");
        if (theBinding == null) {
            return null;
        }
        String theValue = (String) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return null;
        }
        return theValue;
    }

    public void setAddColon(Boolean aValue) {
        _addColon = aValue;
    }

    public void setAddColon(boolean aValue) {
        _addColon = aValue;
    }

    public boolean isAddColon() {
        if (_addColon != null) {
            return _addColon.booleanValue();
        }
        ValueBinding theBinding = getValueBinding("addColon");
        if (theBinding == null) {
            return true;
        }
        Boolean theValue = (Boolean) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return true;
        }
        return theValue;
    }

    @Override
    public Object saveState(FacesContext aContext) {
        Vector<Object> theResult = new Vector<Object>();
        theResult.add(super.saveState(aContext));
        theResult.add(_label);
        theResult.add(_key);
        theResult.add(_styleClass);
        theResult.add(_addColon);
        return theResult.toArray();
    }

    @Override
    public void restoreState(FacesContext aContext, Object aState) {
        Object[] theResult = (Object[]) aState;
        super.restoreState(aContext, theResult[0]);
        _label = (String) theResult[1];
        _key = (String) theResult[2];
        _styleClass = (String) theResult[3];
        _addColon = (Boolean) theResult[4];
    }
}
