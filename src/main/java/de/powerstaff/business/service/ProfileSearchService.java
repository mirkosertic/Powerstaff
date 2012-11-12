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
package de.powerstaff.business.service;

import de.mogwai.common.business.service.Service;
import de.powerstaff.business.dto.DataPage;
import de.powerstaff.business.dto.ProfileSearchEntry;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerProfile;
import de.powerstaff.business.entity.Project;
import de.powerstaff.business.entity.SavedProfileSearch;

import java.util.List;

public interface ProfileSearchService extends Service {

    void saveSearchRequest(SavedProfileSearch searchRequest, boolean cleanup);

    DataPage<ProfileSearchEntry> findProfileDataPage(
            SavedProfileSearch aRequest, int startRow, int pageSize)
            throws Exception;

    void removeSavedSearchEntry(SavedProfileSearch searchRequest, String aDocumentId);

    int getPageSize();

    List<FreelancerProfile> loadProfilesFor(Freelancer aFreelancer);

    SavedProfileSearch getSearchRequestForUser(String aUsername);

    SavedProfileSearch getSearchRequest(long aSearchRequestId);

    List<ProfileSearchEntry> getSimilarFreelancer(Freelancer aFreelancer);

    List<ProfileSearchEntry> getSimilarFreelancer(Project aProject);
}