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
package de.powerstaff.business.service;

import de.mogwai.common.business.entity.Entity;
import de.powerstaff.business.dao.GenericSearchResult;

import java.util.Collection;

public interface NavigatingService<T extends Entity> {

    T findByPrimaryKey(Long id);

    void save(T aBo) throws OptimisticLockException;

    void delete(T aBo) throws ReferenceExistsException, OptimisticLockException;

    T findFirst();

    T findLast();

    T findPrior(T aObject);

    T findNext(T aObject);

    RecordInfo getRecordInfo(T aObject);

    Collection<GenericSearchResult> performQBESearch(T aObject) throws TooManySearchResults;

    T findByRecordNumber(Long aNumber);
}
