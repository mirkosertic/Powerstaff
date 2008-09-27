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

import de.mogwai.common.command.EditEntityCommand;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.mogwai.common.web.utils.UpdateModelInfo;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.Partner;
import de.powerstaff.business.entity.PartnerContact;
import de.powerstaff.business.entity.PartnerHistory;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.PartnerService;
import de.powerstaff.web.backingbean.PersonEditorBackingBean;
import de.powerstaff.web.backingbean.freelancer.FreelancerBackingBean;

public class PartnerBackingBean extends PersonEditorBackingBean<Partner, PartnerBackingBeanDataModel, PartnerService> {

    private FreelancerService freelancerService;

    @Override
    protected PartnerBackingBeanDataModel createDataModel() {
        return new PartnerBackingBeanDataModel();
    }

    /**
     * @return the freelancerService
     */
    public FreelancerService getFreelancerService() {
        return freelancerService;
    }

    /**
     * @param freelancerService
     *                the freelancerService to set
     */
    public void setFreelancerService(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    public String commandFreiberufler() {
        return "FREIBERUFLER";
    }

    public String commandSelectFreelancer() {

        Freelancer theFreelancer = (Freelancer) getData().getFreelancer().getRowData();
        forceUpdateOfBean(FreelancerBackingBean.class, new EditEntityCommand<Freelancer>(theFreelancer));
        return "FREELANCER_STAMMDATEN";
    }

    @Override
    public void updateModel(UpdateModelInfo aInfo) {
        super.updateModel(aInfo);
        if (aInfo.getCommand() instanceof EditEntityCommand) {
            EditEntityCommand<Freelancer> theCommand = (EditEntityCommand<Freelancer>) aInfo.getCommand();

            Partner theOldPartner = theCommand.getValue().getPartner();
            init();

            Partner thePartner = (Partner) entityService.findByPrimaryKey(theOldPartner.getId());
            getData().setEntity(thePartner);
            afterNavigation();

            getData().setOriginalFreelancer(theCommand.getValue());
        }
    }

    @Override
    protected Partner createNew() {
        return new Partner();
    }

    @Override
    public void commandFirst() {
        super.commandFirst();
        getData().setOriginalFreelancer(null);
    }

    @Override
    public void commandLast() {
        super.commandLast();
        getData().setOriginalFreelancer(null);
    }

    @Override
    public void commandNew() {
        super.commandNew();
        getData().setOriginalFreelancer(null);
    }

    @Override
    public void commandNext() {
        super.commandNext();
        getData().setOriginalFreelancer(null);
    }

    @Override
    public void commandPrior() {
        super.commandPrior();
        getData().setOriginalFreelancer(null);
    }

    public String commandJumpToFreelancer() {
        forceUpdateOfBean(FreelancerBackingBean.class, new EditEntityCommand<Freelancer>(getData()
                .getOriginalFreelancer()));
        return "FREELANCER_STAMMDATEN";
    }

    public void commandAddFreelancer() {

        Freelancer theFreelancer = freelancerService.findRealFreelancerByCode(getData().getCodeToAdd());
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

        Freelancer theFreelancer = (Freelancer) getData().getFreelancer().getRowData();

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
}