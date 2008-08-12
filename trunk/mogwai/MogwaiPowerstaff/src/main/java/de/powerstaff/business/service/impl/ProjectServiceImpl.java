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
import de.powerstaff.business.dao.ProjectDAO;
import de.powerstaff.business.entity.Project;
import de.powerstaff.business.service.ProjectService;
import de.powerstaff.business.service.RecordInfo;

public class ProjectServiceImpl extends LogableService implements ProjectService {

    private ProjectDAO projectDAO;

    /**
     * @return the projectDAO
     */
    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    /**
     * @param projectDAO
     *                the projectDAO to set
     */
    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public void delete(Project aEntity) {
        projectDAO.delete(aEntity);
    }

    public Project findByPrimaryKey(Long aId) {
        return projectDAO.findById(aId);
    }

    public Project findFirst() {
        return projectDAO.findFirst();
    }

    public Project findLast() {
        return projectDAO.findLast();
    }

    public Project findNext(Project aObject) {
        return projectDAO.findNext(aObject);
    }

    public Project findPrior(Project aObject) {
        return projectDAO.findPrior(aObject);
    }

    public RecordInfo getRecordInfo(Project aObject) {
        return projectDAO.getRecordInfo(aObject);
    }

    public Collection<Project> performQBESearch(Project aObject) {
        return projectDAO.performQBESearch(aObject);
    }

    public void save(Project aObject) {
        projectDAO.save(aObject);
    }

    public Project findByRecordNumber(Long aNumber) {
        return projectDAO.findByRecordNumber(aNumber);
    }
}
