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

import java.util.ArrayList;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

import de.mogwai.common.command.EditEntityCommand;
import de.mogwai.common.command.ResetNavigationInfo;
import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.backingbean.WrappingBackingBean;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.dto.DataPage;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.dto.ProfileSearchInfoDetail;
import de.powerstaff.business.dto.ProfileSearchRequest;
import de.powerstaff.business.service.ProfileIndexerService;
import de.powerstaff.business.service.ProfileSearchService;
import de.powerstaff.web.backingbean.MessageConstants;
import de.powerstaff.web.backingbean.freelancer.FreelancerBackingBean;
import de.powerstaff.web.utils.PagedListDataModel;
import org.springframework.beans.factory.InitializingBean;

public class ProfileBackingBean extends
		WrappingBackingBean<ProfileBackingBeanDataModel> implements
		MessageConstants, StateHolder, InitializingBean {

	private static final long serialVersionUID = -5802587658636877536L;

	private static final Logger LOGGER = new Logger(ProfileBackingBean.class);

	private transient ProfileSearchService profileSearchService;

    private FreelancerBackingBean freelancerBackingBean;

    public FreelancerBackingBean getFreelancerBackingBean() {
        return freelancerBackingBean;
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

	public ProfileSearchService getProfileSearchService() {
		return profileSearchService;
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

	@Override
	public void resetNavigation() {
		super.resetNavigation();

		try {
			ProfileSearchRequest theResult = profileSearchService
					.getLastSearchRequest();
			if (theResult != null) {
				getData().setSearchRequest(theResult);

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

				initializeDataModel();
			}
		} catch (Exception e) {
			JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIDERPROFILSUCHE,
					e.getMessage());
			LOGGER.logError("Fehler bei Profilsuche", e);
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

		profileSearchService.removeSavedSearchEntry(theEntry.getDocumentId());

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
    public void afterPropertiesSet() throws Exception {
        resetNavigation();
    }
}