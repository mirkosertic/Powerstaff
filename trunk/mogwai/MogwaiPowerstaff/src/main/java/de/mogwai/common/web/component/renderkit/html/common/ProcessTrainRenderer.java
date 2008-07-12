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
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.shared_impl.renderkit.RendererUtils;

import de.mogwai.common.web.component.common.ProcessTrainComponent;
import de.mogwai.common.web.component.renderkit.html.BaseRenderer;

/**
 * Processtrain renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:48 $
 */
public class ProcessTrainRenderer extends BaseRenderer {

    public ProcessTrainRenderer() {
    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {

        ResponseWriter theWriter = aContext.getResponseWriter();

        theWriter.startElement("table", aComponent);
        theWriter.writeAttribute("cellspacing", "0", null);
        theWriter.writeAttribute("cellpadding", "0", null);
        theWriter.writeAttribute("border", "0", null);
        theWriter.writeAttribute("class", "mogwaiProcessTrainTable", null);
        theWriter.startElement("tr", aComponent);
    }

    @Override
    public void encodeChildren(FacesContext aContext, UIComponent aComponent) throws IOException {

        ProcessTrainComponent theTrainComponent = (ProcessTrainComponent) aComponent;

        ResponseWriter theWriter = aContext.getResponseWriter();

        UIData theData = (UIData) aComponent;

        int theFirstRow = theData.getFirst();
        int theRows = theData.getRows();
        int theLast;

        if (theRows <= 0) {
            theLast = theData.getRowCount();
        } else {

            theLast = theFirstRow + theRows;
            if (theLast > theData.getRowCount()) {
                theLast = theData.getRowCount();
            }
        }

        int theRow = theFirstRow;
        int theRowCounter = 0;

        while (theRow < theLast) {

            theData.setRowIndex(theRow);

            theWriter.startElement("td", aComponent);

            RendererUtils.renderChild(aContext, theTrainComponent
                    .getMogwaiFacet(ProcessTrainComponent.TRAIN_NODE_FACET));

            theWriter.endElement("td");

            theRow++;
            theRowCounter++;
        }
    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {

        ResponseWriter theWriter = aContext.getResponseWriter();

        theWriter.endElement("tr");
        theWriter.endElement("table");
    }

}