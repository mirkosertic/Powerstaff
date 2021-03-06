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

import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.dao.ProjectDAO;
import de.powerstaff.business.entity.*;
import de.powerstaff.business.service.*;

import java.util.Collection;
import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    private ProjectDAO projectDAO;

    private PowerstaffSystemParameterService systemParameterService;

    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public void setSystemParameterService(PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    public void delete(Project aEntity) throws OptimisticLockException {
        try {
            projectDAO.delete(aEntity);
        } catch (ReferenceExistsException e) {
            // Kann hier nicht passieren
        }
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

    public Collection<GenericSearchResult> performQBESearch(Project aObject) throws TooManySearchResults {
        int aMax = systemParameterService.getMaxSearchResult();
        Collection<GenericSearchResult> theResult = projectDAO.performQBESearch(aObject, aMax);
        if (theResult.size() == aMax) {
            throw new TooManySearchResults(theResult);
        }
        return theResult;
    }

    public void save(Project aObject) throws OptimisticLockException {
        projectDAO.save(aObject);
    }

    public Project findByRecordNumber(Long aNumber) {
        return projectDAO.findByRecordNumber(aNumber);
    }

    @Override
    public Collection<? extends ProjectPositionStatus> getAvailablePositionStatus() {
        return projectDAO.getAvailablePositionStatus();
    }

    @Override
    public Collection<SavedProfileSearch> getSavedSearchesFor(Project aProject) {
        return projectDAO.getSavedSearchesFor(aProject);
    }

    @Override
    public void deleteSavedSearch(SavedProfileSearch aSearch) throws OptimisticLockException {
        try {
            projectDAO.delete(aSearch);
        } catch (ReferenceExistsException e) {
            // Kann hier nicht passieren
        }
    }

    @Override
    public List<Project> findProjectsFor(Customer aCustomer) {
        return projectDAO.findProjectsFor(aCustomer);
    }

    @Override
    public List<Project> findProjectsFor(Partner aPartner) {
        return projectDAO.findProjectsFor(aPartner);
    }
}