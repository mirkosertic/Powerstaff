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
import de.powerstaff.business.dao.CustomerDAO;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.entity.Customer;
import de.powerstaff.business.service.CustomerService;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.RecordInfo;
import de.powerstaff.business.service.TooManySearchResults;

public class CustomerServiceImpl extends LogableService implements CustomerService {

    private CustomerDAO customerDAO;
    
    private PowerstaffSystemParameterService systemParameterService;
    
    /**
     * @return the customerDAO
     */
    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }

    /**
     * @param customerDAO
     *                the customerDAO to set
     */
    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
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

    public void delete(Customer aEntity) {
        customerDAO.delete(aEntity);
    }

    public Customer findByPrimaryKey(Long aId) {
        return customerDAO.findById(aId);
    }

    public Customer findFirst() {
        return customerDAO.findFirst();
    }

    public Customer findLast() {
        return customerDAO.findLast();
    }

    public Customer findNext(Customer aObject) {
        return customerDAO.findNext(aObject);
    }

    public Customer findPrior(Customer aObject) {
        return customerDAO.findPrior(aObject);
    }

    public RecordInfo getRecordInfo(Customer aObject) {
        return customerDAO.getRecordInfo(aObject);
    }

    public Collection<GenericSearchResult> performQBESearch(Customer aObject) throws TooManySearchResults {
        int aMax = systemParameterService.getMaxSearchResult();
        Collection<GenericSearchResult> theResult = customerDAO.performQBESearch(aObject, aMax);
        if (theResult.size() == aMax) {
            throw new TooManySearchResults(theResult);
        }
        return theResult;
    }

    public void save(Customer aObject) {
        customerDAO.save(aObject);
    }

    public Customer findByRecordNumber(Long aNumber) {
        return customerDAO.findByRecordNumber(aNumber);
    }
}
