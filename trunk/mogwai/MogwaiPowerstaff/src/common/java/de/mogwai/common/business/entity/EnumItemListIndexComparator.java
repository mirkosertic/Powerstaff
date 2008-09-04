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
package de.mogwai.common.business.entity;

import java.util.Comparator;

/**
 * Comparator für Sortierung nach listIndex von EnumItems.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:16:39 $
 */
public class EnumItemListIndexComparator implements Comparator<EnumItem> {
    /**
     * @see java.util.Comparator#compare(T, T)
     */
    public int compare(EnumItem enumItem1, EnumItem enumItem2) {

        Integer listIndex1 = enumItem1.getListIndex();
        Integer listIndex2 = enumItem2.getListIndex();

        if (listIndex1 == null && listIndex2 == null) {
            return 0;
        } else if (listIndex1 == null) {
            return 1;
        } else if (listIndex2 == null) {
            return -1;
        }

        return listIndex1.compareTo(listIndex2);
    }

}
