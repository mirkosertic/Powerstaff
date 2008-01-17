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
import de.powerstaff.business.dao.PartnerDAO;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.Partner;
import de.powerstaff.business.service.PartnerService;
import de.powerstaff.business.service.RecordInfo;

public class PartnerServiceImpl extends LogableService implements PartnerService {

    private PartnerDAO partnerDAO;

    /**
     * @return the partnerDAO
     */
    public PartnerDAO getPartnerDAO() {
        return partnerDAO;
    }

    /**
     * @param partnerDAO
     *            the partnerDAO to set
     */
    public void setPartnerDAO(PartnerDAO partnerDAO) {
        this.partnerDAO = partnerDAO;
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

    public Collection<Partner> performQBESearch(Partner aObject) {
        return partnerDAO.performQBESearch(aObject);
    }

    public void save(Partner aObject) {
        partnerDAO.save(aObject);
    }

    public List<Partner> findAll() {
        return partnerDAO.findAll();
    }

    public void delete(Partner aObject) {
        partnerDAO.delete(aObject);
    }
}
