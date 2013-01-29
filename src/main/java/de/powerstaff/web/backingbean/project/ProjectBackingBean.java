package de.powerstaff.web.backingbean.project;

import de.mogwai.common.usercontext.UserContextHolder;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.entity.*;
import de.powerstaff.business.service.*;
import de.powerstaff.web.backingbean.ContextUtils;
import de.powerstaff.web.backingbean.EntityEditorBackingBeanDataModel;
import de.powerstaff.web.backingbean.NavigatingBackingBean;
import de.powerstaff.web.backingbean.PersonEditorBackingBeanDataModel;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProjectBackingBean extends NavigatingBackingBean<Project, ProjectBackingBeanDataModel, ProjectService> {

    private static final long serialVersionUID = 8688601363580323078L;

    private ContextUtils contextUtils;
    private FreelancerService freelancerService;
    private CustomerService customerService;
    private PartnerService partnerService;
    private ProfileSearchService profileSearchService;
    private AdditionalDataService additionalDataService;

    public void setProfileSearchService(ProfileSearchService profileSearchService) {
        this.profileSearchService = profileSearchService;
    }

    public void setFreelancerService(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void setPartnerService(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    public void setContextUtils(ContextUtils contextUtils) {
        this.contextUtils = contextUtils;
    }

    @Override
    protected ProjectBackingBeanDataModel createDataModel() {
        return new ProjectBackingBeanDataModel();
    }

    @Override
    protected String getNavigationIDPrefix() {
        return "project";
    }

    public String commandSearch() {

        Collection<GenericSearchResult> theResult;
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
            return "pretty:" + getNavigationIDPrefix() + "main";
        }

        getData().getSearchResult().setWrappedData(theResult);
        return "PROJEKT_SEARCHRESULT";
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        commandNew();
    }

    @Override
    public String commandSave() {
        try {
            if (getData().getEntity().getContactPerson() != null) {
                return super.commandSave();
            } else {
                JSFMessageUtils.addGlobalErrorMessage(MSG_KEINKUNDE);
            }
        } catch (Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIMSPEICHERN, e.getMessage());
        }
        return null;
    }

    @Override
    protected Project createNew() {
        return new Project();
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        Project theCurrentProject = getData().getEntity();

        contextUtils.setCurrentProject(theCurrentProject);

        List<ProjectPosition> thePositions = new ArrayList();
        thePositions.addAll(theCurrentProject.getPositions());

        getData().getPositions().setWrappedData(thePositions);

        List<ProjectFirstContact> theFirstContacts = new ArrayList<ProjectFirstContact>();
        theFirstContacts.addAll(theCurrentProject.getFirstContacts());

        getData().getFirstContactPositions().setWrappedData(theFirstContacts);

        List<SavedProfileSearch> theSearches = new ArrayList<SavedProfileSearch>();
        if (theCurrentProject.getId() != null) {
            theSearches.addAll(entityService.getSavedSearchesFor(theCurrentProject));
        }
        Collections.sort(theSearches);

        getData().getSavedSearches().setWrappedData(theSearches);

        getData().setContactTypes(additionalDataService.getContactTypes());
    }

    public void commandDeletePosition() {
        try {

            Project theCurrentProject = getData().getEntity();
            ProjectPosition thePositionToDelete = (ProjectPosition) getData().getPositions().getRowData();
            theCurrentProject.getPositions().remove(thePositionToDelete);

            entityService.save(theCurrentProject);

            JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGELOESCHT);

            afterNavigation();

        } catch (Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIMLOESCHEN);
        }
    }

    public void commandDeleteFirstContact() {
        try {

            Project theCurrentProject = getData().getEntity();
            ProjectFirstContact thePositionToDelete = (ProjectFirstContact) getData().getFirstContactPositions().getRowData();
            theCurrentProject.getFirstContacts().remove(thePositionToDelete);

            entityService.save(theCurrentProject);

            JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGELOESCHT);

            afterNavigation();

        } catch (Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIMLOESCHEN);
        }
    }

    public void commandDeleteSavedSearch() {
        try {

            SavedProfileSearch theSearch = (SavedProfileSearch) getData().getSavedSearches().getRowData();

            entityService.deleteSavedSearch(theSearch);

            JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGELOESCHT);

            afterNavigation();

        } catch (Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIMLOESCHEN);
        }
    }

    public String getPositionFreelancerDescription() {
        ProjectPosition thePosition = (ProjectPosition) getData().getPositions().getRowData();
        Freelancer theFreelancer = freelancerService.findByPrimaryKey(thePosition.getFreelancerId());
        StringBuilder theBuilder = new StringBuilder();
        if (!StringUtils.isEmpty(theFreelancer.getName1())) {
            theBuilder.append(theFreelancer.getName1());
        }
        if (!StringUtils.isEmpty(theFreelancer.getName2())) {
            if (theBuilder.length() > 0) {
                theBuilder.append(" ");
            }
            theBuilder.append(theFreelancer.getName2());
        }
        if (!StringUtils.isEmpty(theFreelancer.getCode())) {
            if (theBuilder.length() > 0) {
                theBuilder.append(" ");
            }
            theBuilder.append("(");
            theBuilder.append(theFreelancer.getCode());
            theBuilder.append(")");
        }

        return theBuilder.toString();
    }

    public List<ProjectPositionStatus> getPositionStatus() {
        List<ProjectPositionStatus> theResult = new ArrayList();
        theResult.addAll(entityService.getAvailablePositionStatus());
        Collections.sort(theResult);
        return theResult;
    }

    @Override
    public void loadEntity() {

        String theCurrentType = getData().getCurrentType();
        String theCurrentTypeId = getData().getCurrentTypeId();

        super.loadEntity();

        getData().setCurrentType(theCurrentType);
        getData().setCurrentTypeId(theCurrentTypeId);

        if (EntityEditorBackingBeanDataModel.NEW_ENTITY_ID.equals(getData().getCurrentEntityId())) {
            if (ProjectBackingBeanDataModel.TYPE_CUSTOMER.equals(getData().getCurrentType())) {
                Customer theCustomer = customerService.findByPrimaryKey(Long.parseLong(getData().getCurrentTypeId()));
                getData().setCustomer(theCustomer);
            }
            if (ProjectBackingBeanDataModel.TYPE_PARTNER.equals(getData().getCurrentType())) {
                Partner thePartner = partnerService.findByPrimaryKey(Long.parseLong(getData().getCurrentTypeId()));
                getData().setPartner(thePartner);
            }
        }
    }

    public List<ProfileSearchEntry> getSimilarFreelancer() {
        return profileSearchService.getSimilarFreelancer(getData().getEntity());
    }

    public void setAdditionalDataService(AdditionalDataService additionalDataService) {
        this.additionalDataService = additionalDataService;
    }

    public void commandAddFirstContact() {

        if (StringUtils.isEmpty(getData().getNewFirstContactValue())) {

            JSFMessageUtils.addGlobalErrorMessage(MSG_KEINE_KONTAKTINFOS);
            return;
        }

        ProjectBackingBeanDataModel theModel = getData();

        Project theProject = theModel.getEntity();

        ProjectFirstContact theContact = new ProjectFirstContact();
        theContact.setCreationUserID(UserContextHolder.getUserContext().getAuthenticatable().getUserId());
        theContact.setName1(getData().getNewFirstContactName1());
        theContact.setName2(getData().getNewFirstContactName2());
        theContact.setContactType(getData().getNewFirstContactType());
        theContact.setContactTypeValue(getData().getNewFirstContactValue());
        theContact.setComment(getData().getNewFirstContactComment());

        theProject.getFirstContacts().add(theContact);

        getData().getFirstContactPositions().setWrappedData(new ArrayList(theProject.getFirstContacts()));
    }
}