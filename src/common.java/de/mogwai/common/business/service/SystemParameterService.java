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

import java.util.Map;

import de.mogwai.common.business.entity.SystemParameter;
import de.mogwai.common.business.enums.BaseEnumItemEnum;

/**
 * Service Interface für Systemparameter.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:11:31 $
 */
public interface SystemParameterService extends Service {

    /**
     * Liest eine Map aller Systemparameter. Key der Map ist
     * SystemParameter.getId().
     * 
     * @return Map aller Systemparameter.
     */
    Map<Long, SystemParameter> getSystemParameterMap();

    /**
     * Liest eine Systemparameter mit bestimmter ID.
     * 
     * @param id
     *                Systemparameter-ID.
     * @return Systemparameter mit bestimmter ID.
     */
    SystemParameter getSystemParameter(Long id);

    /**
     * Liest eine Systemparameter mit bestimmter ID.
     * 
     * @param aEnum
     *                Systemparameter-Enum.
     * @return Systemparameter mit bestimmter ID.
     */
    SystemParameter getSystemParameter(BaseEnumItemEnum aEnum);
}
