/**
 * Mogwai KIAS. Copyright (C) 2002 The Mogwai Project.
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

import java.sql.Timestamp;

import de.mogwai.common.business.entity.AuditableEntity;

public class NewsletterMail extends AuditableEntity {

	private String mail;

	private Timestamp registeredTimestamp;

	private Timestamp confirmedTimestamp;

	private Boolean confirmed;
	
	private int errorcounter;

	public Boolean isConfirmed() {
		return confirmed;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Timestamp getConfirmedTimestamp() {
		return confirmedTimestamp;
	}

	public void setConfirmedTimestamp(Timestamp confirmedTimestamp) {
		this.confirmedTimestamp = confirmedTimestamp;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Timestamp getRegisteredTimestamp() {
		return registeredTimestamp;
	}

	public void setRegisteredTimestamp(Timestamp registeredTimestamp) {
		this.registeredTimestamp = registeredTimestamp;
	}

	public int getErrorcounter() {
		return errorcounter;
	}

	public void setErrorcounter(int errorcounter) {
		this.errorcounter = errorcounter;
	}

	public void setConfirmed(String aValue) {
		setConfirmed("1".equals(aValue));
	}
}
