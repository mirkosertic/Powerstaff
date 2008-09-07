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
package de.powerstaff.business.entity;

import de.mogwai.common.business.entity.Entity;

/**
 * @author msertic
 */
public class SavedProfileSearchEntry extends Entity {

    private Long freelancerId;
    
    private String uniqueDocumentId;

    /**
     * @return the freelancerId
     */
    public Long getFreelancerId() {
        return freelancerId;
    }

    /**
     * @param freelancerId the freelancerId to set
     */
    public void setFreelancerId(Long freelancerId) {
        this.freelancerId = freelancerId;
    }

    /**
     * @return the uniqueDocumentId
     */
    public String getUniqueDocumentId() {
        return uniqueDocumentId;
    }

    /**
     * @param uniqueDocumentId the uniqueDocumentId to set
     */
    public void setUniqueDocumentId(String uniqueDocumentId) {
        this.uniqueDocumentId = uniqueDocumentId;
    }
}