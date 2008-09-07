package de.powerstaff.web.backingbean.partner;

import java.util.Collection;

import de.mogwai.common.command.EditEntityCommand;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.mogwai.common.web.utils.UpdateModelInfo;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.Partner;
import de.powerstaff.business.entity.PartnerContact;
import de.powerstaff.business.entity.PartnerHistory;
import de.powerstaff.business.service.AdditionalDataService;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.PartnerService;
import de.powerstaff.business.service.TooManySearchResults;
import de.powerstaff.web.backingbean.NavigatingBackingBean;
import de.powerstaff.web.backingbean.freelancer.FreelancerBackingBean;

public class PartnerBackingBean extends NavigatingBackingBean<Partner, PartnerBackingBeanDataModel, PartnerService> {

    private AdditionalDataService additinalDataService;
    
    private FreelancerService freelancerService;

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
     *                the additinalDataService to set
     */
    public void setAdditinalDataService(AdditionalDataService additinalDataService) {
        this.additinalDataService = additinalDataService;
    }
    
    /**
     * @return the freelancerService
     */
    public FreelancerService getFreelancerService() {
        return freelancerService;
    }

    /**
     * @param freelancerService the freelancerService to set
     */
    public void setFreelancerService(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    public String commandSearch() {

        Collection<GenericSearchResult> theResult = null;
        try {
            theResult = entityService.performQBESearch(getData().getEntity());
        } catch (TooManySearchResults e) {
            theResult = e.getResult();
            JSFMessageUtils.addGlobalErrorMessage(MSG_ZUVIELESUCHERGEBNISSE);            
        }

        if (theResult.size() < 1) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_KEINEDATENGEFUNDEN);
            return null;
        }

        if (theResult.size() == 1) {
        
            GenericSearchResult theResult2 = (GenericSearchResult) theResult.iterator().next();
            getData().setEntity(entityService.findByPrimaryKey((Long) theResult2.get(GenericSearchResult.OBJECT_ID_KEY)));
            
            afterNavigation();
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

    public String commandFreiberufler() {
        return "PARTNER_FREIBERUFLER";
    }
    
    public void commandAddNewHistoryEntry() {
        PartnerHistory theHistory = new PartnerHistory();
        theHistory.setDescription(getData().getNewHistoryEntry());

        Partner thePartner = getData().getEntity();
        thePartner.getHistory().add(theHistory);
        
        entityService.save(thePartner);

        getData().setNewHistoryEntry(null);
        getData().setEntity(thePartner);

        JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGESPEICHERT);
    }
    
    public String commandSelectFreelancer() {
        
        Freelancer theFreelancer = (Freelancer) getData().getFreelancer().getRowData();
        forceUpdateOfBean(FreelancerBackingBean.class, new EditEntityCommand<Freelancer>(theFreelancer));
        return "FREELANCER_STAMMDATEN";
    }

    public String commandSelectSearchResult() {

        GenericSearchResult theResult = (GenericSearchResult) getData().getSearchResult().getRowData();
        Partner theEntity = entityService.findByPrimaryKey((Long) theResult.get(GenericSearchResult.OBJECT_ID_KEY));
        getData().setEntity(theEntity);

        afterNavigation();
        return "PARTNER_STAMMDATEN";
    }

    public void commandDeleteHistoryEntry() {

        PartnerHistory theHistory = (PartnerHistory) getData().getHistory().getRowData();
        getData().getHistory().remove(theHistory);

        Partner theFreelancer = getData().getEntity();
        entityService.save(theFreelancer);

        getData().setEntity(theFreelancer);

        JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGELOESCHT);
    }

    @Override
    public void updateModel(UpdateModelInfo aInfo) {
        super.updateModel(aInfo);
        if (aInfo.getCommand() instanceof EditEntityCommand) {
            EditEntityCommand<Freelancer> theCommand = (EditEntityCommand<Freelancer>) aInfo.getCommand();
            
            Partner theOldPartner = theCommand.getValue().getPartner();
            init();
            
            Partner thePartner = (Partner) entityService.findByPrimaryKey(theOldPartner.getId());
            getData().setEntity(thePartner);
            afterNavigation();
            
            getData().setOriginalFreelancer(theCommand.getValue());            
        }
    }

    @Override
    protected Partner createNew() {
        return new Partner();
    }

    @Override
    public void commandFirst() {
        super.commandFirst();
        getData().setOriginalFreelancer(null);
    }

    @Override
    public void commandLast() {
        super.commandLast();
        getData().setOriginalFreelancer(null);        
    }

    @Override
    public void commandNew() {
        super.commandNew();
        getData().setOriginalFreelancer(null);        
    }

    @Override
    public void commandNext() {
        super.commandNext();
        getData().setOriginalFreelancer(null);        
    }

    @Override
    public void commandPrior() {
        super.commandPrior();
        getData().setOriginalFreelancer(null);        
    }

    public String commandJumpToFreelancer() {
        forceUpdateOfBean(FreelancerBackingBean.class, new EditEntityCommand<Freelancer>(getData().getOriginalFreelancer()));
        return "FREELANCER_STAMMDATEN";
    }
    
    public void addFreelancer() {
        
        Freelancer theFreelancer = freelancerService.findRealFreelancerByCode(getData().getCodeToAdd());
        if (theFreelancer != null) {

            Partner thePartner = getData().getEntity();

            theFreelancer.setPartner(thePartner);
            freelancerService.save(theFreelancer);
            
            thePartner = entityService.findByPrimaryKey(thePartner.getId());
            getData().setEntity(thePartner);
            
            JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGESPEICHERT);                        
        } else {
            JSFMessageUtils.addGlobalErrorMessage(MSG_KEINEDATENGEFUNDEN);
        }
    }
}