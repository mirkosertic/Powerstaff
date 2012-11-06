package de.powerstaff.web.backingbean;

import de.mogwai.common.business.entity.Entity;
import de.mogwai.common.web.component.ComponentUtils;

import javax.faces.context.FacesContext;
import java.util.List;

public abstract class NavigatingBackingBeanDataModel<T extends Entity> extends
        EntityEditorBackingBeanDataModel<T> {

    private static final long serialVersionUID = -4222492868578096147L;

    private Long recordNumber;

    protected NavigatingBackingBeanDataModel() {
    }

    protected NavigatingBackingBeanDataModel(T aValue) {
        super(aValue);
    }

    public List<String> getChangedComponents() {

        List<String> theResult = ComponentUtils
                .getDynamicContentComponentIDs(FacesContext
                        .getCurrentInstance().getViewRoot());
        ComponentUtils.addModalComponentIDs(theResult, FacesContext
                .getCurrentInstance().getViewRoot());
        return theResult;
    }

    public Long getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(Long recordNumber) {
        this.recordNumber = recordNumber;
    }
}