/**
 * Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.powerstaff.web.backingbean.freelancer;

import de.mogwai.common.command.EditEntityCommand;
import de.mogwai.common.command.UpdateModelCommand;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.dto.ProfileSearchInfoDetail;
import de.powerstaff.business.entity.*;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.ProfileSearchService;
import de.powerstaff.business.service.ProjectService;
import de.powerstaff.web.backingbean.ContextUtils;
import de.powerstaff.web.backingbean.PersonEditorBackingBean;
import de.powerstaff.web.backingbean.partner.PartnerBackingBean;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FreelancerBackingBean
        extends
        PersonEditorBackingBean<Freelancer, FreelancerBackingBeanDataModel, FreelancerService> {

    private static final long serialVersionUID = 1951906917491517234L;

    private transient ProfileSearchService profileSearchService;

    private PartnerBackingBean partnerBackingBean;

    private ContextUtils contextUtils;

    private ProjectService projectService;

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void setContextUtils(ContextUtils contextUtils) {
        this.contextUtils = contextUtils;
    }

    public void setPartnerBackingBean(PartnerBackingBean partnerBackingBean) {
        this.partnerBackingBean = partnerBackingBean;
    }

    @Override
    protected FreelancerBackingBeanDataModel createDataModel() {
        return new FreelancerBackingBeanDataModel();
    }

    /**
     * @param profileSearchService the profileSearchService to set
     */
    public void setProfileSearchService(
            ProfileSearchService profileSearchService) {
        this.profileSearchService = profileSearchService;
    }

    public List<String> getCodeSuggestion(Object aSuggest) {
        return entityService.getCodeSuggestions((String) aSuggest);
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        Freelancer theFreelancer = getData().getEntity();

        getData().getProfiles().setWrappedData(profileSearchService.loadProfilesFor(theFreelancer));
        getData().setCurrentProjectPosition(new ProjectPosition());

        Project theCurrentProject = contextUtils.getCurrentProject();
        if (theCurrentProject != null && theFreelancer.getId() != null) {
            for (ProjectPosition thePosition : theCurrentProject.getPositions()) {
                if (thePosition.getFreelancerId() == theFreelancer.getId().longValue()) {
                    getData().setCurrentProjectPosition(thePosition);
                }
            }
        }

        if (theFreelancer.getId() != null) {
            getData().getPositions().setWrappedData(entityService.findPositionsFor(theFreelancer));
        } else {
            getData().getPositions().setWrappedData(new ArrayList<ProjectPosition>());
        }
    }

    @Override
    protected Freelancer createNew() {
        return new Freelancer();
    }

    @Override
    public void commandDelete() {
        Freelancer theFreelancer = getData().getEntity();

        super.commandDelete();
    }

    @Override
    public void commandSave() {
        Project theCurrentProject = contextUtils.getCurrentProject();
        if (theCurrentProject != null) {
            ProjectPosition thePosition = getData().getCurrentProjectPosition();
            if (thePosition.getId() == null) {
                // Position ist noch nicht persistent, wird aber erst dem Projekt zugeordnet, wenn auch was eingetragen wurde
                if (!StringUtils.isEmpty(thePosition.getComment()) || (!StringUtils.isEmpty(thePosition.getConditions())) || thePosition.getStatus() != null) {
                    thePosition.setProject(theCurrentProject);
                    theCurrentProject.addPosition(thePosition);
                    thePosition.setFreelancerId(getData().getEntity().getId());
                }
            } else {
                for (ProjectPosition thePersistentPos : theCurrentProject.getPositions()) {
                    if (thePersistentPos.equals(thePosition)) {
                        thePersistentPos.setComment(thePosition.getComment());
                        thePersistentPos.setConditions(thePosition.getConditions());
                        thePersistentPos.setStatus(thePosition.getStatus());
                    }
                }
            }
            projectService.save(theCurrentProject);
        }

        super.commandSave();
    }

    public String commandShowPartner() {
        Partner thePartner = getData().getEntity().getPartner();
        if (thePartner != null) {
            partnerBackingBean.updateModel(new EditEntityCommand<Freelancer>(getData().getEntity()));
            return "PARTNER_STAMMDATEN";
        }
        return null;
    }

    public List<ProjectPositionStatus> getPositionStatus() {
        List<ProjectPositionStatus> theResult = new ArrayList();
        theResult.addAll(projectService.getAvailablePositionStatus());
        Collections.sort(theResult);
        return theResult;
    }

    @Override
    public void updateModel(UpdateModelCommand aInfo) {
        super.updateModel(aInfo);
        if (aInfo instanceof EditEntityCommand) {
            EditEntityCommand theCommand = (EditEntityCommand) aInfo;

            afterPropertiesSet();
            if (theCommand.getValue() instanceof ProfileSearchInfoDetail) {

                ProfileSearchInfoDetail theDetails = (ProfileSearchInfoDetail) theCommand
                        .getValue();
                Freelancer theFreelancer = entityService
                        .findByPrimaryKey(theDetails.getId());
                if (theFreelancer != null) {
                    getData().setEntity(theFreelancer);
                } else {
                    // Freelancer wurde gelöscht, wir springen zu einem neuen Eintrag
                    JSFMessageUtils.addGlobalErrorMessage(MSG_KEINEDATENGEFUNDEN);
                    commandNew();
                }
            }

            if (theCommand.getValue() instanceof Freelancer) {
                Freelancer theDetails = (Freelancer) theCommand.getValue();
                Freelancer theFreelancer = entityService
                        .findByPrimaryKey(theDetails.getId());
                getData().setEntity(theFreelancer);
            }

            afterNavigation();
        }
    }

    public String getProfileOpenCommand() {
        FreelancerProfile theProfile = (FreelancerProfile) getData()
                .getProfiles().getRowData();
        if (theProfile.isWordProfile()) {
            return "return openWordFile('"
                    + theProfile.getFileName().replace("\\", "\\\\") + "')";
        }
        return "return openTextFile('"
                + theProfile.getFileName().replace("\\", "\\\\") + "')";
    }

    @Override
    protected FreelancerContact createNewContact() {
        return new FreelancerContact();
    }

    @Override
    protected FreelancerHistory createNewHistory() {
        return new FreelancerHistory();
    }

    public boolean isAssignedToCurrentProject() {
        return getData().getCurrentProjectPosition() != null && getData().getEntity().getId() != null;
    }
}