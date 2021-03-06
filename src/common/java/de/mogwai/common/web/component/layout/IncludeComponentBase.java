package de.mogwai.common.web.component.layout;

import java.util.Vector;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Automatically Generated By JSFCodeGen. DO NOT ALTER ! Generated at Sun Dec 16
 * 11:38:15 CET 2007
 */
public abstract class IncludeComponentBase extends javax.faces.component.UIOutput {

    public static final String FAMILY_NAME = "MogwaiGroupingFamily";

    public static final String COMPONENT_TYPE = "IncludeComponent";

    public static final String RENDERER_TYPE = "IncludeRenderer";

    protected String _page;

    protected String _var;

    @Override
    public String getFamily() {
        return FAMILY_NAME;
    }

    public void setPage(String aValue) {
        _page = aValue;
    }

    public String getPage() {
        if (_page != null) {
            return _page;
        }
        ValueBinding theBinding = getValueBinding("page");
        if (theBinding == null) {
            return null;
        }
        String theValue = (String) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return null;
        }
        return theValue;
    }

    public void setVar(String aValue) {
        _var = aValue;
    }

    public String getVar() {
        if (_var != null) {
            return _var;
        }
        ValueBinding theBinding = getValueBinding("var");
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
        theResult.add(_page);
        theResult.add(_var);
        return theResult.toArray();
    }

    @Override
    public void restoreState(FacesContext aContext, Object aState) {
        Object[] theResult = (Object[]) aState;
        super.restoreState(aContext, theResult[0]);
        _page = (String) theResult[1];
        _var = (String) theResult[2];
    }
}
