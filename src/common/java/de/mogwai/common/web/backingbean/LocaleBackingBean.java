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
package de.mogwai.common.web.backingbean;

import java.util.Iterator;
import java.util.Locale;

import javax.faces.context.FacesContext;

import de.mogwai.common.web.utils.LocaleSelectableEntry;
import de.mogwai.common.web.utils.SelectableEntryList;

/**
 * Backing Bean für die Wahl der Locale.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:17:11 $
 */
public class LocaleBackingBean extends BackingBean {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2111511042199635083L;
	private SelectableEntryList supportedLocales = new SelectableEntryList();

    public LocaleBackingBean() {

        Iterator theLocales = FacesContext.getCurrentInstance().getApplication().getSupportedLocales();
        while (theLocales.hasNext()) {
            Locale theLocale = (Locale) theLocales.next();
            supportedLocales.add(new LocaleSelectableEntry(theLocale, theLocale.equals(FacesContext
                    .getCurrentInstance().getApplication().getDefaultLocale())));
        }

    }

    /**
     * Action Methode setzt die Locale auf <code>Locale.GERMAN</code>.
     * 
     * @return logischer JSF Outcome String.
     */
    public String setLocaleToGerman() {

        FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.GERMAN);
        return ActionOutcome.SUCCESS.value();
    }

    /**
     * Action Methode setzt die Locale auf <code>Locale.FRENCH</code>.
     * 
     * @return logischer JSF Outcome String.
     */
    public String setLocaleToFrench() {

        FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.FRENCH);
        return ActionOutcome.SUCCESS.value();
    }

    /**
     * Setzen des ausgewählten Locales aus der Liste der unterstützten Locales.
     * 
     * @return SUCESS als Action - Outcome
     */
    public String setSelectedLocale() {

        LocaleSelectableEntry theEntry = (LocaleSelectableEntry) supportedLocales.getRowData();
        FacesContext.getCurrentInstance().getViewRoot().setLocale(theEntry.getLocale());

        return ActionOutcome.SUCCESS.value();
    }

    /**
     * Ermittlung der Liste der unterstützten Locales.
     * 
     * Es wird die aktuelle JSF Locale Konfiguration als Grundlage genommen
     * 
     * Die Liste besteht aus LocaleSelectableEntry Einträgen.
     * 
     * @return Die Liste der unterstützten Locales
     */
    public SelectableEntryList getSupportedLocales() {
        return supportedLocales;
    }
}
