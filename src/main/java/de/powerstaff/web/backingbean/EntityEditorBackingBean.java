package de.powerstaff.web.backingbean;

import de.mogwai.common.command.ResetNavigationInfo;
import de.mogwai.common.web.backingbean.WrappingBackingBean;

public abstract class EntityEditorBackingBean<T extends EntityEditorBackingBeanDataModel>
		extends WrappingBackingBean<T> implements MessageConstants {

	@Override
	public void init() {
		super.init();
		setData(createDataModel());
	}

	@Override
	public void resetNavigation(ResetNavigationInfo aInfo) {
		super.resetNavigation(aInfo);
		init();
	}
}
