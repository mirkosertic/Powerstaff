package de.powerstaff.web.backingbean.login;

import de.mogwai.common.web.backingbean.WrappingBackingBean;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.service.AuthenticationService;
import de.powerstaff.web.backingbean.MessageConstants;

public class LoginBackingBean extends WrappingBackingBean<LoginBackingBeanDataModel> implements MessageConstants {

    private AuthenticationService authenticationService;

    /**
     * @return the authenticationService
     */
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    /**
     * @param authenticationService
     *                the authenticationService to set
     */
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected LoginBackingBeanDataModel createDataModel() {
        return new LoginBackingBeanDataModel();
    }

    public String commandLogin() {
        if (authenticationService.login(getData().getUsername(), getData().getPassword()) != null) {
            return "INDEX";
        }

        JSFMessageUtils.addGlobalErrorMessage(MSG_FALSCHESLOGIN);
        return null;
    }
}