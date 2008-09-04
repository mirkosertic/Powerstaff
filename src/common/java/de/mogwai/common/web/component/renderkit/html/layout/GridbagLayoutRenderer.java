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
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.shared_impl.renderkit.RendererUtils;

import de.mogwai.common.web.component.layout.GridBagLayoutUtils;
import de.mogwai.common.web.component.layout.GridbagLayoutCellComponent;
import de.mogwai.common.web.component.layout.GridbagLayoutCellInfo;
import de.mogwai.common.web.component.layout.GridbagLayoutCellInfoVector;
import de.mogwai.common.web.component.layout.GridbagLayoutComponent;
import de.mogwai.common.web.component.layout.GridbagLayoutSizeDefinition;
import de.mogwai.common.web.component.layout.GridbagLayoutSizeDefinitionVector;
import de.mogwai.common.web.component.renderkit.html.BaseRenderer;

/**
 * Renderer for the GridbagLayout component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:35:50 $
 */
public class GridbagLayoutRenderer extends BaseRenderer {

    public GridbagLayoutRenderer() {
        super();
    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {

        GridbagLayoutComponent theComponent = (GridbagLayoutComponent) aComponent;
        ResponseWriter theWriter = aContext.getResponseWriter();

        theWriter.startElement("table", aComponent);

        int theBorder = theComponent.getBorder();
        theWriter.writeAttribute("border", theBorder, null);

        theWriter.writeAttribute("cellspacing", "0", null);
        theWriter.writeAttribute("cellpadding", "0", null);
        theWriter.writeAttribute("class", "mogwaiGridBagLayout", null);
    }

    private GridbagLayoutCellInfoVector generateCellInfo(UIComponent aComponent) {
        GridbagLayoutCellInfoVector theResult = new GridbagLayoutCellInfoVector();

        List theChildren = aComponent.getChildren();
        for (int i = 0; i < theChildren.size(); i++) {
            UIComponent theChild = (UIComponent) theChildren.get(i);
            if ((theChild instanceof GridbagLayoutCellComponent) && (theChild.isRendered())) {

                GridbagLayoutCellComponent theComponent = (GridbagLayoutCellComponent) theChild;
                GridbagLayoutCellInfo theInfo = new GridbagLayoutCellInfo();
                theInfo.setX(theComponent.getX());
                theInfo.setY(theComponent.getY());
                theInfo.setWidth(theComponent.getWidth());
                theInfo.setHeight(theComponent.getHeight());
                theInfo.setAlign(theComponent.getAlign());
                theInfo.setValign(theComponent.getValign());
                theInfo.setComponent(theChild);

                theResult.add(theInfo);

            }
        }

        return theResult;
    }

    @Override
    public void encodeChildren(FacesContext aContext, UIComponent aComponent) throws IOException {

        GridbagLayoutComponent theComponent = (GridbagLayoutComponent) aComponent;

        GridbagLayoutCellInfoVector theCellInfos = generateCellInfo(aComponent);

        ResponseWriter theWriter = aContext.getResponseWriter();

        GridbagLayoutSizeDefinitionVector theRows = theComponent.getRows();
        GridbagLayoutSizeDefinitionVector theCols = theComponent.getCols();

        // Spaltenbreite definieren
        theWriter.startElement("colgroup", theComponent);

        for (int x = 0; x < theCols.size(); x++) {

            String theWidth = GridBagLayoutUtils.getSizeForColumn(theComponent, theCols, x);

            theWriter.startElement("col", aComponent);
            theWriter.writeAttribute("width", theWidth, null);
            theWriter.endElement("col");
        }

        theWriter.endElement("colgroup");

        for (int y = 0; y < theRows.size(); y++) {

            GridbagLayoutSizeDefinition theRow = theRows.get(y);

            theWriter.startElement("tr", aComponent);
            theWriter.writeAttribute("height", theRow.getSize(), null);

            for (int x = 0; x < theCols.size(); x++) {

                GridbagLayoutSizeDefinition theColDef = theCols.get(x);

                GridbagLayoutCellInfo theCell = theCellInfos.findCellInfo(x + 1, y + 1);
                if (theCell == null) {

                    theWriter.startElement("td", aComponent);
                    theWriter.writeAttribute("class", "mogwaiGridBagLayoutCell", null);
                    theWriter.endElement("td");

                } else {

                    if (theCell.isAt(x + 1, y + 1)) {

                        theWriter.startElement("td", aComponent);
                        theWriter.writeAttribute("class", "mogwaiGridBagLayoutCell", null);

                        if (theCell.getWidth() > 1) {
                            theWriter.writeAttribute("colspan", theCell.getWidth(), null);
                        }

                        String theAlign = "";
                        String theVerticalAlign = theCell.getValign();

                        if (theColDef.getAlign() != null) {
                            theWriter.writeAttribute("align", theColDef.getAlign(), null);
                            theAlign = theColDef.getAlign();
                        } else {
                            theAlign = theCell.getAlign();
                            theWriter.writeAttribute("align", theAlign, null);
                        }

                        theWriter.writeAttribute("style", "text-align:" + theAlign + ";vertical-align:"
                                + theVerticalAlign + ";", null);
                        theWriter.writeAttribute("valign", theVerticalAlign, null);

                        if (theCell.getHeight() > 1) {
                            theWriter.writeAttribute("rowspan", theCell.getHeight(), null);
                        }

                        RendererUtils.renderChild(aContext, theCell.getComponent());

                        theWriter.endElement("td");

                    }

                }

            }

            theWriter.endElement("tr");
        }
    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {

        ResponseWriter theWriter = aContext.getResponseWriter();

        theWriter.endElement("table");
    }
}