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
import de.mogwai.common.usercontext.UserContextHolder;
import de.mogwai.common.web.backingbean.WrappingBackingBean;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.dto.DataPage;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.entity.*;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.OptimisticLockException;
import de.powerstaff.business.service.ProfileIndexerService;
import de.powerstaff.business.service.ProfileSearchService;
import de.powerstaff.web.backingbean.ContextUtils;
import de.powerstaff.web.backingbean.MessageConstants;
import de.powerstaff.web.utils.ExcelUtils;
import de.powerstaff.web.utils.PagedListDataModel;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.faces.component.StateHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileBackingBean extends
        WrappingBackingBean<ProfileBackingBeanDataModel> implements
        MessageConstants, StateHolder, InitializingBean {

    private static final long serialVersionUID = -5802587658636877536L;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ProfileBackingBean.class);

    private transient ProfileSearchService profileSearchService;

    private ContextUtils contextUtils;
    
    private transient FreelancerService freelancerService;

    public void setFreelancerService(FreelancerService aService) {
        freelancerService = aService;
    }

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
                            LOGGER.error("Fehler bei Profilsuche", e);
                        }
                        return new DataPage<ProfileSearchEntry>(0, 0,
                                new ArrayList<ProfileSearchEntry>());
                    }
                });
    }

    private void initializeFor(SavedProfileSearch aRequest, boolean aIsInit) throws OptimisticLockException {
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

            LOGGER.error("Fehler beim Speichern", e);
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
            LOGGER.error("Fehler bei Profilsuche", e);
        }
    }

    public void commandSearchExportExcel() {
        try {
            FacesContext theContext = FacesContext.getCurrentInstance();

            ExternalContext externalContext = theContext.getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

            response.reset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=\"ExportSuche.xls\"");

            HSSFWorkbook theWorkbook = new HSSFWorkbook();
            HSSFSheet theWorkSheet = theWorkbook.createSheet("ExportSuche");

            HSSFCellStyle theDateStyle = theWorkbook.createCellStyle();
            theDateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("d/m/jj"));

            int aRow = 0;
            // Header
            HSSFRow theRow = theWorkSheet.createRow(aRow++);
            ExcelUtils.addCellToRow(theRow, 0, "Anrede");
            ExcelUtils.addCellToRow(theRow, 1, "Name1");
            ExcelUtils.addCellToRow(theRow, 2, "Name2");
            ExcelUtils.addCellToRow(theRow, 3, "eMail");
            ExcelUtils.addCellToRow(theRow, 4, "Code");
            ExcelUtils.addCellToRow(theRow, 5, "Verfügbarkeit");
            ExcelUtils.addCellToRow(theRow, 6, "Satz");
            ExcelUtils.addCellToRow(theRow, 7, "Plz");
            ExcelUtils.addCellToRow(theRow, 8, "Letzter Kontakt");
            ExcelUtils.addCellToRow(theRow, 9, "Skills");
            ExcelUtils.addCellToRow(theRow, 10, "Tags");

            // Rows
            PagedListDataModel<ProfileSearchEntry> theData = getData().getSearchResult();
            for (int i=0;i<theData.getRowCount();i++) {
                theData.setRowIndex(i);

                ProfileSearchEntry theDataRow = (ProfileSearchEntry) theData.getRowData();

                Freelancer theFreelancer = freelancerService.findByPrimaryKey(theDataRow.getFreelancer().getId());

                String theSkills  = ExcelUtils.saveObject(theFreelancer.getSkills().replace("\f", "").replace("\n", "").replace("\t", ""));

                StringBuilder theTagList = new StringBuilder();
                for (FreelancerToTag theTagAssignment : theFreelancer.getTags()) {
                    if (theTagList.length() > 0) {
                        theTagList.append(" ");
                    }
                    theTagList.append(theTagAssignment.getTag().getName());
                }

                HSSFRow theFreelancerRow = theWorkSheet.createRow(aRow++);
                ExcelUtils.addCellToRow(theFreelancerRow, 0, ExcelUtils.saveObject(theFreelancer.getTitel()));
                ExcelUtils.addCellToRow(theFreelancerRow, 1, ExcelUtils.saveObject(theFreelancer.getName1()));
                ExcelUtils.addCellToRow(theFreelancerRow, 2, ExcelUtils.saveObject(theFreelancer.getName2()));
                ExcelUtils.addCellToRow(theFreelancerRow, 3, ExcelUtils.saveObject(theFreelancer.getFirstContactEMail())); // eMail
                ExcelUtils.addCellToRow(theFreelancerRow, 4, ExcelUtils.saveObject(theFreelancer.getCode()));
                ExcelUtils.addCellToRow(theFreelancerRow, 5, ExcelUtils.saveObject(theFreelancer.getAvailabilityAsDate()), theDateStyle);
                ExcelUtils.addCellToRow(theFreelancerRow, 6, ExcelUtils.saveObject(theFreelancer.getSallaryLong()));
                ExcelUtils.addCellToRow(theFreelancerRow, 7, ExcelUtils.saveObject(theFreelancer.getPlz()));
                ExcelUtils.addCellToRow(theFreelancerRow, 8, ExcelUtils.saveObject(theFreelancer.getLastContactDate()), theDateStyle);
                ExcelUtils.addCellToRow(theFreelancerRow, 9, ExcelUtils.saveObject(theSkills));
                ExcelUtils.addCellToRow(theFreelancerRow, 10, theTagList.toString());
            }

            theWorkbook.write(response.getOutputStream());

            theContext.responseComplete(); // Important!

        } catch (Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIDERPROFILSUCHE,
                    e.getMessage());
            LOGGER.error("Fehler bei Profilsuche", e);
        }
    }

    public void commandDeleteSearchEntry() {

        ProfileSearchEntry theEntry = (ProfileSearchEntry) getData()
                .getSearchResult().getRowData();

        try {
            profileSearchService.removeSavedSearchEntry(getData().getSearchRequest(), theEntry.getDocumentId());

            initializeDataModel();

            JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGELOESCHT);
        } catch (OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
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

        try {
            profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
            initializeDataModel();

        } catch (OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }

    public void commandSortByName2() {
        getData().getSearchRequest().setSortierung(ProfileIndexerService.NAME2);

        try {
            profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
            initializeDataModel();

        } catch (OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }

    public void commandSortByPLZ() {
        getData().getSearchRequest().setSortierung(ProfileIndexerService.PLZ);

        try {
            profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
            initializeDataModel();

        } catch (OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }

    public void commandSortBySatz() {
        getData().getSearchRequest().setSortierung(
                ProfileIndexerService.STUNDENSATZ);

        try {
            profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
            initializeDataModel();

        } catch (OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }

    public void commandSortByLastContact() {
        getData().getSearchRequest().setSortierung(
                ProfileIndexerService.LETZTERKONTAKT);
        try {
            profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
            initializeDataModel();

        } catch (OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }

    public void commandSortByVerf() {
        getData().getSearchRequest().setSortierung(
                ProfileIndexerService.VERFUEGBARKEIT);

        try {
            profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
            initializeDataModel();

        } catch (OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }

    public void commandSortByCode() {
        getData().getSearchRequest().setSortierung(ProfileIndexerService.CODE);

        try {
            profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
            initializeDataModel();

        } catch (OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
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
        } catch (OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        } catch (Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIDERPROFILSUCHE,
                    e.getMessage());
            LOGGER.error("Fehler bei Profilsuche", e);
        }
   }
}