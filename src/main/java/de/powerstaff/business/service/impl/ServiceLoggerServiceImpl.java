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

import java.sql.Timestamp;

import de.mogwai.common.business.service.impl.LogableService;
import de.powerstaff.business.dao.ServiceLoggerDAO;
import de.powerstaff.business.entity.JobInfo;
import de.powerstaff.business.service.ServiceLoggerService;

public class ServiceLoggerServiceImpl extends LogableService implements ServiceLoggerService {

    private ServiceLoggerDAO serviceLoggerDAO;

    /**
     * @return the serviceLoggerDAO
     */
    public ServiceLoggerDAO getServiceLoggerDAO() {
        return serviceLoggerDAO;
    }

    /**
     * @param serviceLoggerDAO
     *                the serviceLoggerDAO to set
     */
    public void setServiceLoggerDAO(ServiceLoggerDAO serviceLoggerDAO) {
        this.serviceLoggerDAO = serviceLoggerDAO;
    }

    protected JobInfo getJobInfo(String aServiceID) {
        JobInfo theInfo = serviceLoggerDAO.findByServiceID(aServiceID);
        if (theInfo == null) {
            theInfo = new JobInfo();
            theInfo.setId(aServiceID);

            serviceLoggerDAO.save(theInfo);
        }

        return theInfo;
    }

    public void logEnd(String aServiceID, String aDescription) {

        JobInfo theInfo = getJobInfo(aServiceID);
        theInfo.setEndDate(new Timestamp(System.currentTimeMillis()));
        theInfo.setDescription(aDescription);

        serviceLoggerDAO.update(theInfo);

    }

    public void logStart(String aServiceID, String aDescription) {

        JobInfo theInfo = getJobInfo(aServiceID);
        theInfo.setStartDate(new Timestamp(System.currentTimeMillis()));
        theInfo.setEndDate(null);
        theInfo.setDescription(aDescription);

        serviceLoggerDAO.update(theInfo);
    }

}
