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
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import de.mogwai.common.web.component.layout.IncludeComponent;
import de.mogwai.common.web.component.layout.ModalComponent;
import de.mogwai.common.web.component.renderkit.html.BaseRenderer;

/**
 * Modal renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:10:46 $
 */
public class ModalRenderer extends BaseRenderer {

    public ModalRenderer() {
    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {

        ModalComponent theComponent = (ModalComponent) aComponent;
        ResponseWriter theWriter = aContext.getResponseWriter();

        theWriter.startElement("div", aComponent);
        theWriter.writeAttribute("id", aComponent.getClientId(aContext), null);

        if (theComponent.isReallyRendered()) {
            theWriter.startElement("div", aComponent);
            theWriter.writeAttribute("class", "mogwaiModalbackground", null);
            theWriter.endElement("div");

            theWriter.startElement("div", aComponent);
            theWriter.writeAttribute("class", "mogwaiModalforeground", null);
            theWriter.startElement("table", aComponent);
            theWriter.writeAttribute("width", "100%", null);
            theWriter.writeAttribute("height", "100%", null);
            theWriter.writeAttribute("border", "1", null);
            theWriter.startElement("tr", aComponent);
            theWriter.startElement("td", aComponent);
            theWriter.writeAttribute("align", "center", null);
            theWriter.writeAttribute("valign", "middle", null);

            theWriter.startElement("table", aComponent);
            theWriter.writeAttribute("border", "1", null);
            theWriter.startElement("tr", aComponent);
            theWriter.startElement("td", aComponent);
            theWriter.writeAttribute("class", "mogwaiModalcontentcell", null);

            theWriter.startElement("inline", aComponent);
        }
    }

    @Override
    public void encodeChildren(FacesContext aContext, UIComponent aComponent) throws IOException {

        ModalComponent theComponent = (ModalComponent) aComponent;
        if (theComponent.isReallyRendered()) {
            super.encodeChildren(aContext, aComponent);
        } else {
            Iterator theChildren = theComponent.getChildren().iterator();
            while (theChildren.hasNext()) {
                UIComponent theChild = (UIComponent) theChildren.next();
                if (theChild instanceof IncludeComponent) {
                    IncludeComponent theInclude = (IncludeComponent) theChild;
                    theInclude.setLastIncludedPage(null);
                    return;
                }
            }
        }
    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {

        ModalComponent theComponent = (ModalComponent) aComponent;
        ResponseWriter theWriter = aContext.getResponseWriter();

        if (theComponent.isReallyRendered()) {
            theWriter.endElement("inline");
            theWriter.endElement("td");
            theWriter.endElement("tr");
            theWriter.endElement("table");

            theWriter.endElement("td");
            theWriter.endElement("tr");
            theWriter.endElement("table");

            theWriter.endElement("div");
        }

        theWriter.endElement("div");
    }

}
