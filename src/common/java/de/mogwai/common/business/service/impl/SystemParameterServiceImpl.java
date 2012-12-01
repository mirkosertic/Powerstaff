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

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mogwai.common.business.entity.SystemParameter;
import de.mogwai.common.business.enums.BaseEnumItemEnum;
import de.mogwai.common.business.service.SystemParameterService;
import de.mogwai.common.dao.SystemParameterDao;

/**
 * Service Implementation für Systemparameter. Die Systemparameter werden nicht
 * gecached, damit Änderungen in der Datenbank sofort übernommen werden.
 * 
 * Hinweis: Spring instanziiert den Service genau einmal.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:27:35 $
 */
public class SystemParameterServiceImpl extends LogableService implements SystemParameterService {

    private SystemParameterDao systemParameterDao;

    /**
     * Setzt den Wert des Attributs <code>systemParameterDao</code>.
     * 
     * @param systemParameterDao
     *                Wert für das Attribut systemParameterDao.
     */
    public void setSystemParameterDao(SystemParameterDao systemParameterDao) {
        this.systemParameterDao = systemParameterDao;
    }

    /**
     * @see de.mogwai.common.business.service.SystemParameterService#getSystemParameterMap()
     */
    public Map<Long, SystemParameter> getSystemParameterMap() {

        Map<Long, SystemParameter> parameterMap = new HashMap<Long, SystemParameter>();
        List parameterList = this.systemParameterDao.getAll();

        for (Object parameter : parameterList) {
            parameterMap.put(((SystemParameter) parameter).getId(), (SystemParameter) parameter);
        }
        return parameterMap;
    }

    /**
     * @see de.mogwai.common.business.service.SystemParameterService#getSystemParameter(java.lang.Long)
     */
    public SystemParameter getSystemParameter(Long id) {

        SystemParameter systemParameter = (SystemParameter) this.systemParameterDao.getById(SystemParameter.class, id);

        if (systemParameter == null) {
            String msg = MessageFormat.format("Es existiert kein Systemparamter mit ID {0}.", id);
            throw new IllegalArgumentException(msg);
        }
        return systemParameter;
    }

    /**
     * @see de.mogwai.common.business.service.SystemParameterService#getSystemParameter(de.mogwai.common.business.enums.BaseEnumItemEnum)
     */
    public SystemParameter getSystemParameter(BaseEnumItemEnum aEnum) {
        return getSystemParameter(aEnum.getId());
    }

    public Long getLong(BaseEnumItemEnum aEnum, Long aDefault) {
        SystemParameter theParameter = (SystemParameter) systemParameterDao.getById(SystemParameter.class, aEnum
                .getId());
        if (theParameter != null) {
            String theValue = theParameter.getValue();
            if ((theValue == null) || ("".equals(theValue))) {
                return aDefault;
            }
            return Long.parseLong(theValue);
        }
        return aDefault;
    }

    public Integer getInt(BaseEnumItemEnum aEnum, Integer aDefault) {
        SystemParameter theParameter = (SystemParameter) systemParameterDao.getById(SystemParameter.class, aEnum
                .getId());
        if (theParameter != null) {
            String theValue = theParameter.getValue();
            if ((theValue == null) || ("".equals(theValue))) {
                return aDefault;
            }
            return Integer.parseInt(theValue);
        }
        return aDefault;
    }

    public Boolean getBoolean(BaseEnumItemEnum aEnum, Boolean aDefault) {
        SystemParameter theParameter = (SystemParameter) systemParameterDao.getById(SystemParameter.class, aEnum
                .getId());
        if (theParameter != null) {
            String theValue = theParameter.getValue();
            if ((theValue == null) || ("".equals(theValue))) {
                return aDefault;
            }
            return Boolean.parseBoolean(theValue);
        }
        return aDefault;
    }

    public String getString(BaseEnumItemEnum aEnum, String aDefault) {
        SystemParameter theParameter = (SystemParameter) systemParameterDao.getById(SystemParameter.class, aEnum
                .getId());
        if (theParameter != null) {
            String theValue = theParameter.getValue();
            if ((theValue == null) || ("".equals(theValue))) {
                return aDefault;
            }
            return theValue;
        }
        return aDefault;
    }

    public void setString(BaseEnumItemEnum aEnum, String aValue) {
        SystemParameter theParameter = (SystemParameter) systemParameterDao.getById(SystemParameter.class, aEnum
                .getId());
        if (theParameter != null) {
            theParameter.setValue(aValue);
            systemParameterDao.update(theParameter);
        } else {
            theParameter = new SystemParameter();
            theParameter.setId(aEnum.getId());
            theParameter.setName(aEnum.name());
            theParameter.setValue(aValue);
            systemParameterDao.save(theParameter);
        }
    }

    public void setLong(BaseEnumItemEnum aEnum, Long aValue) {
        SystemParameter theParameter = (SystemParameter) systemParameterDao.getById(SystemParameter.class, aEnum
                .getId());
        if (theParameter != null) {
            theParameter.setValue(aValue != null ? aValue.toString() : null);
            systemParameterDao.update(theParameter);
        } else {
            theParameter = new SystemParameter();
            theParameter.setId(aEnum.getId());
            theParameter.setName(aEnum.name());
            theParameter.setValue(aValue != null ? aValue.toString() : null);
            systemParameterDao.save(theParameter);
        }
    }

    public void setInt(BaseEnumItemEnum aEnum, Integer aValue) {
        SystemParameter theParameter = (SystemParameter) systemParameterDao.getById(SystemParameter.class, aEnum
                .getId());
        if (theParameter != null) {
            theParameter.setValue(aValue != null ? aValue.toString() : null);
            systemParameterDao.update(theParameter);
        } else {
            theParameter = new SystemParameter();
            theParameter.setId(aEnum.getId());
            theParameter.setName(aEnum.name());
            theParameter.setValue(aValue != null ? aValue.toString() : null);
            systemParameterDao.save(theParameter);
        }
    }

    public void setBoolean(BaseEnumItemEnum aEnum, Boolean aValue) {
        SystemParameter theParameter = (SystemParameter) systemParameterDao.getById(SystemParameter.class, aEnum
                .getId());
        if (theParameter != null) {
            theParameter.setValue(aValue != null ? aValue.toString() : null);
            systemParameterDao.update(theParameter);
        } else {
            theParameter = new SystemParameter();
            theParameter.setId(aEnum.getId());
            theParameter.setName(aEnum.name());
            theParameter.setValue(aValue != null ? aValue.toString() : null);
            systemParameterDao.save(theParameter);
        }
    }

}
