package de.mogwai.common.web.component.action;

import java.util.Vector;

import javax.faces.context.FacesContext;

/**
 * Automatically Generated By JSFCodeGen. DO NOT ALTER ! Generated at Sun Dec 16
 * 11:38:15 CET 2007
 */
public abstract class PageRefreshComponentBase extends de.mogwai.common.web.component.action.AbstractCommandComponent {

    public static final String FAMILY_NAME = "MogwaiActionFamily";

    public static final String COMPONENT_TYPE = "PageRefreshComponent";

    public static final String RENDERER_TYPE = "PageRefreshRenderer";

    @Override
    public String getFamily() {
        return FAMILY_NAME;
    }

    @Override
    public Object saveState(FacesContext aContext) {
        Vector<Object> theResult = new Vector<Object>();
        theResult.add(super.saveState(aContext));
        return theResult.toArray();
    }

    @Override
    public void restoreState(FacesContext aContext, Object aState) {
        Object[] theResult = (Object[]) aState;
        super.restoreState(aContext, theResult[0]);
    }
}
