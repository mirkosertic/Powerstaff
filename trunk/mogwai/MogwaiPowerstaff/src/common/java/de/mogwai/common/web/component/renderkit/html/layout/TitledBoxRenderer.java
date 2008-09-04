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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import de.mogwai.common.web.component.layout.TitledBoxComponent;
import de.mogwai.common.web.component.renderkit.html.BaseRenderer;

/**
 * Titled box renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:35:43 $
 */
public class TitledBoxRenderer extends BaseRenderer {

    public TitledBoxRenderer() {
    }

    protected void encodeBeginTitledBox(FacesContext aContext, ResponseWriter aWriter, TitledBoxComponent aComponent)
            throws IOException {

        String thePrefix = aComponent.getCssprefix();
        if (thePrefix == null) {
            thePrefix = "mogwaiTitledBox";
        }

        aWriter.startElement("table", aComponent);
        aWriter.writeAttribute("cellspacing", "0", null);
        aWriter.writeAttribute("cellpadding", "0", null);
        aWriter.writeAttribute("boder", "0", null);
        aWriter.writeAttribute("id", aComponent.getClientId(aContext), null);

        setWidthIfInGridBag(aWriter, aComponent, null);

        aWriter.startElement("tr", aComponent);
        aWriter.startElement("td", aComponent);
        aWriter.writeAttribute("class", thePrefix + "HeaderCell", null);
        aWriter.startElement("div", aComponent);
        aWriter.writeAttribute("class", thePrefix + "HeaderDiv", null);
        aWriter.write(aComponent.getLabel());
        aWriter.endElement("div");
        aWriter.endElement("td");
        aWriter.endElement("tr");
        aWriter.startElement("tr", aComponent);
        aWriter.startElement("td", aComponent);
        aWriter.writeAttribute("class", thePrefix + "ContentCell", null);
    }

    protected void encodeBeginGroupBox(ResponseWriter aWriter, TitledBoxComponent aComponent) throws IOException {

        String thePrefix = aComponent.getCssprefix();
        if (thePrefix == null) {
            thePrefix = "mogwaiGroupBox";
        }

        aWriter.startElement("span", aComponent);
        aWriter.writeAttribute("class", thePrefix + "All", null);
        aWriter.startElement("span", aComponent);
        aWriter.writeAttribute("class", thePrefix + "Titel", null);
        aWriter.write(aComponent.getLabel());
        aWriter.endElement("span");

        aWriter.startElement("span", aComponent);

        setWidthIfInGridBag(aWriter, aComponent, null);

        aWriter.writeAttribute("class", thePrefix + " " + thePrefix + "BorderBottom " + thePrefix + "BorderTop "
                + thePrefix + "BorderLeft " + thePrefix + "BorderRight", null);
    }

    protected void encodeEndTitledBox(ResponseWriter aWriter, TitledBoxComponent aComponent) throws IOException {

        aWriter.endElement("td");
        aWriter.endElement("tr");
        aWriter.endElement("table");
    }

    protected void encodeEndGroupBox(ResponseWriter aWriter, TitledBoxComponent aComponent) throws IOException {

        aWriter.endElement("span");
        aWriter.endElement("span");
    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {

        TitledBoxComponent theComponent = (TitledBoxComponent) aComponent;
        ResponseWriter theWriter = aContext.getResponseWriter();

        if (TitledBoxComponent.TITLED_BOX_TYPE.equals(theComponent.getType())) {
            encodeBeginTitledBox(aContext, theWriter, theComponent);
            return;
        }
        if (TitledBoxComponent.GROUP_BOX_TYPE.equals(theComponent.getType())) {
            encodeBeginGroupBox(theWriter, theComponent);
            return;
        }

        throw new IllegalStateException("Unknown type " + theComponent.getType());
    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {

        TitledBoxComponent theComponent = (TitledBoxComponent) aComponent;
        ResponseWriter theWriter = aContext.getResponseWriter();

        if (TitledBoxComponent.TITLED_BOX_TYPE.equals(theComponent.getType())) {
            encodeEndTitledBox(theWriter, theComponent);
            return;
        }
        if (TitledBoxComponent.GROUP_BOX_TYPE.equals(theComponent.getType())) {
            encodeEndGroupBox(theWriter, theComponent);
            return;
        }

        throw new IllegalStateException("Unknown type " + theComponent.getType());
    }

}
