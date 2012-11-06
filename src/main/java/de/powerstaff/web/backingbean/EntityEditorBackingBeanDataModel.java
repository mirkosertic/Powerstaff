package de.powerstaff.web.backingbean;

import de.mogwai.common.business.entity.Entity;
import de.mogwai.common.web.backingbean.BackingBeanDataModel;

public abstract class EntityEditorBackingBeanDataModel<T extends Entity>
        extends BackingBeanDataModel {

    public final static String NEW_ENTITY_ID = "new";

    private static final long serialVersionUID = -248382974035163220L;

    private T entity;

    private String currentEntityId;

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
     * @param entity the entity to set
     */
    public void setEntity(T entity) {
        this.entity = entity;
        if (entity != null) {
            Long theId = entity.getId();
            if (theId != null) {
                currentEntityId = theId.toString();
            } else {
                currentEntityId = NEW_ENTITY_ID;
            }
        } else {
            currentEntityId = NEW_ENTITY_ID;
        }
    }

    public String getCurrentEntityId() {
        return currentEntityId;
    }

    public void setCurrentEntityId(String currentEntityId) {
        this.currentEntityId = currentEntityId;
    }
}