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

import de.mogwai.common.business.service.LoginException;
import de.mogwai.common.business.service.LoginService;
import de.mogwai.common.web.backingbean.WrappingBackingBean;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.web.backingbean.EntityEditorBackingBeanDataModel;
import de.powerstaff.web.backingbean.MessageConstants;
import de.powerstaff.web.backingbean.XingConnectorBackingBean;
import de.powerstaff.web.backingbean.freelancer.FreelancerBackingBean;
import org.slf4j.LoggerFactory;
import org.springframework.security.ui.WebAuthenticationDetails;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class LoginBackingBean extends
        WrappingBackingBean<LoginBackingBeanDataModel> implements
        MessageConstants {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LoginBackingBean.class);

    private static final long serialVersionUID = -8961543903559963065L;

    private transient LoginService loginService;

    private FreelancerBackingBean freelancerBackingBean;

    private XingConnectorBackingBean xingConnectorBackingBean;

    @Override
    protected LoginBackingBeanDataModel createDataModel() {
        return new LoginBackingBeanDataModel();
    }

    public void setXingConnectorBackingBean(XingConnectorBackingBean xingConnectorBackingBean) {
        this.xingConnectorBackingBean = xingConnectorBackingBean;
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

            freelancerBackingBean.getData().setCurrentEntityId(EntityEditorBackingBeanDataModel.NEW_ENTITY_ID);

            // Wenn der Xing Connector genutzt werden soll, muss zuerst eine Anmeldung erfolgen
            if (getData().isLoginWithXing()) {
                xingConnectorBackingBean.performXingAuthentication();
                return null;
            }

            // Redirect auf die leere Freelancer Seite
            return "pretty:freelancermain";
        } catch (LoginException e) {
            LOGGER.error("Fehler beim Login", e);
            JSFMessageUtils.addGlobalErrorMessage(MSG_FALSCHESLOGIN);
            return null;
        }
    }
}