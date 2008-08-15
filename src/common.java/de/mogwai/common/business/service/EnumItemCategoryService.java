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

import java.util.List;
import java.util.Map;

import de.mogwai.common.business.entity.EnumItemCategory;

/**
 * Service Interface für EnumItemCategory (Codetabellen für Comboboxen usw).
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:11:30 $
 */
public interface EnumItemCategoryService extends Service {

    /**
     * Liest eine Liste aller EnumItemCategories.
     * 
     * @return Liste aller EnumItemCategories.
     */
    List<EnumItemCategory> getEnumItemCategoryList();

    /**
     * Liest eine Liste aller EnumItemCategories.
     * 
     * @return Liste aller EnumItemCategories.
     */
    List<EnumItemCategory> getUserEnumItemCategoryList();

    /**
     * Liest eine bestimmte EnumItemCategory.
     * 
     * @param id
     *                ID der gesuchten EnumItemCategory.
     * @return gesuchte EnumItemCategory.
     */
    EnumItemCategory getEnumItemCategory(Long id);

    /**
     * Speichert eine neue EnumItemCategory. Zugehörige EnumItems werden
     * ebenfalls gespeichert.
     * 
     * @param enumItemCategory
     *                Zu speichernde EnumItemCategory. Id muss null sein, da sie
     *                von der DAO-Schicht vergeben wird.
     */
    void insertEnumItemCategory(EnumItemCategory enumItemCategory);

    /**
     * Speichert eine bestehende EnumItemCategory. Zugehörige EnumItems werden
     * ebenfalls gespeichert. Insert, Update und Delete von EnumItems wird über
     * diese Methode realisiert (Manipulation am Set von EnumItems, welches als
     * Attribut der EnumItemCategory geführt wird).
     * 
     * @param enumItemCategory
     *                Zu speichernde EnumItemCategory.
     */
    void updateEnumItemCategory(EnumItemCategory enumItemCategory);

    /**
     * Speichert eine neue <code>EnumItemCategory</code>, oder Aktualisiert
     * eine bestehende <code>EnumItemCategory</code>.
     * 
     * @param enumItemCategory
     *                EnumItemCategory auf welcher SaveOrUpdate ausgeführt wird.
     */
    void saveOrUpdateEnumItemCategory(EnumItemCategory enumItemCategory);

    /**
     * Liest eine Map mit allen EnumItemCategories. Key ist
     * <code>EnumItemCategory.id</code>.
     * 
     * @return Map mit allen EnumItemCategories. Key ist
     *         <code>EnumItemCategory.id</code>.
     */
    Map<Long, EnumItemCategory> getEnumItemCategoryMap();

}
