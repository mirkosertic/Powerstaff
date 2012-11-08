package de.powerstaff.web.backingbean;

import de.mogwai.common.business.entity.Entity;

public abstract class NavigatingBackingBeanDataModel<T extends Entity> extends
        EntityEditorBackingBeanDataModel<T> {

    private static final long serialVersionUID = -4222492868578096147L;

    private Long recordNumber;

    protected NavigatingBackingBeanDataModel() {
    }

    protected NavigatingBackingBeanDataModel(T aValue) {
        super(aValue);
    }

    public Long getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(Long recordNumber) {
        this.recordNumber = recordNumber;
    }
}