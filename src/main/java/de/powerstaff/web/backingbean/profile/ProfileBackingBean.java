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
package de.powerstaff.web.backingbean.profile;

import com.ocpsoft.pretty.util.FacesStateUtils;
import de.mogwai.common.usercontext.UserContextHolder;
import de.mogwai.common.web.backingbean.WrappingBackingBean;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.dto.DataPage;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerToTag;
import de.powerstaff.business.entity.Project;
import de.powerstaff.business.entity.ProjectPosition;
import de.powerstaff.business.entity.ProjectPositionStatus;
import de.powerstaff.business.entity.SavedProfileSearch;
import de.powerstaff.business.entity.Tag;
import de.powerstaff.business.entity.TagType;
import de.powerstaff.business.entity.User;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.OptimisticLockException;
import de.powerstaff.business.service.ProfileIndexerService;
import de.powerstaff.business.service.ProfileSearchService;
import de.powerstaff.business.service.TagService;
import de.powerstaff.web.backingbean.ContextUtils;
import de.powerstaff.web.backingbean.MessageConstants;
import de.powerstaff.web.utils.ExcelUtils;
import de.powerstaff.web.utils.PagedListDataModel;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.faces.component.StateHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileBackingBean extends
        WrappingBackingBean<ProfileBackingBeanDataModel> implements
        MessageConstants, StateHolder, InitializingBean {

    private static final long serialVersionUID = -5802587658636877536L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileBackingBean.class);

    private transient ProfileSearchService profileSearchService;

    private transient TagService tagService;

    private ContextUtils contextUtils;
    
    private transient FreelancerService freelancerService;

    public void setFreelancerService(final FreelancerService aService) {
        freelancerService = aService;
    }

    public void setContextUtils(final ContextUtils contextUtils) {
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
            final ProfileSearchService profileSearchService) {
        this.profileSearchService = profileSearchService;
    }

    public void setTagService(final TagService tagService) {
        this.tagService = tagService;
    }

    public void initializeDataModel() {

        final SavedProfileSearch savedProfileSearch = getData().getSearchRequest();
        getData().getTagSelection().clear();
        for (final Tag tag : tagService.findTagsBy(TagType.SEARCHABLE)) {
            if (savedProfileSearch.getSelectedTags() == null) {
                getData().getTagSelection().add(new TagSelectionState(tag, false));
            } else {
                getData().getTagSelection().add(new TagSelectionState(tag, savedProfileSearch.getSelectedTags().contains(tag.getId() + ";")));
            }
        }

        getData().setSearchResult(
                new PagedListDataModel<ProfileSearchEntry>(getPageSize()) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public DataPage<ProfileSearchEntry> fetchPage(final int startRow,
                                                                  final int pageSize) {
                        try {
                            return profileSearchService.findProfileDataPage(
                                    getData().getSearchRequest(), startRow,
                                    pageSize);
                        } catch (final Exception e) {
                            JSFMessageUtils
                                    .addGlobalErrorMessage(
                                            MSG_FEHLERBEIDERPROFILSUCHE, e
                                            .getMessage());
                            LOGGER.error("Fehler bei Profilsuche", e);
                        }
                        return new DataPage<>(0, 0,
                                new ArrayList<>());
                    }
                });
    }

    private void initializeFor(final SavedProfileSearch aRequest, final boolean aIsInit) throws OptimisticLockException {
        if (aRequest != null) {
            getData().setSearchRequest(aRequest);

            initializeDataModel();

            if (!aIsInit || (aIsInit && !new FacesStateUtils().isPostback())) {
                final int theTotalResult = getData().getSearchResult().getRowCount();

                if (theTotalResult == 0) {
                    JSFMessageUtils
                            .addGlobalErrorMessage(MSG_KEINEPROFILEGEFUNDEN);
                } else {
                    JSFMessageUtils.addGlobalInfoMessage(MSG_PROFILEGEFUNDEN,
                            "" + theTotalResult);
                }
            }
        } else {

            final SavedProfileSearch theSearch = new SavedProfileSearch();
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

        } catch (final Exception e) {

            LOGGER.error("Fehler beim Speichern", e);
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIMSPEICHERN, e.getMessage());
        }
        return null;
    }

    private void updateSelectedTags(final SavedProfileSearch savedProfileSearch) {
        final FacesContext context = FacesContext.getCurrentInstance();
        final HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        final String[] selectedTags = request.getParameterValues("selectedtag");
        if (selectedTags != null) {
            final StringBuilder result = new StringBuilder();
            for (final String element : selectedTags) {
                result.append(element).append(";");
            }
            savedProfileSearch.setSelectedTags(result.toString());
        } else {
            savedProfileSearch.setSelectedTags("");
        }
    }

    public void commandSearch() {
        try {
            final SavedProfileSearch savedProfileSearch = getData().getSearchRequest();

            updateSelectedTags(savedProfileSearch);

            profileSearchService
                    .saveSearchRequest(savedProfileSearch, true);

            initializeFor(savedProfileSearch, false);

        } catch (final Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIDERPROFILSUCHE,
                    e.getMessage());
            LOGGER.error("Fehler bei Profilsuche", e);
        }
    }

    public void commandSearchExportExcel() {
        try {
            final FacesContext theContext = FacesContext.getCurrentInstance();

            final ExternalContext externalContext = theContext.getExternalContext();
            final HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

            response.reset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=\"ExportSuche.xls\"");

            final HSSFWorkbook theWorkbook = new HSSFWorkbook();
            final HSSFSheet theWorkSheet = theWorkbook.createSheet("ExportSuche");

            final HSSFCellStyle theDateStyle = theWorkbook.createCellStyle();
            theDateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("d/m/jj"));

            int aRow = 0;
            // Header
            final HSSFRow theRow = theWorkSheet.createRow(aRow++);
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
            final PagedListDataModel<ProfileSearchEntry> theData = getData().getSearchResult();
            for (int i=0;i<theData.getRowCount() && i<profileSearchService.getPageSize();i++) {
                theData.setRowIndex(i);

                final ProfileSearchEntry theDataRow = (ProfileSearchEntry) theData.getRowData();

                final Freelancer theFreelancer = freelancerService.findByPrimaryKey(theDataRow.getFreelancer().getId());

                final String theSkills  = ExcelUtils.saveObject(theFreelancer.getSkills().replace("\f", "").replace("\n", "").replace("\t", ""));

                final StringBuilder theTagList = new StringBuilder();
                for (final FreelancerToTag theTagAssignment : theFreelancer.getTags()) {
                    if (theTagList.length() > 0) {
                        theTagList.append(" ");
                    }
                    theTagList.append(theTagAssignment.getTag().getName());
                }

                final HSSFRow theFreelancerRow = theWorkSheet.createRow(aRow++);
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

        } catch (final Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIDERPROFILSUCHE,
                    e.getMessage());
            LOGGER.error("Fehler bei Profilsuche", e);
        }
    }

    public void commandDeleteSearchEntry() {

        final ProfileSearchEntry theEntry = (ProfileSearchEntry) getData()
                .getSearchResult().getRowData();

        try {
            profileSearchService.removeSavedSearchEntry(getData().getSearchRequest(), theEntry.getDocumentId());

            initializeDataModel();

            JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGELOESCHT);
        } catch (final OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }

    public boolean isTransient() {
        return false;
    }

    public void setTransient(final boolean aValue) {
    }

    public void restoreState(final FacesContext aContext, final Object aValue) {
        final Object[] theData = (Object[]) aValue;
        setData((ProfileBackingBeanDataModel) theData[0]);

        initializeDataModel();
    }

    public Object saveState(final FacesContext aContext) {
        return new Object[] {getData()};
    }

    public void commandSortByName1() {
        getData().getSearchRequest().setSortierung(ProfileIndexerService.NAME1);

        try {
            profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
            initializeDataModel();

        } catch (final OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }

    public void commandSortByName2() {
        getData().getSearchRequest().setSortierung(ProfileIndexerService.NAME2);

        try {
            profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
            initializeDataModel();

        } catch (final OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }

    public void commandSortByPLZ() {
        getData().getSearchRequest().setSortierung(ProfileIndexerService.PLZ);

        try {
            profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
            initializeDataModel();

        } catch (final OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }

    public void commandSortBySatz() {
        getData().getSearchRequest().setSortierung(
                ProfileIndexerService.STUNDENSATZ);

        try {
            profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
            initializeDataModel();

        } catch (final OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }

    public void commandSortByLastContact() {
        getData().getSearchRequest().setSortierung(
                ProfileIndexerService.LETZTERKONTAKT);
        try {
            profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
            initializeDataModel();

        } catch (final OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }

    public void commandSortByVerf() {
        getData().getSearchRequest().setSortierung(
                ProfileIndexerService.VERFUEGBARKEIT);

        try {
            profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
            initializeDataModel();

        } catch (final OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }

    public void commandSortByCode() {
        getData().getSearchRequest().setSortierung(ProfileIndexerService.CODE);

        try {
            profileSearchService.saveSearchRequest(getData().getSearchRequest(), false);
            initializeDataModel();

        } catch (final OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }

    public int getPageSize() {
        return profileSearchService.getPageSize();
    }

    private static final String POSITION_CACHE_ID = "PositionCache";

    public ProjectPositionStatus findStatusFor(final long aFreelancerId, final Project aProject) {

        Map<Long, ProjectPositionStatus> theStatusCache = (Map<Long, ProjectPositionStatus>) ContextUtils.getCurrentCache().get(POSITION_CACHE_ID);
        if (theStatusCache == null) {
            theStatusCache = new HashMap<>();
            for (final ProjectPosition thePosition : aProject.getPositions()) {
                theStatusCache.put(thePosition.getFreelancerId(), thePosition.getStatus());
            }
            ContextUtils.getCurrentCache().put(POSITION_CACHE_ID, theStatusCache);
        }

        return theStatusCache.get(aFreelancerId);
    }


    public String getRowStyleForProfile() {
        final ProfileSearchEntry theEntry = (ProfileSearchEntry) getData().getSearchResult().getRowData();
        final Project theCurrentProject = contextUtils.getCurrentProject();
        if (theCurrentProject != null) {
            final ProjectPositionStatus theStatus = findStatusFor(theEntry.getFreelancer().getId(), theCurrentProject);
            if (theStatus != null) {
                return "background-color:" + theStatus.getColor();
            }
        }
        return null;
    }

    public void loadData() {
        try {
            if (ProfileBackingBeanDataModel.TYPE_USER.equals(getData().getType())) {

                final SavedProfileSearch theResult = profileSearchService
                        .getSearchRequestForUser(getData().getId());

                initializeFor(theResult, true);
            }
            if (ProfileBackingBeanDataModel.TYPE_SEARCH.equals(getData().getType())) {
                final SavedProfileSearch theResult = profileSearchService.getSearchRequest(Long.parseLong(getData().getId()));
                initializeFor(theResult, true);
            }
        } catch (final OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        } catch (final Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIDERPROFILSUCHE,
                    e.getMessage());
            LOGGER.error("Fehler bei Profilsuche", e);
        }
   }
}