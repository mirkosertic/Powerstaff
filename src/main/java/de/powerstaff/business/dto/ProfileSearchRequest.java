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
package de.powerstaff.business.dto;

import org.apache.commons.lang.StringUtils;

import de.mogwai.common.business.dto.DataTransferObject;

public class ProfileSearchRequest extends DataTransferObject implements
		SearchRequestSupport {

	private String profileContent = "";

	private String plz;

	private Long stundensatzVon;

	private Long stundensatzBis;

	private String sortierung;

	/**
	 * @return the profileContent
	 */
	public String getProfileContent() {
		return profileContent;
	}

	/**
	 * @param profileContent
	 *            the profileContent to set
	 */
	public void setProfileContent(String profileContent) {
		this.profileContent = profileContent;
	}

	public boolean isExtendedSearch() {
		return (!StringUtils.isEmpty(plz)) || (stundensatzVon != null)
				|| (stundensatzBis != null);
	}

	/**
	 * @return the plz
	 */
	public String getPlz() {
		return plz;
	}

	/**
	 * @param plz
	 *            the plz to set
	 */
	public void setPlz(String plz) {
		this.plz = plz;
	}

	/**
	 * @return the stundensatzVon
	 */
	public Long getStundensatzVon() {
		return stundensatzVon;
	}

	/**
	 * @param stundensatzVon
	 *            the stundensatzVon to set
	 */
	public void setStundensatzVon(Long stundensatzVon) {
		this.stundensatzVon = stundensatzVon;
	}

	/**
	 * @return the stundensatzBis
	 */
	public Long getStundensatzBis() {
		return stundensatzBis;
	}

	/**
	 * @param stundensatzBis
	 *            the stundensatzBis to set
	 */
	public void setStundensatzBis(Long stundensatzBis) {
		this.stundensatzBis = stundensatzBis;
	}

	public String getSortierung() {
		return sortierung;
	}

	public void setSortierung(String sortierung) {
		this.sortierung = sortierung;
	}
}