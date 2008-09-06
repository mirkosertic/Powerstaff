package de.powerstaff.web.backingbean.profile;

import java.util.ArrayList;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

import de.mogwai.common.command.EditEntityCommand;
import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.backingbean.WrappingBackingBean;
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

    public void commandSearch() {
        try {
            ProfileSearchResult theResult = profileSearchService.searchDocument(getData().getSearchRequest()); 
            getData().getSearchResult().setWrappedData(theResult.getEnties());

            if (theResult.getEnties().size() == 0) {
                JSFMessageUtils.addGlobalErrorMessage(MSG_KEINEPROFILEGEFUNDEN);
            } else {
                JSFMessageUtils.addGlobalInfoMessage(MSG_PROFILEGEFUNDEN, "" + theResult.getEnties().size(), "" + theResult.getTotalFound());
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
}
