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
import java.util.Locale;
import java.util.ResourceBundle;

import de.mogwai.common.business.service.TextResourceService;
import de.mogwai.common.usercontext.UserContext;
import de.mogwai.common.usercontext.UserContextHolder;

/**
 * Service Implementation für Text Ressourcen Zugriff.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:47 $
 */
public class TextResourceServiceImpl extends LogableService implements TextResourceService {

    // Per Default ist es dieser Name, kann jedoch auch mittels Dependency
    // Injection überschrieben werden.
    private String bundleBaseName = "messages";

    /**
     * Gibt den Wert des Attributs <code>bundleBaseName</code> zurück.
     * 
     * @return Wert des Attributs bundleBaseName.
     */
    public String getBundleBaseName() {
        return bundleBaseName;
    }

    /**
     * Setzt den Wert des Attributs <code>bundleBaseName</code>.
     * 
     * @param bundleBaseName
     *                Wert für das Attribut bundleBaseName.
     */
    public void setBundleBaseName(String bundleBaseName) {
        this.bundleBaseName = bundleBaseName;
    }

    /**
     * @see de.mogwai.common.business.service.TextResourceService#getText(java.lang.String,
     *      java.lang.String[])
     */
    public String getText(String aKey, Object... values) {

        Locale locale = null;
        UserContext userContext = UserContextHolder.getUserContext();
        if (userContext != null) {
            locale = userContext.getLocale();
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }

        return this.getText(locale, aKey, values);
    }

    /**
     * @see de.mogwai.common.business.service.TextResourceService#getText(java.util.Locale,
     *      java.lang.String, java.lang.Object[])
     */
    public String getText(Locale locale, String aKey, Object... values) {

        String theValue = ResourceBundle.getBundle(this.bundleBaseName, locale).getString(aKey);
        return MessageFormat.format(theValue, values);
    }

}
