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

import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.entity.*;
import de.powerstaff.business.service.*;
import de.powerstaff.web.backingbean.ContextUtils;
import de.powerstaff.web.backingbean.PersonEditorBackingBean;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FreelancerBackingBean
        extends
        PersonEditorBackingBean<Freelancer, FreelancerBackingBeanDataModel, FreelancerService> {

    private static final long serialVersionUID = 1951906917491517234L;

    private transient ProfileSearchService profileSearchService;

    private ContextUtils contextUtils;

    private ProjectService projectService;

    private TagService tagService;

    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void setContextUtils(ContextUtils contextUtils) {
        this.contextUtils = contextUtils;
    }

    @Override
    protected FreelancerBackingBeanDataModel createDataModel() {
        return new FreelancerBackingBeanDataModel();
    }

    public void setProfileSearchService(
            ProfileSearchService profileSearchService) {
        this.profileSearchService = profileSearchService;
    }

    @Override
    protected String getNavigationIDPrefix() {
        return "freelancer";
    }

    public List<String> getCodeSuggestion(Object aSuggest) {
        return entityService.getCodeSuggestions((String) aSuggest);
    }

    private void initTagLists() {

        Freelancer theFreelancer = getData().getEntity();

        getData().getTagsBemerkungen().clear();
        getData().getTagsBemerkungen().addAll(theFreelancer.getBemerkungenTags());

        getData().getTagsEinsatzorte().clear();
        getData().getTagsEinsatzorte().addAll(theFreelancer.getEinsatzorteTags());

        getData().getTagsFunktionen().clear();
        getData().getTagsFunktionen().addAll(theFreelancer.getFunktionenTags());

        getData().getTagsSchwerpunkte().clear();
        getData().getTagsSchwerpunkte().addAll(theFreelancer.getSchwerpunkteTags());
    }

    public List<Tag> getAvailableTagsBemerkungen() {
        return tagService.findTagsBy(TagType.BEMERKUNG);
    }

    public List<Tag> getAvailableTagsEinsatzorte() {
        return tagService.findTagsBy(TagType.EINSATZORT);
    }

    public List<Tag> getAvailableTagsFunktionen() {
        return tagService.findTagsBy(TagType.FUNKTION);
    }

    public List<Tag> getAvailableTagsSchwerpunkte() {
        return tagService.findTagsBy(TagType.SCHWERPUNKT);
    }

    public void addTagSchwerpunkte() {

        Freelancer theFreelancer = getData().getEntity();

        Tag theTag = getData().getNewSchwerpunkte();
        if (!theFreelancer.hasTag(theTag)) {

            FreelancerToTag theFreelancerToTag = new FreelancerToTag();
            theFreelancerToTag.setTag(theTag);
            theFreelancerToTag.setType(TagType.SCHWERPUNKT);
            theFreelancer.getTags().add(theFreelancerToTag);

            getData().setNewSchwerpunkte(null);
        }

        initTagLists();
    }

    public void addTagFunktionen() {

        Freelancer theFreelancer = getData().getEntity();

        Tag theTag = getData().getNewFunktion();
        if (!theFreelancer.hasTag(theTag)) {

            FreelancerToTag theFreelancerToTag = new FreelancerToTag();
            theFreelancerToTag.setTag(theTag);
            theFreelancerToTag.setType(TagType.FUNKTION);
            theFreelancer.getTags().add(theFreelancerToTag);

            getData().setNewFunktion(null);
        }

        initTagLists();
    }

    public void addTagEinsatzorte() {

        Freelancer theFreelancer = getData().getEntity();

        Tag theTag = getData().getNewEinsatzOrt();
        if (!theFreelancer.hasTag(theTag)) {

            FreelancerToTag theFreelancerToTag = new FreelancerToTag();
            theFreelancerToTag.setTag(theTag);
            theFreelancerToTag.setType(TagType.EINSATZORT);
            theFreelancer.getTags().add(theFreelancerToTag);

            getData().setNewEinsatzOrt(null);
        }

        initTagLists();
    }

    public void addTagBemerkungen() {

        Freelancer theFreelancer = getData().getEntity();

        Tag theTag = getData().getNewBemerkung();
        if (!theFreelancer.hasTag(theTag)) {

            FreelancerToTag theFreelancerToTag = new FreelancerToTag();
            theFreelancerToTag.setTag(theTag);
            theFreelancerToTag.setType(TagType.BEMERKUNG);
            theFreelancer.getTags().add(theFreelancerToTag);

            getData().setNewBemerkung(null);
        }

        initTagLists();
    }

    public void removeTagSchwerpunkt() {

        Freelancer theFreelancer = getData().getEntity();

        List<FreelancerToTag> theTagsToRemove = new ArrayList<FreelancerToTag>();
        for (FreelancerToTag theFreelancerToTag : theFreelancer.getTags()) {
            if (theFreelancerToTag.getTag().getId().equals(getData().getTagIdSchwerpunkt())) {
                theTagsToRemove.add(theFreelancerToTag);
            }
        }
        theFreelancer.getTags().removeAll(theTagsToRemove);

        initTagLists();
    }

    public void removeTagFunktion() {

        Freelancer theFreelancer = getData().getEntity();

        List<FreelancerToTag> theTagsToRemove = new ArrayList<FreelancerToTag>();
        for (FreelancerToTag theFreelancerToTag : theFreelancer.getTags()) {
            if (theFreelancerToTag.getTag().getId().equals(getData().getTagIdFunktion())) {
                theTagsToRemove.add(theFreelancerToTag);
            }
        }
        theFreelancer.getTags().removeAll(theTagsToRemove);

        initTagLists();
    }

    public void removeTagEinsatzort() {

        Freelancer theFreelancer = getData().getEntity();

        List<FreelancerToTag> theTagsToRemove = new ArrayList<FreelancerToTag>();
        for (FreelancerToTag theFreelancerToTag : theFreelancer.getTags()) {
            if (theFreelancerToTag.getTag().getId().equals(getData().getTagIdEinsatzOrt())) {
                theTagsToRemove.add(theFreelancerToTag);
            }
        }
        theFreelancer.getTags().removeAll(theTagsToRemove);

        initTagLists();
    }

    public void removeTagBemerkung() {

        Freelancer theFreelancer = getData().getEntity();

        List<FreelancerToTag> theTagsToRemove = new ArrayList<FreelancerToTag>();
        for (FreelancerToTag theFreelancerToTag : theFreelancer.getTags()) {
            if (theFreelancerToTag.getTag().getId().equals(getData().getTagIdBemerkung())) {
                theTagsToRemove.add(theFreelancerToTag);
            }

        }
        theFreelancer.getTags().removeAll(theTagsToRemove);

        initTagLists();
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

        initTagLists();
    }

    @Override
    protected Freelancer createNew() {
        return new Freelancer();
    }

    @Override
    public String commandSave() {
        try {
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

            return super.commandSave();

        } catch (OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
            return null;
        }
    }

    public List<ProjectPositionStatus> getPositionStatus() {
        List<ProjectPositionStatus> theResult = new ArrayList();
        theResult.addAll(projectService.getAvailablePositionStatus());
        Collections.sort(theResult);
        return theResult;
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

    public List<ProfileSearchEntry> getSimilarFreelancer() {
        return profileSearchService.getSimilarFreelancer(getData().getEntity());
    }
}