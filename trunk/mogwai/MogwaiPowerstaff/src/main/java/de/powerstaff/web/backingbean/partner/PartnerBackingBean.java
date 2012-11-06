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
import de.mogwai.common.command.UpdateModelCommand;
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
import de.powerstaff.web.backingbean.project.ProjectBackingBean;

public class PartnerBackingBean
		extends
		PersonEditorBackingBean<Partner, PartnerBackingBeanDataModel, PartnerService> {

	private static final long serialVersionUID = -5789495052412740906L;

	private transient FreelancerService freelancerService;

    private FreelancerBackingBean freelancerBackingBean;

    private ProjectBackingBean projectBackingBean;

    public ProjectBackingBean getProjectBackingBean() {
        return projectBackingBean;
    }

    public void setProjectBackingBean(ProjectBackingBean projectBackingBean) {
        this.projectBackingBean = projectBackingBean;
    }

    public void setFreelancerBackingBean(FreelancerBackingBean freelancerBackingBean) {
        this.freelancerBackingBean = freelancerBackingBean;
    }

    @Override
	protected PartnerBackingBeanDataModel createDataModel() {
		return new PartnerBackingBeanDataModel();
	}

	/**
	 * @param freelancerService
	 *            the freelancerService to set
	 */
	public void setFreelancerService(FreelancerService freelancerService) {
		this.freelancerService = freelancerService;
	}

	public String commandFreiberufler() {
		return "FREIBERUFLER";
	}

	public String commandSelectFreelancer() {

		Freelancer theFreelancer = (Freelancer) getData().getFreelancer()
				.getRowData();
        freelancerBackingBean.updateModel(new EditEntityCommand<Freelancer>(theFreelancer));
		return "FREELANCER_STAMMDATEN";
	}

	@Override
	public void updateModel(UpdateModelCommand aInfo) {
		super.updateModel(aInfo);
		if (aInfo instanceof EditEntityCommand) {

			afterPropertiesSet();

			EditEntityCommand theCommand = (EditEntityCommand) aInfo;
			if (theCommand.getValue() instanceof Freelancer) {

				Freelancer theFreelancer = (Freelancer) theCommand.getValue();
				Partner theOldPartner = theFreelancer.getPartner();

				Partner thePartner = (Partner) entityService
						.findByPrimaryKey(theOldPartner.getId());
				getData().setEntity(thePartner);
				afterNavigation();

				getData().setOriginalFreelancer(theFreelancer);
			} else {
				Partner thePartner = (Partner) theCommand.getValue();

				thePartner = (Partner) entityService
						.findByPrimaryKey(thePartner.getId());
				getData().setEntity(thePartner);
				afterNavigation();
			}
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
        freelancerBackingBean.updateModel(new EditEntityCommand<Freelancer>(getData()
                .getOriginalFreelancer()));
		return "FREELANCER_STAMMDATEN";
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

	public String commandNewProject() {

		Partner thePartner = getData().getEntity();
		if (thePartner.getId() == null) {
			JSFMessageUtils.addGlobalErrorMessage(MSG_KEINPARTNER);
			return null;
		}

        projectBackingBean.updateModel(new EditEntityCommand<Partner>(thePartner));
		return "PROJEKT_STAMMDATEN";
	}
}