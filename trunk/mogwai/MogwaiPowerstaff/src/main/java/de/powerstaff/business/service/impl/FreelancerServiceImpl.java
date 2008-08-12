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
package de.powerstaff.business.service.impl;

import java.util.Collection;
import java.util.List;

import de.mogwai.common.business.service.impl.LogableService;
import de.powerstaff.business.dao.FreelancerDAO;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.ProfileSearchInfoDetail;
import de.powerstaff.business.service.RecordInfo;

public class FreelancerServiceImpl extends LogableService implements FreelancerService {

    private FreelancerDAO freelancerDAO;

    /**
     * @return the freelancerDAO
     */
    public FreelancerDAO getFreelancerDAO() {
        return freelancerDAO;
    }

    /**
     * @param freelancerDAO
     *                the freelancerDAO to set
     */
    public void setFreelancerDAO(FreelancerDAO freelancerDAO) {
        this.freelancerDAO = freelancerDAO;
    }

    public ProfileSearchInfoDetail findFreelancerByCode(String code) {
        return freelancerDAO.findByCode(code);
    }

    public void delete(Freelancer aEntity) {
        freelancerDAO.delete(aEntity);
    }

    public Freelancer findByPrimaryKey(Long aId) {
        return freelancerDAO.findById(aId);
    }

    public Freelancer findFirst() {
        return freelancerDAO.findFirst();
    }

    public Freelancer findLast() {
        return freelancerDAO.findLast();
    }

    public Freelancer findNext(Freelancer aObject) {
        return freelancerDAO.findNext(aObject);
    }

    public Freelancer findPrior(Freelancer aObject) {
        return freelancerDAO.findPrior(aObject);
    }

    public RecordInfo getRecordInfo(Freelancer aObject) {
        return freelancerDAO.getRecordInfo(aObject);
    }

    public Collection<Freelancer> performQBESearch(Freelancer aObject) {
        return freelancerDAO.performQBESearch(aObject);
    }

    public void save(Freelancer aObject) {
        freelancerDAO.save(aObject);
    }

    public List<String> getCodeSuggestions(String aSuggest) {
        return freelancerDAO.getCodeSuggestions(aSuggest);
    }

    public Freelancer findByRecordNumber(Long aNumber) {
        return freelancerDAO.findByRecordNumber(aNumber);
    }
}
