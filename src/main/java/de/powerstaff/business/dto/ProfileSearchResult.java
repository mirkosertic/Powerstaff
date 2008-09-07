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
package de.powerstaff.business.dto;

import java.util.ArrayList;
import java.util.List;

import de.mogwai.common.business.dto.DataTransferObject;

public class ProfileSearchResult extends DataTransferObject {

    private List<ProfileSearchEntry> enties = new ArrayList<ProfileSearchEntry>();
    
    private int totalFound;
    
    private ProfileSearchRequest searchRequest;

    /**
     * @return the enties
     */
    public List<ProfileSearchEntry> getEnties() {
        return enties;
    }

    /**
     * @param enties the enties to set
     */
    public void setEnties(List<ProfileSearchEntry> enties) {
        this.enties = enties;
    }

    /**
     * @return the totalFound
     */
    public int getTotalFound() {
        return totalFound;
    }

    /**
     * @param totalFound the totalFound to set
     */
    public void setTotalFound(int totalFound) {
        this.totalFound = totalFound;
    }

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
}