package de.mogwai.common.web.component.input;

import java.util.Vector;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Automatically Generated By JSFCodeGen. DO NOT ALTER ! Generated at Sun Dec 16
 * 11:38:15 CET 2007
 */
public abstract class NumberInputfieldComponentBase extends de.mogwai.common.web.component.input.BaseInputComponent {

    public static final String FAMILY_NAME = "MogwaiInputFamily";

    public static final String COMPONENT_TYPE = "NumberInputfieldComponent";

    public static final String RENDERER_TYPE = "NumberInputfieldRenderer";

    protected String _numberFormatResourceKey;

    @Override
    public String getFamily() {
        return FAMILY_NAME;
    }

    public void setNumberFormatResourceKey(String aValue) {
        _numberFormatResourceKey = aValue;
    }

    public String getNumberFormatResourceKey() {
        if (_numberFormatResourceKey != null) {
            return _numberFormatResourceKey;
        }
        ValueBinding theBinding = getValueBinding("numberFormatResourceKey");
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
        theResult.add(_numberFormatResourceKey);
        return theResult.toArray();
    }

    @Override
    public void restoreState(FacesContext aContext, Object aState) {
        Object[] theResult = (Object[]) aState;
        super.restoreState(aContext, theResult[0]);
        _numberFormatResourceKey = (String) theResult[1];
    }
}
