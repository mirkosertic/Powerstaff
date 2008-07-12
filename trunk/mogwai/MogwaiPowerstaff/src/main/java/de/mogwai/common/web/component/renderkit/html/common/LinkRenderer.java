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
package de.mogwai.common.web.component.renderkit.html.common;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import de.mogwai.common.web.component.common.LinkComponent;
import de.mogwai.common.web.component.renderkit.html.BaseRenderer;

/**
 * Link renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:48 $
 */
public class LinkRenderer extends BaseRenderer {

    public LinkRenderer() {
    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {
    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {

        LinkComponent theComponent = (LinkComponent) aComponent;

        ResponseWriter theWriter = aContext.getResponseWriter();

        theWriter.startElement("link", aComponent);
        theWriter.writeAttribute("rel", theComponent.getRel(), null);
        theWriter.writeAttribute("type", theComponent.getType(), null);
        theWriter.writeAttribute("href", computeAbsoluteURL(aContext, theComponent.getHref()), null);
        theWriter.endElement("link");
    }
}
