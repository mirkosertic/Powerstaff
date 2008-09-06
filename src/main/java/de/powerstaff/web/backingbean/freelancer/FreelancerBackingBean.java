package de.powerstaff.web.backingbean.freelancer;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import de.mogwai.common.command.EditEntityCommand;
import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.mogwai.common.web.utils.UpdateModelInfo;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerContact;
import de.powerstaff.business.entity.FreelancerHistory;
import de.powerstaff.business.entity.FreelancerProfile;
import de.powerstaff.business.entity.Partner;
import de.powerstaff.business.service.AdditionalDataService;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.ProfileSearchInfoDetail;
import de.powerstaff.business.service.ProfileSearchService;
import de.powerstaff.business.service.TooManySearchResults;
import de.powerstaff.web.backingbean.NavigatingBackingBean;
import de.powerstaff.web.backingbean.partner.PartnerBackingBean;
import de.powerstaff.web.utils.Comparators;

public class FreelancerBackingBean extends
        NavigatingBackingBean<Freelancer, FreelancerBackingBeanDataModel, FreelancerService> {

    private static final Logger LOGGER = new Logger(FreelancerBackingBean.class);

    private ProfileSearchService profileSearchService;

    private AdditionalDataService additinalDataService;

    @Override
    protected FreelancerBackingBeanDataModel createDataModel() {
        return new FreelancerBackingBeanDataModel();
    }

    /**
     * @return the profileSearchService
     */
    public ProfileSearchService getProfileSearchService() {
        return profileSearchService;
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
     * @param profileSearchService
     *                the profileSearchService to set
     */
    public void setProfileSearchService(ProfileSearchService profileSearchService) {
        this.profileSearchService = profileSearchService;
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
        return "FREELANCER_SEARCHRESULT";
    }

    public void commandAddContact() {

        if ((getData().getNewContactValue() == null) || ("".equals(getData().getNewContactValue()))) {

            JSFMessageUtils.addGlobalErrorMessage(MSG_KEINE_KONTAKTINFOS);

            return;
        }

        FreelancerBackingBeanDataModel theModel = getData();

        Freelancer theFreelancer = theModel.getEntity();

        FreelancerContact theContact = new FreelancerContact();
        theContact.setType(theModel.getNewContactType());
        theContact.setValue(theModel.getNewContactValue());

        theFreelancer.getContacts().add(theContact);
        theModel.setEntity(theFreelancer);
    }

    public void commandDeleteContact() {

        FreelancerBackingBeanDataModel theModel = getData();

        Freelancer theFreelancer = theModel.getEntity();

        FreelancerContact theContact = (FreelancerContact) theModel.getContacts().getRowData();
        theFreelancer.getContacts().remove(theContact);

        theModel.setEntity(theFreelancer);
    }

    @Override
    public void init() {
        super.init();

        getData().setContactTypes((List<ContactType>) additinalDataService.getContactTypes());

        commandNew();
    }

    public List<String> getCodeSuggestion(Object aSuggest) {
        return entityService.getCodeSuggestions((String) aSuggest);
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        String theCode = getData().getEntity().getCode();
        if ((theCode != null) && (theCode.length() > 0)) {
            try {
                getData().getProfiles().setWrappedData(profileSearchService.findProfiles(theCode));
            } catch (Exception e) {
                JSFMessageUtils.addGlobalErrorMessage(MSG_KEINEDATENGEFUNDEN, e.getMessage());
                LOGGER.logError("Konnte Profilliste nicht laden f�r " + theCode, e);
            }
        } else {
            getData().getProfiles().setWrappedData(new Vector());
        }
    }

    @Override
    protected Freelancer createNew() {
        return new Freelancer();
    }

    public String commandBack() {
        return "FREELANCER_STAMMDATEN";
    }

    public String commandStammdaten() {
        return "FREELANCER_STAMMDATEN";
    }

    public String commandHistorie() {
        return "FREELANCER_HISTORIE";
    }

    public void commandAddNewHistoryEntry() {
        FreelancerHistory theHistory = new FreelancerHistory();
        theHistory.setDescription(getData().getNewHistoryEntry());

        Freelancer theFreelancer = getData().getEntity();
        theFreelancer.getHistory().add(theHistory);

        entityService.save(theFreelancer);

        getData().setNewHistoryEntry(null);
        getData().setEntity(theFreelancer);

        JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGESPEICHERT);
    }

    public String commandSelectSearchResult() {

        GenericSearchResult theResult = (GenericSearchResult) getData().getSearchResult().getRowData();
        Freelancer theEntity = entityService.findByPrimaryKey((Long) theResult.get(GenericSearchResult.OBJECT_ID_KEY));
        getData().setEntity(theEntity);
        afterNavigation();
        return "FREELANCER_STAMMDATEN";
    }

    public void commandDeleteHistoryEntry() {

        FreelancerHistory theHistory = (FreelancerHistory) getData().getHistory().getRowData();
        getData().getHistory().remove(theHistory);

        Freelancer theFreelancer = getData().getEntity();
        entityService.save(theFreelancer);

        getData().setEntity(theFreelancer);
        getData().getHistory().sort(Comparators.INVERSECREATIONDATECOMPARATOR);

        JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGELOESCHT);
    }

    public String commandShowPartner() {
        Partner thePartner = getData().getEntity().getPartner();
        if (thePartner != null) {
            forceUpdateOfBean(PartnerBackingBean.class, new EditEntityCommand<Freelancer>(getData().getEntity()));
            return "PARTNER_STAMMDATEN";
        }
        return null;
    }

    @Override
    public void updateModel(UpdateModelInfo aInfo) {
        super.updateModel(aInfo);
        if (aInfo.getCommand() instanceof EditEntityCommand) {
            EditEntityCommand theCommand = (EditEntityCommand) aInfo
                    .getCommand();

            init();
            if (theCommand.getValue() instanceof ProfileSearchInfoDetail) {

                ProfileSearchInfoDetail theDetails = (ProfileSearchInfoDetail) theCommand.getValue();
                Freelancer theFreelancer = (Freelancer) entityService.findByPrimaryKey(theDetails.getId());
                getData().setEntity(theFreelancer);
            }
            
            if (theCommand.getValue() instanceof Freelancer) {
                Freelancer theDetails = (Freelancer) theCommand.getValue();
                Freelancer theFreelancer = (Freelancer) entityService.findByPrimaryKey(theDetails.getId());
                getData().setEntity(theFreelancer);
            }
            
            afterNavigation();
        }
    }

    public String getProfileOpenCommand() {
        FreelancerProfile theProfile = (FreelancerProfile) getData().getProfiles().getRowData();
        if (theProfile.isWordProfile()) {
            return "return openWordFile('" + theProfile.getFileName().replace("\\", "\\\\") + "')";
        }
        return "return openTextFile('" + theProfile.getFileName().replace("\\", "\\\\") + "')";        
    }
}
