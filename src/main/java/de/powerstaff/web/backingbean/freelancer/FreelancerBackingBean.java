/*
  Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.

  This library is free software; you can redistribute it and/or modify it under
  the terms of the GNU Lesser General Public License as published by the Free
  Software Foundation; either version 2.1 of the License, or (at your option)
  any later version.

  This library is distributed in the hope that it will be useful, but WITHOUT
  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
  FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
  details.

  You should have received a copy of the GNU Lesser General Public License
  along with this library; if not, write to the Free Software Foundation, Inc.,
  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.powerstaff.web.backingbean.freelancer;

import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.entity.*;
import de.powerstaff.business.service.*;
import de.powerstaff.web.backingbean.ContextUtils;
import de.powerstaff.web.backingbean.PersonEditorBackingBean;
import de.powerstaff.web.backingbean.TagSelectionState;
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

    public void setTagService(final TagService tagService) {
        this.tagService = tagService;
    }

    public void setProjectService(final ProjectService projectService) {
        this.projectService = projectService;
    }

    public void setContextUtils(final ContextUtils contextUtils) {
        this.contextUtils = contextUtils;
    }

    @Override
    protected FreelancerBackingBeanDataModel createDataModel() {
        return new FreelancerBackingBeanDataModel();
    }

    public void setProfileSearchService(
            final ProfileSearchService profileSearchService) {
        this.profileSearchService = profileSearchService;
    }

    @Override
    protected String getNavigationIDPrefix() {
        return "freelancer";
    }

    public List<String> getCodeSuggestion(final Object aSuggest) {
        return entityService.getCodeSuggestions((String) aSuggest);
    }

    private void initTagLists() {

        final Freelancer theFreelancer = getData().getEntity();

        getData().getTagsBemerkungen().clear();
        getData().getTagsBemerkungen().addAll(theFreelancer.getBemerkungenTags());

        getData().getTagsEinsatzorte().clear();
        getData().getTagsEinsatzorte().addAll(theFreelancer.getEinsatzorteTags());

        getData().getTagsFunktionen().clear();
        getData().getTagsFunktionen().addAll(theFreelancer.getFunktionenTags());

        getData().getTagsSchwerpunkte().clear();
        getData().getTagsSchwerpunkte().addAll(theFreelancer.getSchwerpunkteTags());

        getData().getTagSelection().clear();
        for (final Tag tag : tagService.findTagsBy(TagType.SEARCHABLE)) {
            getData().getTagSelection().add(new TagSelectionState(tag, theFreelancer.hasTag(tag)));
        }
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

        final Freelancer theFreelancer = getData().getEntity();

        final Tag theTag = getData().getNewSchwerpunkte();
        if (theTag != null) {
            if (!theFreelancer.hasTag(theTag)) {

                final FreelancerToTag theFreelancerToTag = new FreelancerToTag();
                theFreelancerToTag.setTag(theTag);
                theFreelancerToTag.setType(TagType.SCHWERPUNKT);
                theFreelancer.getTags().add(theFreelancerToTag);

                getData().setNewSchwerpunkte(null);
            }

            initTagLists();
        } else {
            JSFMessageUtils.addGlobalErrorMessage(MSG_KEINTAGAUSGEWAEHLT);
        }

    }

    public void addTagFunktionen() {

        final Freelancer theFreelancer = getData().getEntity();

        final Tag theTag = getData().getNewFunktion();
        if (theTag != null) {
            if (!theFreelancer.hasTag(theTag)) {

                final FreelancerToTag theFreelancerToTag = new FreelancerToTag();
                theFreelancerToTag.setTag(theTag);
                theFreelancerToTag.setType(TagType.FUNKTION);
                theFreelancer.getTags().add(theFreelancerToTag);

                getData().setNewFunktion(null);
            }

            initTagLists();
        } else {
            JSFMessageUtils.addGlobalErrorMessage(MSG_KEINTAGAUSGEWAEHLT);
        }

    }

    public void addTagEinsatzorte() {

        final Freelancer theFreelancer = getData().getEntity();

        final Tag theTag = getData().getNewEinsatzOrt();
        if (theTag != null) {
            if (!theFreelancer.hasTag(theTag)) {

                final FreelancerToTag theFreelancerToTag = new FreelancerToTag();
                theFreelancerToTag.setTag(theTag);
                theFreelancerToTag.setType(TagType.EINSATZORT);
                theFreelancer.getTags().add(theFreelancerToTag);

                getData().setNewEinsatzOrt(null);
            }

            initTagLists();
        } else {
            JSFMessageUtils.addGlobalErrorMessage(MSG_KEINTAGAUSGEWAEHLT);
        }

    }

    public void addTagBemerkungen() {

        final Freelancer theFreelancer = getData().getEntity();

        final Tag theTag = getData().getNewBemerkung();
        if (theTag != null) {
            if (!theFreelancer.hasTag(theTag)) {

                final FreelancerToTag theFreelancerToTag = new FreelancerToTag();
                theFreelancerToTag.setTag(theTag);
                theFreelancerToTag.setType(TagType.BEMERKUNG);
                theFreelancer.getTags().add(theFreelancerToTag);

                getData().setNewBemerkung(null);
            }

            initTagLists();
        } else {
            JSFMessageUtils.addGlobalErrorMessage(MSG_KEINTAGAUSGEWAEHLT);
        }
    }

    public void removeTagSchwerpunkt() {

        final Freelancer theFreelancer = getData().getEntity();

        final List<FreelancerToTag> theTagsToRemove = new ArrayList<>();
        for (final FreelancerToTag theFreelancerToTag : theFreelancer.getTags()) {
            if (theFreelancerToTag.getTag().getId().equals(getData().getTagIdSchwerpunkt())) {
                theTagsToRemove.add(theFreelancerToTag);
            }
        }
        theFreelancer.getTags().removeAll(theTagsToRemove);

        initTagLists();
    }

    public void removeTagFunktion() {

        final Freelancer theFreelancer = getData().getEntity();

        final List<FreelancerToTag> theTagsToRemove = new ArrayList<>();
        for (final FreelancerToTag theFreelancerToTag : theFreelancer.getTags()) {
            if (theFreelancerToTag.getTag().getId().equals(getData().getTagIdFunktion())) {
                theTagsToRemove.add(theFreelancerToTag);
            }
        }
        theFreelancer.getTags().removeAll(theTagsToRemove);

        initTagLists();
    }

    public void removeTagEinsatzort() {

        final Freelancer theFreelancer = getData().getEntity();

        final List<FreelancerToTag> theTagsToRemove = new ArrayList<>();
        for (final FreelancerToTag theFreelancerToTag : theFreelancer.getTags()) {
            if (theFreelancerToTag.getTag().getId().equals(getData().getTagIdEinsatzOrt())) {
                theTagsToRemove.add(theFreelancerToTag);
            }
        }
        theFreelancer.getTags().removeAll(theTagsToRemove);

        initTagLists();
    }

    public void removeTagBemerkung() {

        final Freelancer theFreelancer = getData().getEntity();

        final List<FreelancerToTag> theTagsToRemove = new ArrayList<>();
        for (final FreelancerToTag theFreelancerToTag : theFreelancer.getTags()) {
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

        final Freelancer theFreelancer = getData().getEntity();

        getData().getProfiles().setWrappedData(profileSearchService.loadProfilesFor(theFreelancer));
        getData().setCurrentProjectPosition(new ProjectPosition());

        final Project theCurrentProject = contextUtils.getCurrentProject();
        if (theCurrentProject != null && theFreelancer.getId() != null) {
            for (final ProjectPosition thePosition : theCurrentProject.getPositions()) {
                if (thePosition.getFreelancerId() == theFreelancer.getId()) {
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
            final Project theCurrentProject = contextUtils.getCurrentProject();
            if (theCurrentProject != null) {
                final ProjectPosition thePosition = getData().getCurrentProjectPosition();
                if (thePosition.getId() == null) {
                    // Position ist noch nicht persistent, wird aber erst dem Projekt zugeordnet, wenn auch was eingetragen wurde
                    if (!StringUtils.isEmpty(thePosition.getComment()) || (!StringUtils.isEmpty(thePosition.getConditions())) || thePosition.getStatus() != null) {
                        thePosition.setProject(theCurrentProject);
                        theCurrentProject.addPosition(thePosition);
                        thePosition.setFreelancerId(getData().getEntity().getId());
                    }
                } else {
                    for (final ProjectPosition thePersistentPos : theCurrentProject.getPositions()) {
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

        } catch (final OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
            return null;
        }
    }

    public List<ProjectPositionStatus> getPositionStatus() {
        final List<ProjectPositionStatus> theResult = new ArrayList<>(projectService.getAvailablePositionStatus());
        Collections.sort(theResult);
        return theResult;
    }


    public String getProfileOpenCommand() {
        final FreelancerProfile theProfile = (FreelancerProfile) getData()
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