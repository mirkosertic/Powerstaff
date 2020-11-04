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

import de.mogwai.common.web.backingbean.BackingBeanDataModel;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.entity.SavedProfileSearch;
import de.powerstaff.web.backingbean.TagSelectionState;
import de.powerstaff.web.utils.PagedListDataModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileBackingBeanDataModel extends BackingBeanDataModel {

    public static final String TYPE_USER = "user";

    public static final String TYPE_SEARCH = "search";

    public final static String NEW_ENTITY_ID = "new";

    private static final long serialVersionUID = 8778198734206433965L;

    private SavedProfileSearch searchRequest = new SavedProfileSearch();

    private transient PagedListDataModel<ProfileSearchEntry> searchResult;

    private String type;

    private String id;

    private final List<TagSelectionState> tagSelection = new ArrayList<>();

    public SavedProfileSearch getSearchRequest() {
        return searchRequest;
    }

    public void setSearchRequest(final SavedProfileSearch searchRequest) {
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
            final PagedListDataModel<ProfileSearchEntry> searchResult) {
        this.searchResult = searchResult;
    }

    public int getSearchResultSize() {
        if (searchResult == null) {
            return 0;
        }
        return searchResult.getRowCount();
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public List<TagSelectionState> getTagSelection() {
        return tagSelection;
    }
}