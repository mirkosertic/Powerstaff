package de.powerstaff.web.backingbean.login;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.ui.WebAuthenticationDetails;

import de.mogwai.common.business.service.LoginException;
import de.mogwai.common.business.service.LoginService;
import de.mogwai.common.web.backingbean.WrappingBackingBean;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.web.backingbean.MessageConstants;

public class LoginBackingBean extends WrappingBackingBean<LoginBackingBeanDataModel> implements MessageConstants {

    private LoginService loginService;

    @Override
    protected LoginBackingBeanDataModel createDataModel() {
        return new LoginBackingBeanDataModel();
    }
    
    public LoginService getLoginService() {
        return loginService;
    }

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    public String commandLogin() {
        try {
            HttpServletRequest theRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            loginService.login(getData().getUsername(), getData().getPassword(), new WebAuthenticationDetails(theRequest));
            return "INDEX";
        } catch (LoginException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FALSCHESLOGIN);
            return null;
        }
    }
}