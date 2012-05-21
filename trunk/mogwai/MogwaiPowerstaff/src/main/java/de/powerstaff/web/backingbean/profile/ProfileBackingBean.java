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
package de.powerstaff.web.backingbean.profile;

import de.mogwai.common.command.EditEntityCommand;
import de.mogwai.common.command.UpdateModelCommand;
import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.backingbean.WrappingBackingBean;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.dto.DataPage;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.dto.ProfileSearchInfoDetail;
import de.powerstaff.business.dto.ProfileSearchRequest;
import de.powerstaff.business.entity.Project;
import de.powerstaff.business.entity.ProjectPosition;
import de.powerstaff.business.entity.ProjectPositionStatus;
import de.powerstaff.business.entity.SavedProfileSearch;
import de.powerstaff.business.service.ProfileIndexerService;
import de.powerstaff.business.service.ProfileSearchService;
import de.powerstaff.web.backingbean.ContextUtils;
import de.powerstaff.web.backingbean.MessageConstants;
import de.powerstaff.web.backingbean.freelancer.FreelancerBackingBean;
import de.powerstaff.web.utils.PagedListDataModel;

import javax.faces.component.StateHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileBackingBean extends
        WrappingBackingBean<ProfileBackingBeanDataModel> implements
        MessageConstants, StateHolder {

    private static final long serialVersionUID = -5802587658636877536L;

    private static final Logger LOGGER = new Logger(ProfileBackingBean.class);

    private transient ProfileSearchService profileSearchService;

    private FreelancerBackingBean freelancerBackingBean;

    private ContextUtils contextUtils;

    public void setContextUtils(ContextUtils contextUtils) {
        this.contextUtils = contextUtils;
    }

    public void setFreelancerBackingBean(FreelancerBackingBean freelancerBackingBean) {
        this.freelancerBackingBean = freelancerBackingBean;
    }

    @Override
    protected ProfileBackingBeanDataModel createDataModel() {
        return new ProfileBackingBeanDataModel();
    }

    @Override
    public void init() {
        super.init();
        if (getData() != null) {
            getData().setViewRoot(null);
        } else {
            setData(createDataModel());
        }
    }

    public void setProfileSearchService(
            ProfileSearchService profileSearchService) {
        this.profileSearchService = profileSearchService;
    }

    public void initializeDataModel() {
        getData().setSearchResult(
                new PagedListDataModel<ProfileSearchEntry>(getPageSize()) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public DataPage<ProfileSearchEntry> fetchPage(int startRow,
                                                                  int pageSize) {
                        try {
                            return profileSearchService.findProfileDataPage(
                                    getData().getSearchRequest(), startRow,
                                    pageSize);
                        } catch (Exception e) {
                            JSFMessageUtils
                                    .addGlobalErrorMessage(
                                            MSG_FEHLERBEIDERPROFILSUCHE, e
                                            .getMessage());
                            LOGGER.logError("Fehler bei Profilsuche", e);
                        }
                        return new DataPage<ProfileSearchEntry>(0, 0,
                                new ArrayList<ProfileSearchEntry>());
                    }
                });
        if (getData().getDataScroller() != null) {
            getData().getDataScroller().setFirstRow(0);
        }
    }

    private void initializeFor(ProfileSearchRequest aRequest) {
        if (aRequest != null) {
            getData().setSearchRequest(aRequest);

            initializeDataModel();

            int theTotalResult = getData().getSearchResult().getRowCount();

            if (theTotalResult == 0) {
                JSFMessageUtils
                        .addGlobalErrorMessage(MSG_KEINEPROFILEGEFUNDEN);
            } else {
                JSFMessageUtils.addGlobalInfoMessage(MSG_PROFILEGEFUNDEN,
                        "" + theTotalResult);
            }
        } else {

            getData().setSearchRequest(new ProfileSearchRequest());

            profileSearchService.saveSearchRequest(getData().getSearchRequest(), true);

            initializeDataModel();
        }
    }

    public void commandClearContext() {
        contextUtils.commandClearContext();
        getData().setInitialized(false);
        resetNavigation();
    }

    @Override
    public void resetNavigation() {
        if (!getData().isInitialized()) {
            super.resetNavigation();

            try {
                ProfileSearchRequest theResult = profileSearchService
                        .getLastSearchRequest();

                initializeFor(theResult);
            } catch (Exception e) {
                JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIDERPROFILSUCHE,
                        e.getMessage());
                LOGGER.logError("Fehler bei Profilsuche", e);
            }

            getData().setInitialized(true);
        } else {
            try {
                initializeFor(getData().getSearchRequest());
            } catch (Exception e) {
                JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIDERPROFILSUCHE,
                        e.getMessage());
                LOGGER.logError("Fehler bei Profilsuche", e);
            }
        }
    }

    public void commandSaveToProject() {
        try {

            ProfileSearchRequest theRequest = getData().getSearchRequest();
            theRequest.setProject(contextUtils.getCurrentProject());

            profileSearchService
                    .saveSearchRequest(getData().getSearchRequest(), false);

            JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGESPEICHERT);
        } catch (Exception e) {

            LOGGER.logError("Fehler beim Speichern", e);
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIMSPEICHERN, e.getMessage());
        }
    }

    public void commandSearch() {
        try {
            profileSearchService
                    .saveSearchRequest(getData().getSearchRequest(), true);

            initializeDataModel();

            int theTotalResult = getData().getSearchResult().getRowCount();

            if (theTotalResult == 0) {
                JSFMessageUtils.addGlobalErrorMessage(MSG_KEINEPROFILEGEFUNDEN);
            } else {
                JSFMessageUtils.addGlobalInfoMessage(MSG_PROFILEGEFUNDEN, ""
                        + theTotalResult);
            }

        } catch (Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIDERPROFILSUCHE,
                    e.getMessage());
            LOGGER.logError("Fehler bei Profilsuche", e);
        }
    }

    public String commandSelectSearchResult() {
        freelancerBackingBean.updateModel(new EditEntityCommand<ProfileSearchInfoDetail>(
                ((ProfileSearchEntry) getData().getSearchResult()
                        .getRowData()).getFreelancer()));
        return "FREELANCER_STAMMDATEN";
    }

    public void commandDeleteSearchEntry() {

        ProfileSearchEntry theEntry = (ProfileSearchEntry) getData()
                .getSearchResult().getRowData();

        profileSearchService.removeSavedSearchEntry(getData().getSearchRequest(), theEntry.getDocumentId());

        initializeDataModel();

        JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGELOESCHT);
    }

    public boolean isTransient() {
        return false;
    }

    public void setTransient(boolean aValue) {
    }

    public void restoreState(FacesContext aContext, Object aValue) {
        Object[] theData = (Object[]) aValue;
        setData((ProfileBackingBeanDataModel) theData[0]);

        initializeDataModel();
    }

    public Object saveState(FacesContext aContext) {
        ArrayList theData = new ArrayList();
        theData.add(getData());
        return theData.toArray();
    }

    public void commandSortByName1() {
        getData().getSearchRequest().setSortierung(ProfileIndexerService.NAME1);

        profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
        initializeDataModel();
    }

    public void commandSortByName2() {
        getData().getSearchRequest().setSortierung(ProfileIndexerService.NAME2);

        profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
        initializeDataModel();
    }

    public void commandSortByPLZ() {
        getData().getSearchRequest().setSortierung(ProfileIndexerService.PLZ);

        profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
        initializeDataModel();
    }

    public void commandSortBySatz() {
        getData().getSearchRequest().setSortierung(
                ProfileIndexerService.STUNDENSATZ);

        profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
        initializeDataModel();
    }

    public void commandSortByVerf() {
        getData().getSearchRequest().setSortierung(
                ProfileIndexerService.VERFUEGBARKEIT);

        profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
        initializeDataModel();
    }

    public void commandSortByCode() {
        getData().getSearchRequest().setSortierung(ProfileIndexerService.CODE);

        profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
        initializeDataModel();
    }

    public int getPageSize() {
        return profileSearchService.getPageSize();
    }

    @Override
    public void updateModel(UpdateModelCommand aInfo) {
        super.updateModel(aInfo);
        if (aInfo instanceof EditEntityCommand) {

            EditEntityCommand theCommand = (EditEntityCommand) aInfo;

            SavedProfileSearch theSearch = (SavedProfileSearch) theCommand.getValue();
            ProfileSearchRequest theResult = profileSearchService.getSearchRequestFor(theSearch);

            initializeFor(theResult);
        }
    }

    private static final String POSITION_CACHE_ID = "PositionCache";

    public ProjectPositionStatus findStatusFor(long aFreelancerId, Project aProject) {

        ExternalContext theContext = FacesContext.getCurrentInstance().getExternalContext();

        Map<Long, ProjectPositionStatus> theStatusCache = (Map<Long, ProjectPositionStatus>) theContext.getRequestParameterMap().get(POSITION_CACHE_ID);
        if (theStatusCache == null) {
            theStatusCache = new HashMap<Long, ProjectPositionStatus>();
            for (ProjectPosition thePosition : aProject.getPositions()) {
                theStatusCache.put(thePosition.getFreelancerId(), thePosition.getStatus());
            }
            theContext.getRequestParameterMap().put(POSITION_CACHE_ID, theStatusCache);
        }
        return theStatusCache.get(aFreelancerId);
    }


    public String getRowStyleForProfile() {
        ProfileSearchEntry theEntry = (ProfileSearchEntry) getData().getSearchResult().getRowData();
        Project theCurrentProject = contextUtils.getCurrentProject();
        if (theCurrentProject != null) {
            ProjectPositionStatus theStatus = findStatusFor(theEntry.getFreelancer().getId(), theCurrentProject);
            if (theStatus != null) {
                return "background-color:" + theStatus.getColor();
            }
        }
        return null;
    }
}