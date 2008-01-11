package de.powerstaff.web.backingbean;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import de.mogwai.common.business.entity.Entity;
import de.mogwai.common.web.component.ComponentUtils;

public abstract class NavigatingBackingBeanDataModel<T extends Entity> extends EntityEditorBackingBeanDataModel<T> {

	private UIComponent viewRoot;

	protected NavigatingBackingBeanDataModel() {
	}

	protected NavigatingBackingBeanDataModel(T aValue) {
		super(aValue);
	}
	
	/**
	 * @return the viewRoot
	 */
	public UIComponent getViewRoot() {
		return null;
	}

	/**
	 * @param viewRoot the viewRoot to set
	 */
	public void setViewRoot(UIComponent viewRoot) {
		this.viewRoot = viewRoot;
	}
	
	public List<String> getChangedComponents() {
		List<String> theResult = ComponentUtils.getDynamicContentComponentIDs(viewRoot);
		ComponentUtils.addModalComponentIDs(theResult, FacesContext.getCurrentInstance().getViewRoot());
		return theResult;
	}
}