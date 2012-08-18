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
package de.mogwai.common.business.service.impl;

import de.mogwai.common.business.entity.Entity;
import de.mogwai.common.business.entity.LockEntry;
import de.mogwai.common.business.service.LockService;
import de.mogwai.common.business.service.ObjectLockedException;
import de.mogwai.common.dao.LockServiceDao;
import de.mogwai.common.usercontext.UserContextHolder;
import de.powerstaff.business.service.ReferenceExistsException;

import java.sql.Timestamp;

/**
 * Implementation des LockService.
 *
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:27:33 $
 */
public class LockServiceImpl extends LogableService implements LockService {

    private LockServiceDao lockServiceDao;

    public LockServiceDao getLockServiceDao() {
        return lockServiceDao;
    }

    public void setLockServiceDao(LockServiceDao lockServiceDao) {
        this.lockServiceDao = lockServiceDao;
    }

    /**
     * Überprüfung, ob ein LockEntry von der aktuellen Session gesperrt wurde.
     *
     * @param aEntry das LockEntry
     * @return true wenn ja, sonst false
     */
    protected boolean lockedByCurrentUser(LockEntry aEntry) {

        return (aEntry.getLockUser().equals(UserContextHolder.getUserContext().getAuthenticatable().getUserId()))
                && (aEntry.getSessionId().equals(UserContextHolder.getUserContext().getSessionId()));

    }

    public boolean isObjectLocked(Entity aEntity) {

        if ((aEntity == null) || (aEntity.getId() == null)) {
            return false;
        }

        LockEntry theEntry = lockServiceDao.getLockEntryFor(aEntity);

        if (theEntry == null) {
            return false;
        }

        return !lockedByCurrentUser(theEntry);
    }

    public void lockObject(String aUserID, Entity aEntity) {

        if ((aEntity == null) || (aEntity.getId() == null)) {
            return;
        }

        LockEntry theEntry = lockServiceDao.getLockEntryFor(aEntity);

        if (theEntry != null) {
            if (!lockedByCurrentUser(theEntry)) {
                throw new ObjectLockedException(aEntity, theEntry.getLockUser(), theEntry.getLockDate());
            }

        } else {
            theEntry = new LockEntry();
            theEntry.setObjectId(aEntity.getId());
            theEntry.setObjectName(lockServiceDao.getObjectName(aEntity));
            theEntry.setLockUser(aUserID);
            theEntry.setSessionId(UserContextHolder.getUserContext().getSessionId());
        }

        theEntry.setLockDate(new Timestamp(System.currentTimeMillis()));

        lockServiceDao.save(theEntry);
    }

    public void unlockObject(String aUserID, Entity aEntity) {

        if ((aEntity == null) || (aEntity.getId() == null)) {
            return;
        }

        LockEntry theEntry = lockServiceDao.getLockEntryFor(aEntity);

        if (lockedByCurrentUser(theEntry)) {
            try {
                lockServiceDao.delete(theEntry);
            } catch (ReferenceExistsException e) {
                // Kann hier nicht passieren
            }
        } else {
            throw new ObjectLockedException(aEntity, theEntry.getLockUser(), theEntry.getLockDate());
        }

    }

    public void removeLocksForUser(String aUserID) {
        lockServiceDao.removeLocksForUser(aUserID);
    }

    public void removeLocksForSession(String aSessionID) {
        lockServiceDao.removeLocksForSession(aSessionID);
    }

    public void removeLocksForCurrentSession() {
        lockServiceDao.removeLocksForSession(UserContextHolder.getUserContext().getSessionId());
    }

    public void lockObjectByCurrentUser(Entity aEntity) {
        lockObject(UserContextHolder.getUserContext().getAuthenticatable().getUserId(), aEntity);
    }

    public void unlockObjectbyCurrentUser(Entity aEntity) {
        unlockObject(UserContextHolder.getUserContext().getAuthenticatable().getUserId(), aEntity);
    }
}
