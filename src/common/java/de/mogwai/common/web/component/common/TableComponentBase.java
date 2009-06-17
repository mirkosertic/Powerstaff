package de.mogwai.common.web.component.common;

import java.util.Vector;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Automatically Generated By JSFCodeGen. DO NOT ALTER ! Generated at Sun Dec 16
 * 11:38:15 CET 2007
 */
public abstract class TableComponentBase extends de.mogwai.common.web.component.ListawareComponent {

    public static final String FAMILY_NAME = "MogwaiGroupingFamily";

    public static final String COMPONENT_TYPE = "TableComponent";

    public static final String RENDERER_TYPE = "TableRenderer";

    protected String _functionsAlign;

    protected Boolean _showHeader;

    protected Boolean _showFooter;

    protected Boolean _useStyles;

    protected Boolean _fillupRows;

    @Override
    public String getFamily() {
        return FAMILY_NAME;
    }

    public void setFunctionsAlign(String aValue) {
        _functionsAlign = aValue;
    }

    public String getFunctionsAlign() {
        if (_functionsAlign != null) {
            return _functionsAlign;
        }
        ValueBinding theBinding = getValueBinding("functionsAlign");
        if (theBinding == null) {
            return "right";
        }
        String theValue = (String) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return "right";
        }
        return theValue;
    }

    public void setShowHeader(Boolean aValue) {
        _showHeader = aValue;
    }

    public void setShowHeader(boolean aValue) {
        _showHeader = aValue;
    }

    public boolean isShowHeader() {
        if (_showHeader != null) {
            return _showHeader.booleanValue();
        }
        ValueBinding theBinding = getValueBinding("showHeader");
        if (theBinding == null) {
            return true;
        }
        Boolean theValue = (Boolean) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return true;
        }
        return theValue;
    }

    public void setShowFooter(Boolean aValue) {
        _showFooter = aValue;
    }

    public void setShowFooter(boolean aValue) {
        _showFooter = aValue;
    }

    public boolean isShowFooter() {
        if (_showFooter != null) {
            return _showFooter.booleanValue();
        }
        ValueBinding theBinding = getValueBinding("showFooter");
        if (theBinding == null) {
            return false;
        }
        Boolean theValue = (Boolean) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return false;
        }
        return theValue;
    }

    public void setUseStyles(Boolean aValue) {
        _useStyles = aValue;
    }

    public void setUseStyles(boolean aValue) {
        _useStyles = aValue;
    }

    public boolean isUseStyles() {
        if (_useStyles != null) {
            return _useStyles.booleanValue();
        }
        ValueBinding theBinding = getValueBinding("useStyles");
        if (theBinding == null) {
            return true;
        }
        Boolean theValue = (Boolean) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return true;
        }
        return theValue;
    }

    public void setFillupRows(Boolean aValue) {
        _fillupRows = aValue;
    }

    public void setFillupRows(boolean aValue) {
        _fillupRows = aValue;
    }

    public boolean isFillupRows() {
        if (_fillupRows != null) {
            return _fillupRows.booleanValue();
        }
        ValueBinding theBinding = getValueBinding("fillupRows");
        if (theBinding == null) {
            return false;
        }
        Boolean theValue = (Boolean) theBinding.getValue(getFacesContext());
        if (theValue == null) {
            return false;
        }
        return theValue;
    }

    @Override
    public Object saveState(FacesContext aContext) {
        Vector<Object> theResult = new Vector<Object>();
        theResult.add(super.saveState(aContext));
        theResult.add(_functionsAlign);
        theResult.add(_showHeader);
        theResult.add(_showFooter);
        theResult.add(_useStyles);
        theResult.add(_fillupRows);
        return theResult.toArray();
    }

    @Override
    public void restoreState(FacesContext aContext, Object aState) {
        Object[] theResult = (Object[]) aState;
        super.restoreState(aContext, theResult[0]);
        _functionsAlign = (String) theResult[1];
        _showHeader = (Boolean) theResult[2];
        _showFooter = (Boolean) theResult[3];
        _useStyles = (Boolean) theResult[4];
        _fillupRows = (Boolean) theResult[5];
    }
}