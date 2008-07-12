package de.powerstaff.web.backingbean.login;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.persistence.Column;

import de.mogwai.common.web.backingbean.BackingBeanDataModel;
import de.mogwai.common.web.component.ComponentUtils;

public class LoginBackingBeanDataModel extends BackingBeanDataModel {

    private UIComponent viewRoot;

    private String username;

    private String password;

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *                the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the username
     */
    @Column(nullable = false, length = 255)
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *                the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the viewRoot
     */
    public UIComponent getViewRoot() {
        return viewRoot;
    }

    /**
     * @param viewRoot
     *                the viewRoot to set
     */
    public void setViewRoot(UIComponent viewRoot) {
        this.viewRoot = viewRoot;
    }

    public List<String> getChangedComponents() {
        List<String> theResult = ComponentUtils.getDynamicContentComponentIDs(viewRoot);
        return theResult;
    }
}
