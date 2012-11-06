package de.powerstaff.web.backingbean;

import de.mogwai.common.business.entity.Entity;
import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.service.NavigatingService;
import de.powerstaff.business.service.RecordInfo;
import de.powerstaff.business.service.ReferenceExistsException;

public abstract class NavigatingBackingBean<T extends Entity, V extends NavigatingBackingBeanDataModel, S extends NavigatingService<T>>
        extends EntityEditorBackingBean<V> {

    private static final long serialVersionUID = -7505086677065098879L;

    private static final Logger LOGGER = new Logger(NavigatingBackingBean.class);

    protected S entityService;

    public void reload() {
        T theEntity = (T) getData().getEntity();
        if (theEntity != null && theEntity.getId() != null) {
            getData().setEntity(entityService.findByPrimaryKey(theEntity.getId()));
            afterNavigation();
        } else {
            getData().setCurrentEntityId(NavigatingBackingBeanDataModel.NEW_ENTITY_ID);
            afterNavigation();
        }
    }

    public void setEntityService(S entityService) {
        this.entityService = entityService;
    }

    protected abstract String getNavigationIDPrefix();

    public String commandFirst() {

        T theEntity = entityService.findFirst();
        if (theEntity != null) {
            getData().setEntity(theEntity);
        } else {
            getData().setCurrentEntityId(NavigatingBackingBeanDataModel.NEW_ENTITY_ID);
        }
        return "pretty:" + getNavigationIDPrefix() + "main";
    }

    public String commandPrior() {

        T theCurrent = (T) getData().getEntity();

        T theEntity = entityService.findPrior(theCurrent);
        if (theEntity != null) {
            getData().setEntity(theEntity);
        } else {
            getData().setCurrentEntityId(NavigatingBackingBeanDataModel.NEW_ENTITY_ID);
        }
        return "pretty:" + getNavigationIDPrefix() + "main";
    }

    public String commandNext() {

        T theCurrent = (T) getData().getEntity();

        T theEntity = entityService.findNext(theCurrent);
        if (theEntity != null) {
            getData().setEntity(theEntity);
        } else {
            getData().setCurrentEntityId(NavigatingBackingBeanDataModel.NEW_ENTITY_ID);
        }
        return "pretty:" + getNavigationIDPrefix() + "main";
    }

    public String commandLast() {

        T theEntity = entityService.findLast();

        if (theEntity != null) {
            getData().setEntity(theEntity);
        } else {
            getData().setCurrentEntityId(NavigatingBackingBeanDataModel.NEW_ENTITY_ID);
        }
        return "pretty:" + getNavigationIDPrefix() + "main";
    }

    protected abstract T createNew();

    public String commandNew() {
        getData().setEntity(createNew());
        getData().setCurrentEntityId(NavigatingBackingBeanDataModel.NEW_ENTITY_ID);

        return "pretty:" + getNavigationIDPrefix() + "main";
    }

    protected void afterNavigation() {
        getData().setRecordNumber(null);
    }

    public String commandDelete() {

        try {
            entityService.delete((T) getData().getEntity());
            JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGELOESCHT);

            return commandNext();

        } catch (ReferenceExistsException e) {

            LOGGER.logError("Fehler beim Löschen", e);
            JSFMessageUtils.addGlobalErrorMessage(MSG_ESEXISTIERENABHAENGIGEDATEN);

        } catch (Exception e) {

            LOGGER.logError("Fehler beim Löschen", e);
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIMLOESCHEN);
        }
        return null;
    }

    public String commandSave() {
        try {
            entityService.save((T) getData().getEntity());
            getData().setCurrentEntityId(getData().getEntity().getId().toString());

            JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGESPEICHERT);

            return "pretty:" + getNavigationIDPrefix() + "main";
        } catch (Exception e) {

            LOGGER.logError("Fehler beim Speichern", e);
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIMSPEICHERN, e.getMessage());
        }
        return null;
    }

    public String commandJumpToRecord() {

        T theEntity = entityService.findByRecordNumber(getData().getRecordNumber());
        getData().setEntity(theEntity);
        getData().setCurrentEntityId(theEntity.getId().toString());

        return "pretty:" + getNavigationIDPrefix() + "main";
    }

    public String getRecordInfo() {
        RecordInfo theInfo = entityService.getRecordInfo((T) getData().getEntity());
        if (theInfo.getNumber() != 0) {
            return theInfo.getNumber() + " / " + theInfo.getCount();
        }

        return "Neu";
    }

    /**
     * Wird von PrettyFaces aufgerufen, wenn die BackingBean mit den Werten aus der REST-URL befüllt wurde.
     * <p/>
     * Ist also eine PageAction, um die Initialbefüllung der BackingBean vorzunehmen.
     */
    public void loadEntity() {
        if (EntityEditorBackingBeanDataModel.NEW_ENTITY_ID.equals(getData().getCurrentEntityId())) {
            commandNew();
        } else {
            Entity theEntity = entityService
                    .findByPrimaryKey(Long.parseLong(getData().getCurrentEntityId()));
            if (theEntity != null) {
                getData().setEntity(theEntity);
            } else {
                commandNew();
            }
        }
        afterNavigation();
    }
}