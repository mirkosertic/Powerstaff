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
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.component.action.PageRefreshComponent;
import de.mogwai.common.web.utils.JSFJavaScriptFactory;
import de.mogwai.common.web.utils.JSFJavaScriptUtilities;

/**
 * Command button renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:59 $
 */
public class PageRefreshRenderer extends BaseCommandRenderer {

    private final static Logger LOGGER = new Logger(PageRefreshRenderer.class);

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {
    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {
    }

    @Override
    public void decode(FacesContext aContext, UIComponent aComponent) {

        PageRefreshComponent theComponent = (PageRefreshComponent) aComponent;

        JSFJavaScriptUtilities theUtilities = JSFJavaScriptFactory.getJavaScriptUtilities(aContext);

        String theGUID = theUtilities.getCurrentGUID(aContext);
        if (theUtilities.isGUIDAlreadyUsed(aContext, theGUID)) {

            LOGGER.logDebug("Invoking action method for component " + theComponent + " as a page resubmit was caused");

            ActionEvent theActionEvent = new ActionEvent(theComponent);
            theComponent.queueEvent(theActionEvent);
        }
    }

    @Override
    protected String getDisabledClass() {
        return null;
    }

    @Override
    protected String getEnabledClass() {
        return null;
    }
}