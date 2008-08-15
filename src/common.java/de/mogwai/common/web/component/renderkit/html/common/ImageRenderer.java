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

import de.mogwai.common.utils.SourceProvider;
import de.mogwai.common.web.component.common.ImageComponent;
import de.mogwai.common.web.component.renderkit.html.BaseRenderer;

/**
 * Image renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:12:25 $
 */
public class ImageRenderer extends BaseRenderer {

    public ImageRenderer() {
    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {
        ImageComponent theComponent = (ImageComponent) aComponent;

        String theSource = theComponent.getSource();

        SourceProvider theSourceProvider = theComponent.getSourceProvider();
        if (theSourceProvider != null) {
            theSource = theSourceProvider.provideSource();
        }

        theSource = computeAbsoluteURL(aContext, theSource);

        ResponseWriter theWriter = aContext.getResponseWriter();

        theWriter.startElement("img", aComponent);
        theWriter.writeAttribute("id", theComponent.getClientId(aContext), null);
        theWriter.writeAttribute("border", "0", null);
        theWriter.writeAttribute("class", "mogwaiImage", null);
        theWriter.writeAttribute("src", theSource, null);

        Integer theWidth = theComponent.getWidth();
        if (theWidth != null) {
            theWriter.writeAttribute("width", "" + theWidth, null);
        }

        Integer theHeight = theComponent.getHeight();
        if (theHeight != null) {
            theWriter.writeAttribute("height", "" + theHeight, null);
        }

    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {

        ResponseWriter theWriter = aContext.getResponseWriter();

        theWriter.endElement("img");
    }
}
