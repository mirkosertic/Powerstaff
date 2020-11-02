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
package de.mogwai.common.web.component.renderkit.html.layout;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import de.mogwai.common.web.component.layout.ScrollComponent;
import de.mogwai.common.web.component.renderkit.html.BaseRenderer;

/**
 * Renderer für eine Scrollbox.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:35:44 $
 */
public class ScrollRenderer extends BaseRenderer {

    private static final String FORM_FIELD_SCROLL_TOP = "_scrollTop";

    private static final String FORM_FIELD_SCROLL_LEFT = "_scrollLeft";

    public ScrollRenderer() {
    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {

        ScrollComponent theComponent = (ScrollComponent) aComponent;

        String theClientID = theComponent.getClientId(aContext);

        ResponseWriter theWriter = aContext.getResponseWriter();

        theWriter.startElement("input", aComponent);
        theWriter.writeAttribute("type", "hidden", null);
        theWriter.writeAttribute("name", theClientID + FORM_FIELD_SCROLL_LEFT, null);
        theWriter.writeAttribute("id", theClientID + FORM_FIELD_SCROLL_LEFT, null);
        theWriter.writeAttribute("value", "" + theComponent.getScrollLeft(), "scrollLeft");
        theWriter.endElement("input");

        theWriter.startElement("input", aComponent);
        theWriter.writeAttribute("type", "hidden", null);
        theWriter.writeAttribute("name", theClientID + FORM_FIELD_SCROLL_TOP, null);
        theWriter.writeAttribute("id", theClientID + FORM_FIELD_SCROLL_TOP, null);
        theWriter.writeAttribute("value", "" + theComponent.getScrollTop(), "scrollTop");
        theWriter.endElement("input");

        theWriter.startElement("div", aComponent);
        theWriter.writeAttribute("class", theComponent.isSaveScrollState() ? "mogwaiScrollPreserve" : "mogwaiScroll",
                null);
        theWriter.writeAttribute("id", theClientID, null);

        setWidthIfInGridBag(theWriter, theComponent, getHeightIfInGridGag(aComponent));
    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {

        ResponseWriter theWriter = aContext.getResponseWriter();
        theWriter.endElement("div");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decode(FacesContext aContext, UIComponent aComponent) {
        super.decode(aContext, aComponent);

        ScrollComponent theComponent = (ScrollComponent) aComponent;
        if (theComponent.isSaveScrollState()) {
            String theClientID = theComponent.getClientId(aContext);

            Map theParamMap = aContext.getExternalContext().getRequestParameterMap();
            String theValue = (String) theParamMap.get(theClientID + FORM_FIELD_SCROLL_LEFT);
            if ((theValue != null) && (!"".equals(theValue))) {
                theComponent.setScrollLeft(Integer.parseInt(theValue));
            }

            theValue = (String) theParamMap.get(theClientID + FORM_FIELD_SCROLL_TOP);
            if ((theValue != null) && (!"".equals(theValue))) {
                theComponent.setScrollTop(Integer.parseInt(theValue));
            }
        }

    }
}
