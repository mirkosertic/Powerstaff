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

import de.mogwai.common.business.dto.DataTransferObject;


public class ProfileSearchEntry extends DataTransferObject {

    private String code;

    private ProfileSearchInfoDetail freelancer;

    private String highlightResult;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ProfileSearchInfoDetail getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(ProfileSearchInfoDetail freelancer) {
        this.freelancer = freelancer;
    }

    public String getHighlightResult() {
        return highlightResult;
    }

    public void setHighlightResult(String highlightResult) {
        this.highlightResult = highlightResult;
    }

}
