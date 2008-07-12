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
 * Basisklasse für History - Entitäten.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:39 $
 * @param <T>
 *                Das zu historisierende Entity
 */
public abstract class HistoryEntity<T extends Entity> extends Entity {

    public static final String REASON_INSERT = "I";

    public static final String REASON_UPDATE = "U";

    public static final String REASON_DELETE = "D";

    private String modificationUserID;

    private Timestamp modificationDate;

    private String reason;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Timestamp getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Timestamp modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getModificationUserID() {
        return modificationUserID;
    }

    public void setModificationUserID(String modificationUserID) {
        this.modificationUserID = modificationUserID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
