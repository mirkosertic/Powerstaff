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
package de.powerstaff.business.service.impl;

import de.mogwai.common.business.service.impl.LogableService;
import de.powerstaff.business.dao.AdditionalDataDAO;
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.HistoryType;
import de.powerstaff.business.entity.User;
import de.powerstaff.business.service.AdditionalDataService;

import java.util.List;

public class AdditionalDataServiceImpl extends LogableService implements AdditionalDataService {

    private AdditionalDataDAO additionalDataDAO;

    public void setAdditionalDataDAO(AdditionalDataDAO additionalDataDAO) {
        this.additionalDataDAO = additionalDataDAO;
    }

    public List<ContactType> getContactTypes() {
        return additionalDataDAO.getContactTypes();
    }

    @Override
    public List<HistoryType> getHistoryTypes() {
        return additionalDataDAO.getHistoryTypes();
    }

    @Override
    public List<User> getUserList() {
        return additionalDataDAO.getUserList();
    }
}
