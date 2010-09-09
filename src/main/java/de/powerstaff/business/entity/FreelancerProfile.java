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

import java.io.Serializable;

public class FreelancerProfile implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private String fileName;

	private String infotext;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FreelancerProfile() {
	}

	public FreelancerProfile(String aName) {
		name = aName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isWordProfile() {
		return fileName.toLowerCase().endsWith(".doc");
	}

	public boolean isTextProfile() {
		return fileName.toLowerCase().endsWith(".txt");
	}

	/**
	 * @return the infotext
	 */
	public String getInfotext() {
		return infotext;
	}

	/**
	 * @param infotext
	 *            the infotext to set
	 */
	public void setInfotext(String infotext) {
		this.infotext = infotext;
	}
}