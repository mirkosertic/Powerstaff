package de.powerstaff.web.backingbean.project;

import java.util.Collection;

import de.mogwai.common.command.EditEntityCommand;
import de.mogwai.common.command.UpdateModelCommand;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.mogwai.common.web.utils.UpdateModelInfo;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.entity.Customer;
import de.powerstaff.business.entity.Partner;
import de.powerstaff.business.entity.Project;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.ProfileSearchService;
import de.powerstaff.business.service.ProjectService;
import de.powerstaff.business.service.TooManySearchResults;
import de.powerstaff.web.backingbean.NavigatingBackingBean;
import de.powerstaff.web.backingbean.customer.CustomerBackingBean;
import de.powerstaff.web.backingbean.partner.PartnerBackingBean;

public class ProjectBackingBean extends NavigatingBackingBean<Project, ProjectBackingBeanDataModel, ProjectService> {

	private static final long serialVersionUID = 8688601363580323078L;

    private CustomerBackingBean customerBackingBean;
    private PartnerBackingBean partnerBackingBean;

    private ProfileSearchService profileSearchService;

    public ProfileSearchService getProfileSearchService() {
        return profileSearchService;
    }

    public void setProfileSearchService(ProfileSearchService profileSearchService) {
        this.profileSearchService = profileSearchService;
    }

    public PartnerBackingBean getPartnerBackingBean() {
        return partnerBackingBean;
    }

    public void setPartnerBackingBean(PartnerBackingBean partnerBackingBean) {
        this.partnerBackingBean = partnerBackingBean;
    }

    public CustomerBackingBean getCustomerBackingBean() {
        return customerBackingBean;
    }

    public void setCustomerBackingBean(CustomerBackingBean customerBackingBean) {
        this.customerBackingBean = customerBackingBean;
    }

    @Override
    protected ProjectBackingBeanDataModel createDataModel() {
        return new ProjectBackingBeanDataModel();
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
        return "PROJEKT_SEARCHRESULT";
    }

    @Override
    public void init() {
        super.init();
        commandNew();
    }

    @Override
    public void commandSave() {
        try {
            if (getData().getEntity().getContactPerson() != null) {
                super.commandSave();
            } else {
                JSFMessageUtils.addGlobalErrorMessage(MSG_KEINKUNDE);
            }
        } catch (Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIMSPEICHERN, e.getMessage());
        }
    }

    public String commandBack() {
        return "PROJEKT_STAMMDATEN";
    }

    public String commandStammdaten() {
        return "PROJEKT_STAMMDATEN";
    }

    public String commandSelectSearchResult() {

        GenericSearchResult theResult = (GenericSearchResult) getData().getSearchResult().getRowData();
        Project theEntity = entityService.findByPrimaryKey((Long) theResult.get(GenericSearchResult.OBJECT_ID_KEY));
        getData().setEntity(theEntity);

        afterNavigation();
        return "PROJEKT_STAMMDATEN";
    }

    public String commandShowCustomer() {
        Customer theCustomer = getData().getEntity().getCustomer();
        if (theCustomer != null) {

            customerBackingBean.updateModel(new EditEntityCommand<Customer>(theCustomer));
            return "CUSTOMER_STAMMDATEN";
        }

        Partner thePartner = getData().getEntity().getPartner();
        if (thePartner != null) {

            partnerBackingBean.updateModel(new EditEntityCommand<Partner>(thePartner));
            return "PARTNER_STAMMDATEN";
        }

        JSFMessageUtils.addGlobalErrorMessage(MSG_KEINKUNDE);
        return null;
    }

    @Override
    public void updateModel(UpdateModelCommand aInfo) {
        super.updateModel(aInfo);
        if (aInfo instanceof EditEntityCommand) {
            
            EditEntityCommand theCommand = (EditEntityCommand) aInfo;

            init();

            Project theProject = new Project();
            if (theCommand.getValue() instanceof Customer) {
                theProject.setCustomer((Customer) theCommand.getValue());
            } 
            if (theCommand.getValue() instanceof Partner) {
                theProject.setPartner((Partner) theCommand.getValue());
            } 

            getData().setEntity(theProject);
            afterNavigation();
        }
    }

    @Override
    protected Project createNew() {
        return new Project();
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        profileSearchService.findMatchingFreelancer(getData().getEntity());
   }
}