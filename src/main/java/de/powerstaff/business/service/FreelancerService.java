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

import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.ProjectPosition;

import java.util.List;
import java.util.Set;

public interface FreelancerService extends PersonService<Freelancer> {

    List<String> getCodeSuggestions(String aSuggest);

    Freelancer findRealFreelancerByCode(String aCode);

    List<ProjectPosition> findPositionsFor(Freelancer aFreelancer);

    List<Freelancer> findFreelancerByTagIDs(Set<Long> aTagIDs);

    List<Freelancer> findFreelancerByTagIDsSortByName1(Set<Long> aTagIDs);

    List<Freelancer> findFreelancerByTagIDsSortByName2(Set<Long> aTagIDs);

    List<Freelancer> findFreelancerByTagIDsSortByCode(Set<Long> aTagIDs);

    List<Freelancer> findFreelancerByTagIDsSortByAvailability(Set<Long> aTagIDs);

    List<Freelancer> findFreelancerByTagIDsSortBySallary(Set<Long> aTagIDs);

    List<Freelancer> findFreelancerByTagIDsSortByPlz(Set<Long> aTagIDs);

    List<Freelancer> findFreelancerByTagIDsSortByLastContact(Set<Long> aTagIDs);
}