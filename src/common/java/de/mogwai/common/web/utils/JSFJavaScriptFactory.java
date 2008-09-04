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

import javax.faces.context.FacesContext;

import de.mogwai.common.web.utils.adf.ADFJSFJavaScriptUtilities;
import de.mogwai.common.web.utils.myfaces.MyFacesJSFJavaScriptUtilities;

/**
 * Factory - Klasse für die erstellung der Konkreten JavaScript - Bib.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:16:22 $
 */
public final class JSFJavaScriptFactory {

    private static final String HTML_BASIC_RENDER_KIT = "HTML_BASIC";

    private static final String ADF_RENDER_KIT = "oracle.adf.core";

    private JSFJavaScriptFactory() {
    }

    /**
     * Ermittlung der aktuellen JSF JavaScript Utilities.
     * 
     * Abhängig vom aktuellen RenderKit wird eine andere Utility - Instanz
     * zurückgegeben.
     * 
     * @param aContext
     *                der Context
     * @return die Utility - Instanz für das aktuelle RenderKit
     */
    public static JSFJavaScriptUtilities getJavaScriptUtilities(FacesContext aContext) {

        String theRenderKit = aContext.getApplication().getDefaultRenderKitId();
        if (HTML_BASIC_RENDER_KIT.equals(theRenderKit)) {
            return MyFacesJSFJavaScriptUtilities.getInstance();
        }

        if (ADF_RENDER_KIT.equals(theRenderKit)) {
            return ADFJSFJavaScriptUtilities.getInstance();
        }

        throw new RuntimeException("Unsupported render kit id: " + theRenderKit);
    }

}