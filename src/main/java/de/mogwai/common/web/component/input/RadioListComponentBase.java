package de.mogwai.common.web.component.input;

import java.util.Vector;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Automatically Generated By JSFCodeGen. DO NOT ALTER ! Generated at Sun Dec 16
 * 11:38:15 CET 2007
 */
public abstract class RadioListComponentBase extends de.mogwai.common.web.component.input.BaseInputComponent {

    public static final String FAMILY_NAME = "MogwaiInputFamily";

    public static final String COMPONENT_TYPE = "RadioListComponent";

    public static final String RENDERER_TYPE = "RadioListRenderer";

    protected String _display;

    protected String _flow;

    protected Integer _columns;

    @Override
    public String getFamily() {
        return FAMILY_NAME;
    }

    public void setDisplay(String aValue) {
        _display = aValue;
    }

    public String getDisplay() {
        if (_display != null) {
            return _display;
        }
        ValueBinding theBinding = getValueBinding("display");
        if (theBinding == null) {
            return null;
        }
        String theValue = (String) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return null;
        }
        return theValue;
    }

    public void setFlow(String aValue) {
        _flow = aValue;
    }

    public String getFlow() {
        if (_flow != null) {
            return _flow;
        }
        ValueBinding theBinding = getValueBinding("flow");
        if (theBinding == null) {
            return "vertical";
        }
        String theValue = (String) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return "vertical";
        }
        return theValue;
    }

    public void setColumns(Integer aValue) {
        _columns = aValue;
    }

    public void setColumns(int aValue) {
        _columns = aValue;
    }

    public int getColumns() {
        if (_columns != null) {
            return _columns.intValue();
        }
        ValueBinding theBinding = getValueBinding("columns");
        if (theBinding == null) {
            return 1;
        }
        Number theValue = (Number) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return 1;
        }
        return theValue.intValue();
    }

    @Override
    public Object saveState(FacesContext aContext) {
        Vector<Object> theResult = new Vector<Object>();
        theResult.add(super.saveState(aContext));
        theResult.add(_display);
        theResult.add(_flow);
        theResult.add(_columns);
        return theResult.toArray();
    }

    @Override
    public void restoreState(FacesContext aContext, Object aState) {
        Object[] theResult = (Object[]) aState;
        super.restoreState(aContext, theResult[0]);
        _display = (String) theResult[1];
        _flow = (String) theResult[2];
        _columns = (Integer) theResult[3];
    }
}
