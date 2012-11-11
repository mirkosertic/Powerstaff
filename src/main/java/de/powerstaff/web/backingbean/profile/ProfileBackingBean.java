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

import com.ocpsoft.pretty.util.FacesStateUtils;
import de.mogwai.common.logging.Logger;
import de.mogwai.common.usercontext.UserContextHolder;
import de.mogwai.common.web.backingbean.WrappingBackingBean;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.dto.DataPage;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.entity.*;
import de.powerstaff.business.service.ProfileIndexerService;
import de.powerstaff.business.service.ProfileSearchService;
import de.powerstaff.web.backingbean.ContextUtils;
import de.powerstaff.web.backingbean.MessageConstants;
import de.powerstaff.web.utils.PagedListDataModel;
import org.springframework.beans.factory.InitializingBean;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileBackingBean extends
        WrappingBackingBean<ProfileBackingBeanDataModel> implements
        MessageConstants, StateHolder, InitializingBean {

    private static final long serialVersionUID = -5802587658636877536L;

    private static final Logger LOGGER = new Logger(ProfileBackingBean.class);

    private transient ProfileSearchService profileSearchService;

    private ContextUtils contextUtils;

    public void setContextUtils(ContextUtils contextUtils) {
        this.contextUtils = contextUtils;
    }

    @Override
    protected ProfileBackingBeanDataModel createDataModel() {
        return new ProfileBackingBeanDataModel();
    }

    @Override
    public void afterPropertiesSet() {
        setData(createDataModel());
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

    private void initializeFor(SavedProfileSearch aRequest, boolean aIsInit) {
        if (aRequest != null) {
            getData().setSearchRequest(aRequest);

            initializeDataModel();

            if (!aIsInit || (aIsInit && !new FacesStateUtils().isPostback())) {
                int theTotalResult = getData().getSearchResult().getRowCount();

                if (theTotalResult == 0) {
                    JSFMessageUtils
                            .addGlobalErrorMessage(MSG_KEINEPROFILEGEFUNDEN);
                } else {
                    JSFMessageUtils.addGlobalInfoMessage(MSG_PROFILEGEFUNDEN,
                            "" + theTotalResult);
                }
            }
        } else {

            SavedProfileSearch theSearch = new SavedProfileSearch();
            theSearch.setUser((User) UserContextHolder.getUserContext().getAuthenticatable());
            getData().setSearchRequest(theSearch);

            profileSearchService.saveSearchRequest(getData().getSearchRequest(), true);

            initializeDataModel();
        }
    }

    public String commandSaveToProject() {
        try {

            SavedProfileSearch theRequest = getData().getSearchRequest();

            theRequest = (SavedProfileSearch) theRequest.clone();
            theRequest.setProject(contextUtils.getCurrentProject());
            theRequest.setUser((User) UserContextHolder.getUserContext().getAuthenticatable());

            profileSearchService
                    .saveSearchRequest(theRequest, false);

            // Wir haben jetzt einen neuen Eintrag
            getData().setSearchRequest(theRequest);

            JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGESPEICHERT);

            // Redirect auf die URL des neuen Eintrags
            return "pretty:profilemain";

        } catch (Exception e) {

            LOGGER.logError("Fehler beim Speichern", e);
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIMSPEICHERN, e.getMessage());
        }
        return null;
    }

    public void commandSearch() {
        try {
            profileSearchService
                    .saveSearchRequest(getData().getSearchRequest(), true);

            initializeFor(getData().getSearchRequest(), false);

        } catch (Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIDERPROFILSUCHE,
                    e.getMessage());
            LOGGER.logError("Fehler bei Profilsuche", e);
        }
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

    private static final String POSITION_CACHE_ID = "PositionCache";

    public ProjectPositionStatus findStatusFor(long aFreelancerId, Project aProject) {

        Map<Long, ProjectPositionStatus> theStatusCache = (Map<Long, ProjectPositionStatus>) ContextUtils.getCurrentCache().get(POSITION_CACHE_ID);
        if (theStatusCache == null) {
            theStatusCache = new HashMap<Long, ProjectPositionStatus>();
            for (ProjectPosition thePosition : aProject.getPositions()) {
                theStatusCache.put(thePosition.getFreelancerId(), thePosition.getStatus());
            }
            ContextUtils.getCurrentCache().put(POSITION_CACHE_ID, theStatusCache);
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

    public void loadData() {

        try {
            if (ProfileBackingBeanDataModel.TYPE_USER.equals(getData().getType())) {
                SavedProfileSearch theResult = profileSearchService
                        .getSearchRequestForUser(getData().getId());

                initializeFor(theResult, true);
            }
            if (ProfileBackingBeanDataModel.TYPE_SEARCH.equals(getData().getType())) {
                SavedProfileSearch theResult = profileSearchService.getSearchRequest(Long.parseLong(getData().getId()));
                initializeFor(theResult, true);
            }
        } catch (Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIDERPROFILSUCHE,
                    e.getMessage());
            LOGGER.logError("Fehler bei Profilsuche", e);
        }

    }
}