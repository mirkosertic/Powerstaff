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
package de.powerstaff.web.backingbean.login;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import de.powerstaff.web.backingbean.NavigatingBackingBean;
import de.powerstaff.web.backingbean.freelancer.FreelancerBackingBean;
import org.springframework.security.ui.WebAuthenticationDetails;

import de.mogwai.common.business.service.LoginException;
import de.mogwai.common.business.service.LoginService;
import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.backingbean.WrappingBackingBean;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.web.backingbean.MessageConstants;

public class LoginBackingBean extends
		WrappingBackingBean<LoginBackingBeanDataModel> implements
		MessageConstants {

	private static final long serialVersionUID = -8961543903559963065L;

	private transient LoginService loginService;

	private static final Logger LOGGER = new Logger(LoginBackingBean.class);

    private FreelancerBackingBean freelancerBackingBean;

	@Override
	protected LoginBackingBeanDataModel createDataModel() {
		return new LoginBackingBeanDataModel();
	}

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

    public void setFreelancerBackingBean(FreelancerBackingBean freelancerBackingBean) {
        this.freelancerBackingBean = freelancerBackingBean;
    }

    public String commandLogin() {
		try {
			HttpServletRequest theRequest = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			loginService.login(getData().getUsername(),
					getData().getPassword(), new WebAuthenticationDetails(
							theRequest));

            freelancerBackingBean.getData().setCurrentFreelancerId(NavigatingBackingBean.NEW_RECORD_ID);

            // Redirect auf die leere Freelancer Seite
            return "pretty:freelancer";
		} catch (LoginException e) {
			LOGGER.logError("Fehler beim Login", e);
			JSFMessageUtils.addGlobalErrorMessage(MSG_FALSCHESLOGIN);
			return null;
		}
	}
}