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
package de.mogwai.common.business.service;

import java.sql.Timestamp;

import de.mogwai.common.business.entity.Entity;

/**
 * Exception für den LockService.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:18 $
 */
public class ObjectLockedException extends RuntimeException {

    private Entity entity;

    private String lockUserId;

    private Timestamp lockTimestamp;

    public ObjectLockedException(Entity aEntity, String aUserID, Timestamp aLockTimestamp) {
        lockUserId = aUserID;
        lockTimestamp = aLockTimestamp;
        entity = aEntity;
    }

    public Timestamp getLockTimestamp() {
        return lockTimestamp;
    }

    public String getLockUserId() {
        return lockUserId;
    }

    public Entity getEntity() {
        return entity;
    }
}
