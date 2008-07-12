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

import de.mogwai.common.business.entity.EnumItemCategory;
import de.mogwai.common.business.service.EnumItemCategoryService;
import de.mogwai.common.dao.EnumItemCategoryDao;
import de.mogwai.common.exception.BusinessException;

/**
 * Service Implementation für EnumItemCategory (Codetabellen für Comboboxen
 * usw).
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:46 $
 */
public class EnumItemCategoryServiceImpl extends LogableService implements EnumItemCategoryService {

    private EnumItemCategoryDao enumItemCategoryDao;

    /**
     * Gibt den Wert des Attributs <code>enumItemCategoryDao</code> zurück.
     * 
     * @return Wert des Attributs enumItemCategoryDao.
     */
    public EnumItemCategoryDao getEnumItemCategoryDao() {
        return enumItemCategoryDao;
    }

    /**
     * Setzt den Wert des Attributs <code>enumItemCategoryDao</code>.
     * 
     * @param enumItemCategoryDao
     *                Wert für das Attribut enumItemCategoryDao.
     */
    public void setEnumItemCategoryDao(EnumItemCategoryDao enumItemCategoryDao) {
        this.enumItemCategoryDao = enumItemCategoryDao;
    }

    /**
     * @see de.mogwai.common.business.service.EnumItemCategoryService#getEnumItemCategoryList()
     */
    public List<EnumItemCategory> getEnumItemCategoryList() {

        return this.enumItemCategoryDao.getAll();
    }

    /**
     * @see de.mogwai.common.business.service.EnumItemCategoryService#getUserEnumItemCategoryList()
     */
    public List<EnumItemCategory> getUserEnumItemCategoryList() {

        return this.enumItemCategoryDao.getUserCategories();
    }

    /**
     * @see de.mogwai.common.business.service.EnumItemCategoryService#getEnumItemCategory(java.lang.Long)
     */
    public EnumItemCategory getEnumItemCategory(Long id) {

        return (EnumItemCategory) this.enumItemCategoryDao.getById(EnumItemCategory.class, id);
    }

    /**
     * @see de.mogwai.common.business.service.EnumItemCategoryService#insertEnumItemCategory(de.mogwai.common.business.entity.EnumItemCategory)
     */
    public void insertEnumItemCategory(EnumItemCategory enumItemCategory) {

        if (enumItemCategory.getId() != null) {
            throw new BusinessException("enumItemCategory.id muss für insert null sein. Id wird von DB vergeben.");
        }

        this.enumItemCategoryDao.save(enumItemCategory);
    }

    /**
     * @see de.mogwai.common.business.service.EnumItemCategoryService#updateEnumItemCategory(de.mogwai.common.business.entity.EnumItemCategory)
     */
    public void updateEnumItemCategory(EnumItemCategory enumItemCategory) {

        if (enumItemCategory.getId() == null) {
            throw new BusinessException("enumItemCategory.id muss für update vorhanden sein.");
        }

        this.enumItemCategoryDao.save(enumItemCategory);
    }

    /**
     * @see de.mogwai.common.business.service.EnumItemCategoryService#saveOrUpdateEnumItemCategory(de.mogwai.common.business.entity.EnumItemCategory)
     */
    public void saveOrUpdateEnumItemCategory(EnumItemCategory enumItemCategory) {

        this.enumItemCategoryDao.save(enumItemCategory);
    }

    /**
     * @see de.mogwai.common.business.service.EnumItemCategoryService#getEnumItemCategoryMap()
     */
    public Map<Long, EnumItemCategory> getEnumItemCategoryMap() {

        Map<Long, EnumItemCategory> enumItemCategoryMap = new HashMap<Long, EnumItemCategory>();
        List enumItemCategoryList = this.enumItemCategoryDao.getAll();

        for (Object enumItemCategory : enumItemCategoryList) {
            enumItemCategoryMap.put(((EnumItemCategory) enumItemCategory).getId(), (EnumItemCategory) enumItemCategory);
        }
        return enumItemCategoryMap;
    }

}
