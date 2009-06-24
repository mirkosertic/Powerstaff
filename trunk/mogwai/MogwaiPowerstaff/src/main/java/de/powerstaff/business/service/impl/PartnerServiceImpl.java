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

import de.mogwai.common.business.service.impl.LogableService;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.dao.PartnerDAO;
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.Partner;
import de.powerstaff.business.service.PartnerService;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.RecordInfo;
import de.powerstaff.business.service.TooManySearchResults;

public class PartnerServiceImpl extends LogableService implements PartnerService {

    private PartnerDAO partnerDAO;
    
    private PowerstaffSystemParameterService systemParameterService;

    /**
     * @return the partnerDAO
     */
    public PartnerDAO getPartnerDAO() {
        return partnerDAO;
    }

    /**
     * @param partnerDAO
     *                the partnerDAO to set
     */
    public void setPartnerDAO(PartnerDAO partnerDAO) {
        this.partnerDAO = partnerDAO;
    }

    /**
     * @return the systemParameterService
     */
    public PowerstaffSystemParameterService getSystemParameterService() {
        return systemParameterService;
    }

    /**
     * @param systemParameterService the systemParameterService to set
     */
    public void setSystemParameterService(PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    public void delete(Freelancer aEntity) {
        partnerDAO.delete(aEntity);
    }

    public Partner findByPrimaryKey(Long aId) {
        return partnerDAO.findById(aId);
    }

    public Partner findFirst() {
        return partnerDAO.findFirst();
    }

    public Partner findLast() {
        return partnerDAO.findLast();
    }

    public Partner findNext(Partner aObject) {
        return partnerDAO.findNext(aObject);
    }

    public Partner findPrior(Partner aObject) {
        return partnerDAO.findPrior(aObject);
    }

    public RecordInfo getRecordInfo(Partner aObject) {
        return partnerDAO.getRecordInfo(aObject);
    }

    public Collection<GenericSearchResult> performQBESearch(Partner aObject) throws TooManySearchResults {
        int aMax = systemParameterService.getMaxSearchResult();
        Collection<GenericSearchResult> theResult = partnerDAO.performQBESearch(aObject, aMax);
        if (theResult.size() == aMax) {
            throw new TooManySearchResults(theResult);
        }
        return theResult;
    }

    public void save(Partner aObject) {
        partnerDAO.save(aObject);
    }

    public void delete(Partner aObject) {
        partnerDAO.delete(aObject);
    }

    public Partner findByRecordNumber(Long aNumber) {
        return partnerDAO.findByRecordNumber(aNumber);
    }

    public Collection<GenericSearchResult> performSearchByContact(String aContact, ContactType aContactType)
            throws TooManySearchResults {
        int aMax = systemParameterService.getMaxSearchResult();
        Collection<GenericSearchResult> theResult = partnerDAO.performSearchByContact(aContact, aContactType, aMax);
        if (theResult.size() == aMax) {
            throw new TooManySearchResults(theResult);
        }
        return theResult;
    }
}