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
package de.mogwai.common.web.utils.adf;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import de.mogwai.common.web.utils.JSFJavaScriptUtilities;

/**
 * Implementierung der JSF JavaScriptUtilities für ADF.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:39:07 $
 */
public class ADFJSFJavaScriptUtilities extends JSFJavaScriptUtilities {

    private static final String FIRST_LINK_ON_PAGE = "FIRST_LINK_ON_PAGE";

    private static ADFJSFJavaScriptUtilities me;

    public static ADFJSFJavaScriptUtilities getInstance() {
        if (me == null) {
            me = new ADFJSFJavaScriptUtilities();
        }
        return me;
    }

    @Override
    public String getJavaScriptSubmitCommand(FacesContext aContext, UICommand aCommandComponent, String aEvent) {

        UIComponent theFormComponent = findForm(aContext);

        // This is the oracle way to to it!
        return "return submitFormADF('" + theFormComponent.getClientId(aContext) + "','"
                + getJavaScriptComponentId(aContext, aCommandComponent) + "')";
    }

    @Override
    public String getFormSubmitSource(FacesContext aContext, UIComponent aComponent) {

        return (String) aContext.getExternalContext().getRequestParameterMap().get("source");
    }

    @Override
    public void encodeSpecialFormSubmitHTML(FacesContext aContext, UIComponent aComponent, ResponseWriter aWriter)
            throws IOException {

        Map map = aContext.getExternalContext().getRequestMap();
        Boolean firstLink = (Boolean) map.get(FIRST_LINK_ON_PAGE);

        if (firstLink == null || firstLink.equals(Boolean.TRUE)) {
            map.put(FIRST_LINK_ON_PAGE, Boolean.FALSE);

            renderHiddenGUIDField(aContext, aWriter, aComponent);
        }

    }
}
