package de.powerstaff.web.backingbean.partner;

import java.util.Collection;

import de.mogwai.common.command.EditEntityCommand;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.mogwai.common.web.utils.UpdateModelInfo;
import de.powerstaff.business.entity.Partner;
import de.powerstaff.business.entity.PartnerContact;
import de.powerstaff.business.entity.PartnerHistory;
import de.powerstaff.business.service.AdditionalDataService;
import de.powerstaff.business.service.PartnerService;
import de.powerstaff.web.backingbean.NavigatingBackingBean;
import de.powerstaff.web.utils.Comparators;

public class PartnerBackingBean extends NavigatingBackingBean<Partner, PartnerBackingBeanDataModel, PartnerService> {

    private AdditionalDataService additinalDataService;

    @Override
    protected PartnerBackingBeanDataModel createDataModel() {
        return new PartnerBackingBeanDataModel();
    }

    /**
     * @return the additinalDataService
     */
    public AdditionalDataService getAdditinalDataService() {
        return additinalDataService;
    }

    /**
     * @param additinalDataService
     *            the additinalDataService to set
     */
    public void setAdditinalDataService(AdditionalDataService additinalDataService) {
        this.additinalDataService = additinalDataService;
    }

    public String commandSearch() {

        Collection theResult = entityService.performQBESearch(getData().getEntity());

        if (theResult.size() < 1) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_KEINEDATENGEFUNDEN);
            return null;
        }

        if (theResult.size() == 1) {
            getData().setEntity((Partner) theResult.iterator().next());
            return null;
        }

        getData().getSearchResult().setWrappedData(theResult);
        return "PARTNER_SEARCHRESULT";
    }

    public void commandAddContact() {

        if ((getData().getNewContactValue() == null) || ("".equals(getData().getNewContactValue()))) {

            JSFMessageUtils.addGlobalErrorMessage(MSG_KEINE_KONTAKTINFOS);

            return;
        }

        PartnerBackingBeanDataModel theModel = getData();

        Partner theFreelancer = theModel.getEntity();

        PartnerContact theContact = new PartnerContact();
        theContact.setType(theModel.getNewContactType());
        theContact.setValue(theModel.getNewContactValue());

        theFreelancer.getContacts().add(theContact);

        theModel.setEntity(theFreelancer);
    }

    public void commandDeleteContact() {

        PartnerBackingBeanDataModel theModel = getData();

        Partner theFreelancer = theModel.getEntity();

        PartnerContact theContact = (PartnerContact) theModel.getContacts().getRowData();
        theFreelancer.getContacts().remove(theContact);

        theModel.setEntity(theFreelancer);
    }

    @Override
    public void init() {
        super.init();

        getData().setContactTypes(additinalDataService.getContactTypes());

        commandNew();
    }

    public String commandBack() {
        return "PARTNER_STAMMDATEN";
    }

    public String commandStammdaten() {
        return "PARTNER_STAMMDATEN";
    }

    public String commandHistorie() {
        return "PARTNER_HISTORIE";
    }

    public void commandAddNewHistoryEntry() {
        PartnerHistory theHistory = new PartnerHistory();
        theHistory.setDescription(getData().getNewHistoryEntry());

        getData().setNewHistoryEntry(null);

        Partner theFreelancer = getData().getEntity();
        getData().getHistory().add(theHistory);
        getData().getHistory().sort(Comparators.INVERSECREATIONDATECOMPARATOR);

        entityService.save(theFreelancer);

        getData().setNewHistoryEntry(null);

        JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGESPEICHERT);
    }

    public String commandSelectSearchResult() {

        getData().setEntity((Partner) getData().getSearchResult().getRowData());
        return "PARTNER_STAMMDATEN";
    }

    public void commandDeleteHistoryEntry() {

        PartnerHistory theHistory = (PartnerHistory) getData().getHistory().getRowData();
        getData().getHistory().remove(theHistory);

        Partner theFreelancer = getData().getEntity();
        entityService.save(theFreelancer);

        getData().setEntity(theFreelancer);
        getData().getHistory().sort(Comparators.INVERSECREATIONDATECOMPARATOR);

        JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGELOESCHT);
    }

    @Override
    public void updateModel(UpdateModelInfo aInfo) {
        super.updateModel(aInfo);
        if (aInfo.getCommand() instanceof EditEntityCommand) {
            EditEntityCommand<Partner> theCommand = (EditEntityCommand<Partner>) aInfo.getCommand();

            init();

            Partner thePartner = (Partner) entityService.findByPrimaryKey(theCommand.getValue().getId());
            getData().setEntity(thePartner);
            afterNavigation();
        }
    }

    @Override
    protected Partner createNew() {
        return new Partner();
    }
}
