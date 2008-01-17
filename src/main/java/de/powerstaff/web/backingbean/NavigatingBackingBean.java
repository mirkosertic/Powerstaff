package de.powerstaff.web.backingbean;

import de.mogwai.common.business.entity.Entity;
import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.service.NavigatingService;
import de.powerstaff.business.service.RecordInfo;

public abstract class NavigatingBackingBean<T extends Entity, V extends NavigatingBackingBeanDataModel, S extends NavigatingService<T>>
        extends EntityEditorBackingBean<V> {

    private static final Logger LOGGER = new Logger(NavigatingBackingBean.class);

    protected S entityService;

    /**
     * @return the entityService
     */
    public S getEntityService() {
        return entityService;
    }

    /**
     * @param entityService
     *            the entityService to set
     */
    public void setEntityService(S entityService) {
        this.entityService = entityService;
    }

    public void commandFirst() {

        T theEntity = (T) entityService.findFirst();
        getData().setEntity(theEntity);
        afterNavigation();
    }

    public void commandPrior() {

        T theCurrent = (T) getData().getEntity();

        T theEntity = (T) entityService.findPrior(theCurrent);
        getData().setEntity(theEntity);
        afterNavigation();
    }

    public void commandNext() {

        T theCurrent = (T) getData().getEntity();

        T theEntity = entityService.findNext(theCurrent);
        getData().setEntity(theEntity);
        afterNavigation();
    }

    public void commandLast() {

        T theEntity = (T) entityService.findLast();
        getData().setEntity(theEntity);
        afterNavigation();
    }

    protected abstract T createNew();

    public void commandNew() {
        getData().setEntity(createNew());
        afterNavigation();
    }

    protected void afterNavigation() {

    }

    public void commandDelete() {

        try {
            entityService.delete((T) getData().getEntity());
            commandFirst();
            JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGELOESCHT);

        } catch (Exception e) {

            LOGGER.logError("Fehler beim Speichern", e);
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIMSPEICHERN);
        }
    }

    public void commandSave() {
        try {
            entityService.save((T) getData().getEntity());
            afterNavigation();
            JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGESPEICHERT);
        } catch (Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIMSPEICHERN, e.getMessage());
        }
    }

    public String getRecordInfo() {
        RecordInfo theInfo = entityService.getRecordInfo((T) getData().getEntity());
        if (theInfo.getNumber() != 0) {
            return theInfo.getNumber() + " / " + theInfo.getCount();
        }

        return "Neu";
    }

}