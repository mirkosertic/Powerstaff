/**
 * Copyright 2002 - 2007 the Mogwai Project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.mogwai.common.business.entity;

import java.sql.Timestamp;

/**
 * Entity providing a kind of audit log for change tracking.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:16:33 $
 */
public class AuditableEntity extends Entity {

	private static final long serialVersionUID = 1453762400693078426L;

	private Timestamp creationDate = new Timestamp(System.currentTimeMillis());

	private String creationUserID;

	private Timestamp lastModificationDate;

	private String lastModificationUserID;

    public AuditableEntity() {
    }

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationUserID() {
		return creationUserID;
	}

	public void setCreationUserID(String creationUserID) {
		this.creationUserID = creationUserID;
	}

	public Timestamp getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Timestamp lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	public String getLastModificationUserID() {
		return lastModificationUserID;
	}

	public void setLastModificationUserID(String lastModificationUserID) {
		this.lastModificationUserID = lastModificationUserID;
	}
}
