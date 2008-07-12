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

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Comparator für sprachspezifische lexikografische Sortierung von EnumItems.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:38 $
 */
public class EnumItemLabelComparator implements Comparator<EnumItem> {

    private Locale locale;

    /**
     * Konstruktor.
     * 
     * @param locale
     *                Locale mit Sprache die bei der Sortierung berücksichtigt
     *                wird.
     */
    public EnumItemLabelComparator(Locale locale) {
        this.locale = locale;
    }

    /**
     * @see java.util.Comparator#compare(T, T)
     */
    public int compare(EnumItem enumItem1, EnumItem enumItem2) {

        String label1 = enumItem1.getLabel(this.locale);
        String label2 = enumItem2.getLabel(this.locale);

        Collator collator = Collator.getInstance(this.locale);
        collator.setStrength(Collator.PRIMARY);
        return collator.compare(label1, label2);
    }

}
