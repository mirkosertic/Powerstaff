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
package de.mogwai.common.dao;

import java.util.List;

import de.mogwai.common.business.entity.EnumItem;
import de.mogwai.common.business.enums.BaseEnumItemEnum;

/**
 * Data Access Object (DAO) Interface für EnumItems.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:12:38 $
 */
public interface EnumItemDao extends DAO {

    /**
     * Liest alle EnumItems sortiert nach id.
     * 
     * @return alle EnumItems sortiert nach id.
     */
    List getAll();

    /**
     * Ermittlung eines EnumItems anhand der ID.
     * 
     * @param aId
     * @return das EnumItem oder null wenn nichts gefunden wurde
     */
    EnumItem getById(long aId);

    /**
     * Ermittlung des EnumItems anhand eines Enums.
     * 
     * @param aEnum
     *                das Enum
     * @return das EnumItem
     */
    EnumItem getByEnum(BaseEnumItemEnum aEnum);
}
