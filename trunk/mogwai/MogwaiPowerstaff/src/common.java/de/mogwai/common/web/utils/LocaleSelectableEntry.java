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

import java.util.Locale;

import javax.faces.context.FacesContext;

import de.mogwai.common.utils.LabelProvider;

/**
 * Selectable Entry für Locales.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:09:16 $
 */
public class LocaleSelectableEntry implements SelectableEntry, LabelProvider {

    private boolean selected = false;

    private Locale locale;

    public LocaleSelectableEntry(Locale aLocale, boolean aSelected) {
        locale = aLocale;
        selected = aSelected;
    }

    public String getResourceKey() {
        return locale.getDisplayName();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setResourceKey(String aResourceKey) {
    }

    public void setSelected(boolean aSelected) {
        selected = aSelected;
    }

    public String getLabel() {

        Locale theLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return locale.getDisplayName(theLocale);
    }

    public String getCountry() {

        Locale theLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return locale.getDisplayCountry(theLocale);
    }

    public String getLanguage() {

        Locale theLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return locale.getDisplayLanguage(theLocale);
    }

    public Locale getLocale() {
        return locale;
    }
}