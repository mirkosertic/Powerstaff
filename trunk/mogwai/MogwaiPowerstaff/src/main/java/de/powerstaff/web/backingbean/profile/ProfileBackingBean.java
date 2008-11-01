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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.TransformingComparator;

import de.mogwai.common.command.EditEntityCommand;
import de.mogwai.common.command.ResetNavigationInfo;
import de.mogwai.common.logging.Logger;
import de.mogwai.common.utils.DateComparator;
import de.mogwai.common.utils.LongComparator;
import de.mogwai.common.utils.NullsafeComparator;
import de.mogwai.common.utils.StringLowerCaseTransformer;
import de.mogwai.common.web.backingbean.WrappingBackingBean;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.dto.ProfileSearchInfoDetail;
import de.powerstaff.business.dto.ProfileSearchResult;
import de.powerstaff.business.service.ProfileSearchService;
import de.powerstaff.web.backingbean.MessageConstants;
import de.powerstaff.web.backingbean.freelancer.FreelancerBackingBean;

public class ProfileBackingBean extends WrappingBackingBean<ProfileBackingBeanDataModel> implements MessageConstants,
        StateHolder {

    private static final Logger LOGGER = new Logger(ProfileBackingBean.class);

    private ProfileSearchService profileSearchService;
    
    private static final Comparator STRINGCOMPARATOR = new NullsafeComparator(new TransformingComparator(new StringLowerCaseTransformer()));
    private static final Comparator LONGCOMPARATOR = new NullsafeComparator(LongComparator.INSTANCE);
    private static final Comparator DATECOMPARATOR = new NullsafeComparator(DateComparator.INSTANCE);

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

    /**
     * @return the profileSearchService
     */
    public ProfileSearchService getProfileSearchService() {
        return profileSearchService;
    }

    /**
     * @param profileSearchService
     *                the profileSearchService to set
     */
    public void setProfileSearchService(ProfileSearchService profileSearchService) {
        this.profileSearchService = profileSearchService;
    }

    @Override
    public void resetNavigation(ResetNavigationInfo aInfo) {
        super.resetNavigation(aInfo);

        try {
            ProfileSearchResult theResult = profileSearchService.getLastSearchResult();
            if (theResult != null) {
                getData().setSearchRequest(theResult.getSearchRequest());
                getData().getSearchResult().setWrappedData(theResult.getEnties());

                if (theResult.getEnties().size() == 0) {
                    JSFMessageUtils.addGlobalErrorMessage(MSG_KEINEPROFILEGEFUNDEN);
                } else {
                    JSFMessageUtils.addGlobalInfoMessage(MSG_PROFILEGEFUNDEN, "" + theResult.getEnties().size(), ""
                            + theResult.getTotalFound());
                }
            }
        } catch (Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIDERPROFILSUCHE, e.getMessage());
            LOGGER.logError("Fehler bei Profilsuche", e);
        }
    }

    public void commandSearch() {
        try {
            ProfileSearchResult theResult = profileSearchService.searchDocument(getData().getSearchRequest());
            getData().getSearchResult().setWrappedData(theResult.getEnties());

            if (theResult.getEnties().size() == 0) {
                JSFMessageUtils.addGlobalErrorMessage(MSG_KEINEPROFILEGEFUNDEN);
            } else {
                JSFMessageUtils.addGlobalInfoMessage(MSG_PROFILEGEFUNDEN, "" + theResult.getEnties().size(), ""
                        + theResult.getTotalFound());
            }

        } catch (Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIDERPROFILSUCHE, e.getMessage());
            LOGGER.logError("Fehler bei Profilsuche", e);
        }
    }

    public String commandSelectSearchResult() {
        forceUpdateOfBean(FreelancerBackingBean.class, new EditEntityCommand<ProfileSearchInfoDetail>(
                ((ProfileSearchEntry) getData().getSearchResult().getRowData()).getFreelancer()));
        return "FREELANCER_STAMMDATEN";
    }

    public void commandDeleteSearchEntry() {
        ProfileSearchEntry theEntry = (ProfileSearchEntry) getData().getSearchResult().getRowData();
        getData().getSearchResult().remove(theEntry);

        profileSearchService.removeSavedSearchEntry(theEntry.getSavedSearchEntry());

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
    }

    public Object saveState(FacesContext aContext) {
        ArrayList theData = new ArrayList();
        theData.add(getData());
        return theData.toArray();
    }

    public void commandSortByName1() {
        List theData = (List) getData().getSearchResult().getWrappedData();
        Collections.sort(theData, new BeanComparator("freelancer.name1", STRINGCOMPARATOR));
        getData().setSearchResult(new CollectionDataModel<ProfileSearchEntry>(theData));
    }

    public void commandSortByName2() {
        List theData = (List) getData().getSearchResult().getWrappedData();
        Collections.sort(theData, new BeanComparator("freelancer.name2", STRINGCOMPARATOR));
        getData().setSearchResult(new CollectionDataModel<ProfileSearchEntry>(theData));
    }

    public void commandSortByPLZ() {
        List theData = (List) getData().getSearchResult().getWrappedData();
        Collections.sort(theData, new BeanComparator("freelancer.plz", STRINGCOMPARATOR));
        getData().setSearchResult(new CollectionDataModel<ProfileSearchEntry>(theData));
    }

    public void commandSortBySatz() {
        List theData = (List) getData().getSearchResult().getWrappedData();
        Collections.sort(theData, new BeanComparator("freelancer.sallaryLong", LONGCOMPARATOR));
        getData().setSearchResult(new CollectionDataModel<ProfileSearchEntry>(theData));
    }

    public void commandSortByVerf() {
        List theData = (List) getData().getSearchResult().getWrappedData();
        Collections.sort(theData, new BeanComparator("freelancer.availabilityAsDate", DATECOMPARATOR));
        getData().setSearchResult(new CollectionDataModel<ProfileSearchEntry>(theData));
    }

    public void commandSortByCode() {
        List theData = (List) getData().getSearchResult().getWrappedData();
        Collections.sort(theData, new BeanComparator("code" , STRINGCOMPARATOR));
        getData().setSearchResult(new CollectionDataModel<ProfileSearchEntry>(theData));
    }
}
