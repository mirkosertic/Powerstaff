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

import de.mogwai.common.web.backingbean.BackingBeanDataModel;

import javax.persistence.Column;

public class LoginBackingBeanDataModel extends BackingBeanDataModel {

    private static final long serialVersionUID = -6679747434377687583L;

    private String username;

    private String password;

    private boolean loginWithXing;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(nullable = false, length = 255)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLoginWithXing() {
        return loginWithXing;
    }

    public void setLoginWithXing(boolean loginWithXing) {
        this.loginWithXing = loginWithXing;
    }
}
