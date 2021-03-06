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

import de.mogwai.common.business.enums.BaseEnumItemEnum;

/**
 * Data Access Object (DAO) Interface für Systemparameter.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:32:27 $
 */
public interface SystemParameterDao extends DAO {

    /**
     * Liest alle Systemparameter sortiert nach id.
     * 
     * @return alle Systemparameter sortiert nach id.
     */
    List getAll();

    /**
     * Ermitteln eines Parameters als String.
     * 
     * @param aEnumItem
     *                der Parameter
     * @return der String
     */
    String getString(BaseEnumItemEnum aEnumItem);

    /**
     * Setzen eines Parameters als String.
     * 
     * @param aEnumItem
     *                der Parameter
     * @param aName
     *                Name des Parameters
     * @param aValue
     *                der Wert
     */
    void setString(BaseEnumItemEnum aEnumItem, String aName, String aValue);

    /**
     * Ermitteln eines Parameters als Boolean.
     * 
     * @param aEnumItem
     *                der Parameter
     * @return der String
     */
    Boolean getBoolean(BaseEnumItemEnum aEnumItem);

    /**
     * Setzen eines Parameters als String.
     * 
     * @param aEnumItem
     *                der Parameter
     * @param aName
     *                Name des Parameters
     * @param aValue
     *                der Wert
     */
    void setBoolean(BaseEnumItemEnum aEnumItem, String aName, Boolean aValue);

    /**
     * Ermitteln eines Parameters als int.
     * 
     * @param aEnumItem
     *                der Parameter
     * @return der String
     */
    int getInt(BaseEnumItemEnum aEnumItem);

    /**
     * Setzen eines Parameters als int.
     * 
     * @param aEnumItem
     *                der Parameter
     * @param aName
     *                Name des Parameters
     * @param aValue
     *                der Wert
     */
    void setInt(BaseEnumItemEnum aEnumItem, String aName, int aValue);

    void save(Object aValue);

    void update(Object aValue);
}
