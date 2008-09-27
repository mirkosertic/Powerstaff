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
package de.powerstaff.business.entity;

import de.mogwai.common.business.entity.AuditableEntity;
import de.mogwai.common.utils.StringPresentationProvider;

public class ContactType extends AuditableEntity implements StringPresentationProvider {

    private String description;

    private boolean phone;

    private boolean fax;

    private boolean email;

    private boolean web;
    
    private boolean gulp;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isFax() {
        return fax;
    }

    public void setFax(boolean fax) {
        this.fax = fax;
    }

    public boolean isPhone() {
        return phone;
    }

    public void setPhone(boolean phone) {
        this.phone = phone;
    }

    public boolean isWeb() {
        return web;
    }

    public void setWeb(boolean web) {
        this.web = web;
    }

    public String getStringPresentation() {
        return description;
    }

    /**
     * @return the gulp
     */
    public boolean isGulp() {
        return gulp;
    }

    /**
     * @param gulp the gulp to set
     */
    public void setGulp(boolean gulp) {
        this.gulp = gulp;
    }
}
