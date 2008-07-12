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
package de.mogwai.common.web.component.taglib.jsp.layout;

import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.component.layout.IncludeComponent;
import de.mogwai.common.web.utils.ServletResponseWrapperInclude;

/**
 * Label tag.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:09:46 $
 */
public class IncludeTag extends IncludeTagBase {

    private final static Logger LOGGER = new Logger(IncludeTag.class);

    @Override
    public int doStartTag() throws JspException {
        int theValue = super.doStartTag();

        IncludeComponent theComponent = (IncludeComponent) getComponentInstance();
        String thePath = theComponent.getPage();

        theComponent.getChildren().clear();

        if (thePath != null) {

            boolean saveState = false;
            if (!thePath.equals(theComponent.getLastIncludedPage())) {
                saveState = true;
            }

            Object theOldState = null;

            if (saveState) {
                theOldState = theComponent.processSaveState(getFacesContext());
            }

            LOGGER.logDebug("Seite wird neu geladen : " + thePath);

            JspWriter theWriter = pageContext.getOut();

            ServletRequest theRequest = pageContext.getRequest();
            ServletResponse theResponse = pageContext.getResponse();
            RequestDispatcher theDispatcher = theRequest.getRequestDispatcher(thePath);
            FacesContext facesContext = getFacesContext();
            if (null == theDispatcher) {
                throw new JspException("UIInclude component " + theComponent.getClientId(facesContext)
                        + " could't include page with path " + thePath);
            }
            try {
                ServletResponseWrapperInclude responseWrapper = new ServletResponseWrapperInclude(theResponse,
                        theWriter);
                theDispatcher.include(theRequest, responseWrapper);
                // Write buffered data, if any;
                responseWrapper.flushBuffer();
            } catch (Exception e) {
                throw new JspException(e);
            }

            if (saveState) {
                theComponent.processRestoreState(getFacesContext(), theOldState);
            }

            theComponent.setLastIncludedPage(thePath);
        }

        return theValue;
    }

}