package de.powerstaff.web.backingbean;

import de.mogwai.common.web.backingbean.BackingBeanDataModel;

public abstract class EntityEditorBackingBeanDataModel<T extends Object>
		extends BackingBeanDataModel {

	private static final long serialVersionUID = -248382974035163220L;

	private T entity;

	public EntityEditorBackingBeanDataModel() {
		initialize();
	}

	public EntityEditorBackingBeanDataModel(T aValue) {
		entity = aValue;
	}

	protected abstract void initialize();

	/**
	 * @return the entity
	 */
	public T getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(T entity) {
		this.entity = entity;
	}
}