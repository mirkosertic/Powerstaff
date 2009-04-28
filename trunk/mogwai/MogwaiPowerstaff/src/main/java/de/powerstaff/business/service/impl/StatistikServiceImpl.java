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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.powerstaff.business.dao.StatistikDAO;
import de.powerstaff.business.dto.KontakthistorieEntry;
import de.powerstaff.business.entity.User;
import de.powerstaff.business.service.StatisticService;

public class StatistikServiceImpl implements StatisticService {
    
    private StatistikDAO statistikDAO;
    
    /**
     * @return the statistikDAO
     */
    public StatistikDAO getStatistikDAO() {
        return statistikDAO;
    }

    /**
     * @param statistikDAO the statistikDAO to set
     */
    public void setStatistikDAO(StatistikDAO statistikDAO) {
        this.statistikDAO = statistikDAO;
    }

    @Override
    public List<KontakthistorieEntry> kontakthistorie(Date aDatumVon, Date aDatumBis, User aBenutzer) {
        Calendar theCalendar = Calendar.getInstance();
        if (aDatumVon != null) {
            theCalendar.setTime(aDatumVon);
            theCalendar.set(Calendar.HOUR_OF_DAY, 0);
            theCalendar.set(Calendar.MINUTE, 0);
            theCalendar.set(Calendar.SECOND, 0);
            theCalendar.set(Calendar.MILLISECOND, 0);
            aDatumVon = theCalendar.getTime();
        }
        
        if (aDatumBis != null) {
            theCalendar.setTime(aDatumBis);
            theCalendar.set(Calendar.HOUR_OF_DAY, 23);
            theCalendar.set(Calendar.MINUTE, 59);
            theCalendar.set(Calendar.SECOND, 59);
            theCalendar.set(Calendar.MILLISECOND, 0);
            aDatumBis = theCalendar.getTime();
        }
        
        return statistikDAO.kontakthistorie(aDatumVon, aDatumBis, aBenutzer);
    }
}