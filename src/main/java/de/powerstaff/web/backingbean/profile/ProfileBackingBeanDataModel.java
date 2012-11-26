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
import de.powerstaff.business.entity.SavedProfileSearch;
import de.powerstaff.web.utils.PagedListDataModel;
import org.richfaces.component.UIDatascroller;

public class ProfileBackingBeanDataModel extends BackingBeanDataModel {

    public static final String TYPE_USER = "user";

    public static final String TYPE_SEARCH = "search";

    public final static String NEW_ENTITY_ID = "new";

    private static final long serialVersionUID = 8778198734206433965L;

    private SavedProfileSearch searchRequest = new SavedProfileSearch();

    private transient PagedListDataModel<ProfileSearchEntry> searchResult;

    private transient UIDatascroller dataScroller;

    private String type;

    private String id;

    public SavedProfileSearch getSearchRequest() {
        return searchRequest;
    }

    public void setSearchRequest(SavedProfileSearch searchRequest) {
        this.searchRequest = searchRequest;
        if (searchRequest.getProject() != null) {
            type = TYPE_SEARCH;
            if (searchRequest.getId() != null) {
                id = searchRequest.getId().toString();
            } else {
                id = NEW_ENTITY_ID;
            }
        } else {
            type = TYPE_USER;
            id = searchRequest.getUser().getName();
        }
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}