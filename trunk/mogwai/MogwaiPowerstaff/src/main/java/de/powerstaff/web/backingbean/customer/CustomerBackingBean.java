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
package de.powerstaff.web.backingbean.customer;

import de.powerstaff.business.entity.Customer;
import de.powerstaff.business.entity.CustomerContact;
import de.powerstaff.business.entity.HistoryEntity;
import de.powerstaff.business.entity.Project;
import de.powerstaff.business.service.CustomerService;
import de.powerstaff.business.service.ProjectService;
import de.powerstaff.web.backingbean.PersonEditorBackingBean;

import java.util.List;

public class CustomerBackingBean extends PersonEditorBackingBean<Customer, CustomerBackingBeanDataModel, CustomerService> {

    private static final long serialVersionUID = -1019229554217528125L;

    private ProjectService projectService;

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    protected CustomerBackingBeanDataModel createDataModel() {
        return new CustomerBackingBeanDataModel();
    }

    @Override
    protected String getNavigationIDPrefix() {
        return "customer";
    }

    @Override
    protected Customer createNew() {
        return new Customer();
    }

    @Override
    protected CustomerContact createNewContact() {
        return new CustomerContact();
    }

    @Override
    protected HistoryEntity createNewHistory() {
        return new HistoryEntity();
    }

    public List<Project> getCurrentProjects() {
        return projectService.findProjectsFor(getData().getEntity());
    }
}