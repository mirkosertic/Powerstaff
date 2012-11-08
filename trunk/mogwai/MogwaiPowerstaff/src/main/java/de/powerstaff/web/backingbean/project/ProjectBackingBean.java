package de.powerstaff.web.backingbean.project;

import de.mogwai.common.command.EditEntityCommand;
import de.mogwai.common.command.UpdateModelCommand;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.entity.*;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.ProjectService;
import de.powerstaff.business.service.TooManySearchResults;
import de.powerstaff.web.backingbean.ContextUtils;
import de.powerstaff.web.backingbean.NavigatingBackingBean;
import de.powerstaff.web.backingbean.profile.ProfileBackingBean;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProjectBackingBean extends NavigatingBackingBean<Project, ProjectBackingBeanDataModel, ProjectService> {

    private static final long serialVersionUID = 8688601363580323078L;

    private ProfileBackingBean profileBackingBean;
    private ContextUtils contextUtils;
    private FreelancerService freelancerService;

    public void setProfileBackingBean(ProfileBackingBean profileBackingBean) {
        this.profileBackingBean = profileBackingBean;
    }

    public void setFreelancerService(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
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
            return null;
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
    public void updateModel(UpdateModelCommand aInfo) {
        super.updateModel(aInfo);
        if (aInfo instanceof EditEntityCommand) {

            EditEntityCommand theCommand = (EditEntityCommand) aInfo;

            afterPropertiesSet();

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

        Project theCurrentProject = getData().getEntity();

        contextUtils.setCurrentProject(theCurrentProject);

        List<ProjectPosition> thePositions = new ArrayList();
        thePositions.addAll(theCurrentProject.getPositions());

        getData().getPositions().setWrappedData(thePositions);

        List<SavedProfileSearch> theSearches = new ArrayList<SavedProfileSearch>();
        if (theCurrentProject.getId() != null) {
            theSearches.addAll(entityService.getSavedSearchesFor(theCurrentProject));
        }
        Collections.sort(theSearches);

        getData().getSavedSearches().setWrappedData(theSearches);
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

    public String commandOpenSavedSearch() {
        SavedProfileSearch theSearch = (SavedProfileSearch) getData().getSavedSearches().getRowData();

        profileBackingBean.updateModel(new EditEntityCommand<SavedProfileSearch>(theSearch));
        return "PROFILE_STAMMDATEN";
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
}