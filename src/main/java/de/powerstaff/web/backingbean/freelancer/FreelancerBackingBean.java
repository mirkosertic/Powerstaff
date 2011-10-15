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
package de.powerstaff.web.backingbean.freelancer;

import java.util.List;
import java.util.Vector;

import de.mogwai.common.command.EditEntityCommand;
import de.mogwai.common.command.UpdateModelCommand;
import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.mogwai.common.web.utils.UpdateModelInfo;
import de.powerstaff.business.dto.ProfileSearchInfoDetail;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerContact;
import de.powerstaff.business.entity.FreelancerHistory;
import de.powerstaff.business.entity.FreelancerProfile;
import de.powerstaff.business.entity.Partner;
import de.powerstaff.business.service.FreelancerService;
import de.powerstaff.business.service.ProfileSearchService;
import de.powerstaff.web.backingbean.PersonEditorBackingBean;
import de.powerstaff.web.backingbean.partner.PartnerBackingBean;

public class FreelancerBackingBean
		extends
		PersonEditorBackingBean<Freelancer, FreelancerBackingBeanDataModel, FreelancerService> {

	private static final long serialVersionUID = 1951906917491517234L;

	private static final Logger LOGGER = new Logger(FreelancerBackingBean.class);

	private transient ProfileSearchService profileSearchService;

    private PartnerBackingBean partnerBackingBean;

    public PartnerBackingBean getPartnerBackingBean() {
        return partnerBackingBean;
    }

    public void setPartnerBackingBean(PartnerBackingBean partnerBackingBean) {
        this.partnerBackingBean = partnerBackingBean;
    }

    @Override
	protected FreelancerBackingBeanDataModel createDataModel() {
		return new FreelancerBackingBeanDataModel();
	}

	/**
	 * @return the profileSearchService
	 */
	public ProfileSearchService getProfileSearchService() {
		return profileSearchService;
	}

	/**
	 * @param profileSearchService
	 *            the profileSearchService to set
	 */
	public void setProfileSearchService(
			ProfileSearchService profileSearchService) {
		this.profileSearchService = profileSearchService;
	}

	public List<String> getCodeSuggestion(Object aSuggest) {
		return entityService.getCodeSuggestions((String) aSuggest);
	}

	@Override
	protected void afterNavigation() {
		super.afterNavigation();

        getData().getProfiles().setWrappedData(profileSearchService.loadProfilesFor(getData().getEntity()));
	}

	@Override
	protected Freelancer createNew() {
		return new Freelancer();
	}

	public String commandShowPartner() {
		Partner thePartner = getData().getEntity().getPartner();
		if (thePartner != null) {
			partnerBackingBean.updateModel(new EditEntityCommand<Freelancer>(getData().getEntity()));
			return "PARTNER_STAMMDATEN";
		}
		return null;
	}

	@Override
	public void updateModel(UpdateModelCommand aInfo) {
		super.updateModel(aInfo);
		if (aInfo instanceof EditEntityCommand) {
			EditEntityCommand theCommand = (EditEntityCommand) aInfo;

			init();
			if (theCommand.getValue() instanceof ProfileSearchInfoDetail) {

				ProfileSearchInfoDetail theDetails = (ProfileSearchInfoDetail) theCommand
						.getValue();
				Freelancer theFreelancer = (Freelancer) entityService
						.findByPrimaryKey(theDetails.getId());
                if (theFreelancer != null) {
				    getData().setEntity(theFreelancer);
                } else {
                    // Freelancer wurde gelöscht, wir springen zu einem neuen Eintrag
                    JSFMessageUtils.addGlobalErrorMessage(MSG_KEINEDATENGEFUNDEN);
                    commandNew();
                }
			}

			if (theCommand.getValue() instanceof Freelancer) {
				Freelancer theDetails = (Freelancer) theCommand.getValue();
				Freelancer theFreelancer = entityService
						.findByPrimaryKey(theDetails.getId());
				getData().setEntity(theFreelancer);
			}

			afterNavigation();
		}
	}

	public String getProfileOpenCommand() {
		FreelancerProfile theProfile = (FreelancerProfile) getData()
				.getProfiles().getRowData();
		if (theProfile.isWordProfile()) {
			return "return openWordFile('"
					+ theProfile.getFileName().replace("\\", "\\\\") + "')";
		}
		return "return openTextFile('"
				+ theProfile.getFileName().replace("\\", "\\\\") + "')";
	}

	@Override
	protected FreelancerContact createNewContact() {
		return new FreelancerContact();
	}

	@Override
	protected FreelancerHistory createNewHistory() {
		return new FreelancerHistory();
	}
}
