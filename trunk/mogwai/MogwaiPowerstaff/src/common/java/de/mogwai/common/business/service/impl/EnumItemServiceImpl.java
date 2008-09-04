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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mogwai.common.business.entity.EnumItem;
import de.mogwai.common.business.service.EnumItemService;
import de.mogwai.common.dao.EnumItemDao;

/**
 * Service Implementation für Codes (Codetabellen für Comboboxen usw).
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:27:35 $
 */
public class EnumItemServiceImpl extends LogableService implements EnumItemService {

    private EnumItemDao enumItemDao;

    /**
     * Gibt den Wert des Attributs <code>enumItemDao</code> zurück.
     * 
     * @return Wert des Attributs enumItemDao.
     */
    public EnumItemDao getEnumItemDao() {
        return enumItemDao;
    }

    /**
     * Setzt den Wert des Attributs <code>enumItemDao</code>.
     * 
     * @param enumItemDao
     *                Wert für das Attribut enumItemDao.
     */
    public void setEnumItemDao(EnumItemDao enumItemDao) {
        this.enumItemDao = enumItemDao;
    }

    /**
     * @see de.mogwai.common.business.service.EnumItemService#getEnumItemMap()
     */
    public Map<Long, EnumItem> getEnumItemMap() {

        Map<Long, EnumItem> enumItemMap = new HashMap<Long, EnumItem>();
        List enumItemList = this.enumItemDao.getAll();

        for (Object enumItem : enumItemList) {
            enumItemMap.put(((EnumItem) enumItem).getId(), (EnumItem) enumItem);
        }
        return enumItemMap;
    }
}
