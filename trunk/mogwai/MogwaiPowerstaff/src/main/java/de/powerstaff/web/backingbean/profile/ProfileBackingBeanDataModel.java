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

import de.mogwai.common.web.backingbean.BackingBeanDataModel;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.dto.ProfileSearchRequest;
import de.powerstaff.web.utils.PagedListDataModel;
import org.richfaces.component.UIDatascroller;

import javax.faces.component.UIComponent;

public class ProfileBackingBeanDataModel extends BackingBeanDataModel {

    private static final long serialVersionUID = 8778198734206433965L;

    private ProfileSearchRequest searchRequest = new ProfileSearchRequest();

    private transient UIComponent viewRoot;

    private transient PagedListDataModel<ProfileSearchEntry> searchResult;

    private transient UIDatascroller dataScroller;

    private boolean initialized;

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
     * @param viewRoot the viewRoot to set
     */
    public void setViewRoot(UIComponent viewRoot) {
        this.viewRoot = viewRoot;
    }

    public PagedListDataModel<ProfileSearchEntry> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(
            PagedListDataModel<ProfileSearchEntry> searchResult) {
        this.searchResult = searchResult;
    }

    public int getSearchResultSize() {
        if (searchResult == null) {
            return 0;
        }
        return searchResult.getRowCount();
    }

    public UIDatascroller getDataScroller() {
        return dataScroller;
    }

    public void setDataScroller(UIDatascroller dataScroller) {
        this.dataScroller = dataScroller;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}