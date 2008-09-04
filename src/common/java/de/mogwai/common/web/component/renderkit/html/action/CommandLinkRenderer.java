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
package de.mogwai.common.web.component.renderkit.html.action;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import de.mogwai.common.web.component.action.CommandLinkComponent;
import de.mogwai.common.web.utils.JSFJavaScriptFactory;
import de.mogwai.common.web.utils.JSFJavaScriptUtilities;

/**
 * Command button renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:29:44 $
 */
public class CommandLinkRenderer extends BaseCommandRenderer {

    private static String DISABLED_CLASS = "mogwaiLinkDisabled";

    private static String ENABLED_CLASS = "mogwaiLink";

    public CommandLinkRenderer() {
    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {

        ResponseWriter theWriter = aContext.getResponseWriter();
        CommandLinkComponent theComponent = (CommandLinkComponent) aComponent;

        String theClientID = aComponent.getClientId(aContext);

        JSFJavaScriptUtilities theUtilities = JSFJavaScriptFactory.getJavaScriptUtilities(aContext);

        theWriter.startElement("a", aComponent);
        theWriter.writeAttribute("id", theClientID, null);
        theWriter.writeAttribute("name", theClientID, null);

        String theTitle = theComponent.getTitle();
        if ((theTitle != null) && (!"".equals(theTitle))) {
            theWriter.writeAttribute("title", theTitle, null);
        }

        String theStyle = theComponent.getStyleClass();

        if ((theStyle != null) && (!"".equals(theStyle))) {
            theWriter.writeAttribute("class", theStyle, null);
        } else {
            theWriter.writeAttribute("class", getDisplayClass(theComponent), null);
        }

        theWriter.writeAttribute("href", "#", null);
        theWriter.writeAttribute("onclick", theUtilities.getJavaScriptSubmitCommand(aContext, theComponent, "onclick"),
                null);

    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {

        ResponseWriter theWriter = aContext.getResponseWriter();
        CommandLinkComponent theComponent = (CommandLinkComponent) aComponent;

        JSFJavaScriptUtilities theUtilities = JSFJavaScriptFactory.getJavaScriptUtilities(aContext);

        String theClientID = aComponent.getClientId(aContext);

        theWriter.endElement("a");

        UIForm theForm = theUtilities.findForm(aContext);
        String theFormID = theForm.getClientId(aContext);

        if (theComponent.isKeySensitive()) {

            theWriter.startElement("script", null);

            theWriter.write("document.onkeypress = mogwaiFormKeyListener;");
            theWriter.write("registerHotKey('" + theFormID + "'," + theComponent.getActionKey() + ",'" + theClientID
                    + "');");

            theWriter.endElement("script");
        }

        theUtilities.encodeSpecialFormSubmitHTML(aContext, theComponent, theWriter);
    }

    @Override
    protected String getDisabledClass() {
        return DISABLED_CLASS;
    }

    @Override
    protected String getEnabledClass() {
        return ENABLED_CLASS;
    }
}