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

import java.util.List;

import javax.faces.component.UIComponent;
import javax.persistence.Column;

import de.mogwai.common.web.backingbean.BackingBeanDataModel;
import de.mogwai.common.web.component.ComponentUtils;

public class LoginBackingBeanDataModel extends BackingBeanDataModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6679747434377687583L;

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
