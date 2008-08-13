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
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.component.action.AbstractCommandComponent;
import de.mogwai.common.web.component.renderkit.html.BaseRenderer;
import de.mogwai.common.web.utils.JSFJavaScriptFactory;
import de.mogwai.common.web.utils.JSFJavaScriptUtilities;

/**
 * Base class for action renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-13 17:33:45 $
 */
public abstract class BaseCommandRenderer extends BaseRenderer {

    private static final Logger LOGGER = new Logger(BaseCommandRenderer.class);

    protected BaseCommandRenderer() {
    }

    @Override
    public void decode(FacesContext aContext, UIComponent aComponent) {

        AbstractCommandComponent theComponent = (AbstractCommandComponent) aComponent;

        JSFJavaScriptUtilities theUtilities = JSFJavaScriptFactory.getJavaScriptUtilities(aContext);

        String theGUID = theUtilities.getCurrentGUID(aContext);
        String theSource = theUtilities.getFormSubmitSource(aContext, aComponent);

        String theSearchedSource = theUtilities.getJavaScriptComponentId(aContext, theComponent);
        if (theSearchedSource.equals(theSource)) {

            if (!theUtilities.isGUIDAlreadyUsed(aContext, theGUID)) {

                theUtilities.markGUIDAsUsed(aContext, theGUID);

                LOGGER.logDebug("Invoking action method for component " + theComponent
                        + " as it was the action source : " + theSource);

                ActionEvent theActionEvent = new ActionEvent(theComponent);
                theComponent.queueEvent(theActionEvent);

            } else {

                LOGGER.logDebug("Browser refresh detected. GUID " + theGUID + " already used");

            }
        }
    }

    protected void encodeDisabledAttributes(AbstractCommandComponent aComponent, ResponseWriter aWriter)
            throws IOException {
        if (aComponent.isDisabled()) {
            aWriter.writeAttribute("disabled", "disabled", null);
        }
    }

    protected abstract String getEnabledClass();

    protected abstract String getDisabledClass();

    protected final String getDisplayClass(AbstractCommandComponent aComponent) {
        if (aComponent.isDisabled()) {
            return getDisabledClass();
        }

        return getEnabledClass();
    }
}
