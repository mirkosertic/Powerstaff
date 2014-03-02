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

import java.io.File;
import java.io.Serializable;

public class FreelancerProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String fileName;

    private String infotext;

    private File fileOnserver;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FreelancerProfile() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWordProfile() {
        return fileName.toLowerCase().endsWith(".doc") || fileName.toLowerCase().endsWith(".docx");
    }

    public boolean isTextProfile() {
        return fileName.toLowerCase().endsWith(".txt");
    }

    public boolean isOtherFormat() {
        return !isWordProfile() && !isTextProfile();
    }

    public String getInfotext() {
        return infotext;
    }

    public void setInfotext(String infotext) {
        this.infotext = infotext;
    }

    public File getFileOnserver() {
        return fileOnserver;
    }

    public void setFileOnserver(File fileOnserver) {
        this.fileOnserver = fileOnserver;
    }
}