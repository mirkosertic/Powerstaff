package de.powerstaff.web.backingbean.profile;

import java.util.Vector;

import javax.faces.component.UIComponent;

import de.mogwai.common.web.backingbean.BackingBeanDataModel;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.dto.ProfileSearchRequest;

public class ProfileBackingBeanDataModel extends BackingBeanDataModel {

    private ProfileSearchRequest searchRequest = new ProfileSearchRequest();

    private transient UIComponent viewRoot;

    private CollectionDataModel<ProfileSearchEntry> searchResult = new CollectionDataModel<ProfileSearchEntry>(
            new Vector<ProfileSearchEntry>());

    /**
     * @return the searchRequest
     */
    public ProfileSearchRequest getSearchRequest() {
        return searchRequest;
    }

    /**
     * @param searchRequest the searchRequest to set
     */
    public void setSearchRequest(ProfileSearchRequest searchRequest) {
        this.searchRequest = searchRequest;
    }

    /**
     * @return the viewRoot
     */
    public UIComponent getViewRoot() {
        return viewRoot;
    }

    /**
     * @param viewRoot
     *                the viewRoot to set
     */
    public void setViewRoot(UIComponent viewRoot) {
        this.viewRoot = viewRoot;
    }

    /**
     * @return the searchResult
     */
    public CollectionDataModel<ProfileSearchEntry> getSearchResult() {
        return searchResult;
    }

    public int getSearchResultSize() {
        return searchResult.size();
    }

    /**
     * @param searchResult the searchResult to set
     */
    public void setSearchResult(CollectionDataModel<ProfileSearchEntry> searchResult) {
        this.searchResult = searchResult;
    }
}