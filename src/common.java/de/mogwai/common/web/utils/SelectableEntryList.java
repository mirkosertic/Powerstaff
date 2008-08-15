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

import java.util.Vector;

/**
 * Eine Liste von Selektierbaren Einträgen.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:09:13 $
 */
@SuppressWarnings("serial")
public class SelectableEntryList extends CollectionDataModel<SelectableEntry> {

    public SelectableEntryList() {
        super(new Vector<SelectableEntry>());
    }

    /**
     * Selektieren eines bestimmten Eintrages.
     * 
     * @param aIndex
     *                der Index des Eintrages
     */
    @SuppressWarnings("unchecked")
    public void select(int aIndex) {
        Vector<SelectableEntry> theEntries = (Vector<SelectableEntry>) getWrappedData();
        for (int i = 0; i < theEntries.size(); i++) {
            SelectableEntry theEntry = theEntries.get(i);
            theEntry.setSelected(i == aIndex);
        }
    }

    /**
     * Selektieren eines bestimmten Eintrages.
     * 
     * @param aEntry
     *                der Eintrag
     */
    @SuppressWarnings("unchecked")
    public void select(SelectableEntry aEntry) {
        Vector<SelectableEntry> theEntries = (Vector<SelectableEntry>) getWrappedData();
        for (int i = 0; i < theEntries.size(); i++) {
            SelectableEntry theEntry = theEntries.get(i);
            theEntry.setSelected(theEntry.equals(aEntry));
        }
    }
}
