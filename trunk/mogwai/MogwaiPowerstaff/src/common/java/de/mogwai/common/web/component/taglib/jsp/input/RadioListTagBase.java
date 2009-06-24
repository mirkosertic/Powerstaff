package de.mogwai.common.web.component.taglib.jsp.input;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagUtils;

/**
 * Automatically Generated By JSFCodeGen. DO NOT ALTER !
 */
public abstract class RadioListTagBase extends de.mogwai.common.web.component.taglib.jsp.input.BaseInputTag {

    protected String _display;

    protected String _flow;

    protected String _columns;

    protected String _values;

    @Override
    public String getComponentType() {
        return "RadioListComponent";
    }

    @Override
    public String getRendererType() {
        return "RadioListRenderer";
    }

    public void setDisplay(String aValue) {
        _display = aValue;
    }

    public void setFlow(String aValue) {
        _flow = aValue;
    }

    public void setColumns(String aValue) {
        _columns = aValue;
    }

    public void setValues(String aValue) {
        _values = aValue;
    }

    @Override
    protected void setProperties(UIComponent aComponent) {
        super.setProperties(aComponent);
        UIComponentTagUtils.setStringProperty(getFacesContext(), aComponent, "display", _display);
        UIComponentTagUtils.setStringProperty(getFacesContext(), aComponent, "flow", _flow);
        UIComponentTagUtils.setIntegerProperty(getFacesContext(), aComponent, "columns", _columns);
        UIComponentTagUtils.setStringProperty(getFacesContext(), aComponent, "values", _values);
    }

    @Override
    public void release() {
        super.release();
        _display = null;
        _flow = null;
        _columns = null;
        _values = null;
    }
}