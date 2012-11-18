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
package de.powerstaff.web.backingbean.partner;

import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.entity.*;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.PartnerService;
import de.powerstaff.business.service.ProjectService;
import de.powerstaff.web.backingbean.PersonEditorBackingBean;

import java.util.List;

public class PartnerBackingBean
        extends
        PersonEditorBackingBean<Partner, PartnerBackingBeanDataModel, PartnerService> {

    private static final long serialVersionUID = -5789495052412740906L;

    private transient FreelancerService freelancerService;

    private ProjectService projectService;

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    protected PartnerBackingBeanDataModel createDataModel() {
        return new PartnerBackingBeanDataModel();
    }

    public void setFreelancerService(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    @Override
    protected String getNavigationIDPrefix() {
        return "partner";
    }

    @Override
    protected Partner createNew() {
        return new Partner();
    }

    @Override
    public String commandFirst() {
        getData().setOriginalFreelancer(null);
        return super.commandFirst();
    }

    @Override
    public String commandLast() {
        getData().setOriginalFreelancer(null);
        return super.commandLast();
    }

    @Override
    public String commandNew() {
        getData().setOriginalFreelancer(null);
        return super.commandNew();
    }

    @Override
    public String commandNext() {
        getData().setOriginalFreelancer(null);
        return super.commandNext();
    }

    @Override
    public String commandPrior() {
        getData().setOriginalFreelancer(null);
        return super.commandPrior();
    }

    public void commandAddFreelancer() {

        Freelancer theFreelancer = freelancerService
                .findRealFreelancerByCode(getData().getCodeToAdd());
        if (theFreelancer != null) {

            Partner thePartner = getData().getEntity();

            theFreelancer.setPartner(thePartner);
            freelancerService.save(theFreelancer);

            thePartner = entityService.findByPrimaryKey(thePartner.getId());
            getData().setEntity(thePartner);

            JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGESPEICHERT);
        } else {
            JSFMessageUtils.addGlobalErrorMessage(MSG_KEINEDATENGEFUNDEN);
        }
    }

    public void commandRemoveFreelancer() {

        Freelancer theFreelancer = (Freelancer) getData().getFreelancer()
                .getRowData();

        Partner thePartner = getData().getEntity();

        theFreelancer.setPartner(null);
        freelancerService.save(theFreelancer);

        thePartner = entityService.findByPrimaryKey(thePartner.getId());
        getData().setEntity(thePartner);

        JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGESPEICHERT);
    }

    @Override
    protected PartnerContact createNewContact() {
        return new PartnerContact();
    }

    @Override
    protected PartnerHistory createNewHistory() {
        return new PartnerHistory();
    }

   public List<Project> getCurrentProjects() {
        return projectService.findProjectsFor(getData().getEntity());
    }
}