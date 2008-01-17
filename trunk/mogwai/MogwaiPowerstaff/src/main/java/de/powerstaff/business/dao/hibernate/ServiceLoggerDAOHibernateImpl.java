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
package de.powerstaff.business.dao.hibernate;

import de.mogwai.common.dao.hibernate.GenericDaoHibernateImpl;
import de.powerstaff.business.dao.ServiceLoggerDAO;
import de.powerstaff.business.entity.JobInfo;

public class ServiceLoggerDAOHibernateImpl extends GenericDaoHibernateImpl implements ServiceLoggerDAO {

    public JobInfo findByServiceID(String aServiceID) {
        return (JobInfo) getHibernateTemplate().get(JobInfo.class, aServiceID);
    }

    public void save(JobInfo aInfo) {
        getHibernateTemplate().save(aInfo);
    }

    public void update(JobInfo aInfo) {
        getHibernateTemplate().update(aInfo);
    }

}
