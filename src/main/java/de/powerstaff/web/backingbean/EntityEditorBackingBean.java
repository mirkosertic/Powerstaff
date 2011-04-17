package de.powerstaff.web.backingbean;

import java.util.ArrayList;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

import de.mogwai.common.command.ResetNavigationInfo;
import de.mogwai.common.web.backingbean.WrappingBackingBean;

public abstract class EntityEditorBackingBean<T extends EntityEditorBackingBeanDataModel> extends
        WrappingBackingBean<T> implements MessageConstants, StateHolder {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2457744179297229854L;

	@Override
    public void init() {
        super.init();
        setData(createDataModel());
    }

    @Override
    public void resetNavigation() {
        super.resetNavigation();
        init();
    }

    public boolean isTransient() {
        return false;
    }

    public void setTransient(boolean aValue) {
    }

    public void restoreState(FacesContext aContext, Object aValue) {
        Object[] theData = (Object[]) aValue;
        setData((T) theData[0]);
    }

    public Object saveState(FacesContext aContext) {
        ArrayList theData = new ArrayList();
        theData.add(getData());
        return theData.toArray();
    }
}
