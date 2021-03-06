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
package de.powerstaff.business.dao;

import de.mogwai.common.dao.DAO;
import de.powerstaff.business.dto.KontakthistorieEntry;
import de.powerstaff.business.entity.User;

import java.util.Date;
import java.util.List;

public interface StatistikDAO extends DAO {

    List<KontakthistorieEntry> kontakthistorie(Date aDatumVon, Date aDatumBis, User aBenutzer);

}
