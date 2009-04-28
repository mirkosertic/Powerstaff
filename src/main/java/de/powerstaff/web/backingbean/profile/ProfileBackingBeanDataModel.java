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