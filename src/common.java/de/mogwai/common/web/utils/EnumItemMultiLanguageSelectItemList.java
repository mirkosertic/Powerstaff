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
package de.mogwai.common.web.utils;

import java.util.List;
import java.util.Vector;

import de.mogwai.common.business.entity.EnumItem;
import de.mogwai.common.business.enums.BaseEnumItemEnum;
import de.mogwai.common.web.model.EnumItemMultiLanguageSelectItem;

/**
 * Spezialisierte Liste aus EnumItemMultiLanguageSelectItem.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:09:09 $
 */
public class EnumItemMultiLanguageSelectItemList extends Vector<EnumItemMultiLanguageSelectItem> {

    /**
     * Default - Konstruktor.
     */
    public EnumItemMultiLanguageSelectItemList() {
    }

    /**
     * Copy - Konstruktor.
     * 
     * Es werden alle Einträge aus einer bestehenden Liste in die neue
     * Übernommen
     * 
     * @param aData
     *                die Liste der Daten
     */
    public EnumItemMultiLanguageSelectItemList(List<EnumItemMultiLanguageSelectItem> aData) {
        super(aData);
    }

    /**
     * Entfernt alle Enums aus der Liste.
     * 
     * @param aEnum
     *                die Liste der Enums, die entfernt werden sollen
     */
    public void removeByEnum(BaseEnumItemEnum... aEnum) {

        Vector<EnumItemMultiLanguageSelectItem> itemsToRemove = new Vector<EnumItemMultiLanguageSelectItem>();
        for (EnumItemMultiLanguageSelectItem theItem : this) {
            for (BaseEnumItemEnum theEnum : aEnum) {
                if (((EnumItem) theItem.getValue()).getId().equals(theEnum.getId())) {
                    itemsToRemove.add(theItem);
                }
            }
        }

        removeAll(itemsToRemove);
    }

    /**
     * Entfernen aller Enums aus der Liste, die nicht definiert sind.
     * 
     * @param aEnum
     *                die Liste der Enums, die behalten werden soll
     */
    public void leaveByEnum(BaseEnumItemEnum... aEnum) {

        Vector<EnumItemMultiLanguageSelectItem> itemsToRemove = new Vector<EnumItemMultiLanguageSelectItem>();
        for (EnumItemMultiLanguageSelectItem theItem : this) {

            boolean theLeave = true;
            for (BaseEnumItemEnum theEnum : aEnum) {
                if (!((EnumItem) theItem.getValue()).getId().equals(theEnum.getId())) {
                    theLeave = false;
                }
            }

            if (!theLeave) {
                itemsToRemove.add(theItem);
            }
        }

        removeAll(itemsToRemove);
    }

}
