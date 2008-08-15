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
import java.io.StringWriter;
import java.util.Vector;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.shared_impl.renderkit.RendererUtils;

import de.mogwai.common.web.component.ListAwareUtils;
import de.mogwai.common.web.component.RowGroupProvider;
import de.mogwai.common.web.component.common.TableComponent;
import de.mogwai.common.web.component.layout.GridBagLayoutUtils;
import de.mogwai.common.web.component.layout.GridbagLayoutSizeDefinitionVector;
import de.mogwai.common.web.component.renderkit.html.BaseRenderer;

/**
 * Table renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:12:27 $
 */
public class TableRenderer extends BaseRenderer {

    public TableRenderer() {
        super();
    }

    protected void renderChildAndCheckEmptyness(FacesContext aContext, UIComponent aComponent) throws IOException {

        ResponseWriter theWriter = aContext.getResponseWriter();

        StringWriter theStringWriter = new StringWriter();
        ResponseWriter theBufferedWriter = theWriter.cloneWithWriter(theStringWriter);
        aContext.setResponseWriter(theBufferedWriter);

        if (aComponent != null) {
            RendererUtils.renderChild(aContext, aComponent);
        }

        aContext.setResponseWriter(theWriter);

        theStringWriter.flush();
        StringBuffer theContent = stripCommentsFromString(theStringWriter.getBuffer());
        if (theContent.length() > 0) {
            theWriter.write(theContent.toString());
        } else {
            theWriter.write("<div style=\"width:1px; height:1px;\"></div>");
        }

    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {

        TableComponent theTableComponent = (TableComponent) aComponent;

        ResponseWriter theWriter = aContext.getResponseWriter();

        boolean hasId = false;

        if (theTableComponent.hasFunctions()) {
            theWriter.startElement("table", aComponent);
            theWriter.writeAttribute("id", theTableComponent.getClientId(aContext), null);
            theWriter.writeAttribute("cellspacing", "0", null);
            theWriter.writeAttribute("cellpadding", "0", null);
            theWriter.writeAttribute("border", "0", null);
            theWriter.startElement("tr", aComponent);
            theWriter.startElement("td", aComponent);
            hasId = true;
        }

        theWriter.startElement("table", aComponent);
        if (!hasId) {
            theWriter.writeAttribute("id", theTableComponent.getClientId(aContext), null);
        }
        theWriter.writeAttribute("cellspacing", "0", null);
        theWriter.writeAttribute("cellpadding", "0", null);
        theWriter.writeAttribute("border", "0", null);
        theWriter.writeAttribute("class", "mogwaiTable", null);

        Vector<UIColumn> theColumns = ListAwareUtils.getColumns(theTableComponent);
        GridbagLayoutSizeDefinitionVector theCols = theTableComponent.getCols();

        // Render the table definition
        theWriter.startElement("colgroup", theTableComponent);
        for (int i = 0; i < theColumns.size(); i++) {

            String theWidth = GridBagLayoutUtils.getSizeForColumn(theTableComponent, theCols, i);

            theWriter.startElement("col", aComponent);
            theWriter.writeAttribute("width", theWidth, null);
            theWriter.endElement("col");

        }
        theWriter.endElement("colgroup");

    }

    protected void renderHeader(FacesContext aContext, UIComponent aComponent, Vector<UIColumn> aColumns,
            Object aGroupObject) throws IOException {

        TableComponent theTableComponent = (TableComponent) aComponent;

        if (!theTableComponent.isShowHeader()) {
            return;
        }

        GridbagLayoutSizeDefinitionVector theCols = theTableComponent.getCols();

        ResponseWriter theWriter = aContext.getResponseWriter();

        if (aGroupObject != null) {
            theWriter.startElement("tr", aComponent);

            if (theTableComponent.isUseStyles()) {
                theWriter.writeAttribute("class", "mogwaiTableGroupRow", null);
            }

            theWriter.startElement("td", aComponent);
            theWriter.writeAttribute("colspan", aColumns.size(), null);

            if (theTableComponent.isUseStyles()) {
                theWriter.writeAttribute("class", "mogwaiTableGroupCell", null);
            }

            UIComponent theGroupHeader = theTableComponent.getMogwaiFacet(TableComponent.GROUPHEADER_FACET);

            aContext.getExternalContext().getRequestMap().put(TableComponent.HEADER_VAR_ATTRIBUTE, aGroupObject);

            RendererUtils.renderChild(aContext, theGroupHeader);

            aContext.getExternalContext().getRequestMap().remove(TableComponent.HEADER_VAR_ATTRIBUTE);

            theWriter.endElement("td");
            theWriter.endElement("tr");
        }

        theWriter.startElement("tr", aComponent);

        if (theTableComponent.isUseStyles()) {
            theWriter.writeAttribute("class", "mogwaiTableHeaderRow", null);
        }

        for (int i = 0; i < aColumns.size(); i++) {
            UIComponent theColumn = aColumns.get(i);

            theWriter.startElement("th", aComponent);

            if (theTableComponent.isUseStyles()) {
                theWriter.writeAttribute("class", (i == aColumns.size() - 1) ? "mogwaiTableHeaderCellLast"
                        : "mogwaiTableHeaderCell", null);
            }
            theWriter.writeAttribute("align", theCols.get(i).getAlign(), null);

            renderChildAndCheckEmptyness(aContext, theColumn.getFacet("header"));

            theWriter.endElement("th");
        }

        theWriter.endElement("tr");

    }

    protected void renderFooter(FacesContext aContext, ResponseWriter aWriter, TableComponent aComponent,
            Vector<UIColumn> aColumns) throws IOException {

        if (!aComponent.isShowFooter()) {
            return;
        }

        GridbagLayoutSizeDefinitionVector theCols = aComponent.getCols();

        aWriter.startElement("tr", aComponent);

        if (aComponent.isUseStyles()) {
            aWriter.writeAttribute("class", "mogwaiTableFooterRow", null);
        }

        for (int i = 0; i < aColumns.size(); i++) {

            UIComponent theColumn = aColumns.get(i);

            aWriter.startElement("th", aComponent);

            if (aComponent.isUseStyles()) {
                aWriter.writeAttribute("class", (i == aColumns.size() - 1) ? "mogwaiTableFooterCellLast"
                        : "mogwaiTableFooterCell", null);
            }
            aWriter.writeAttribute("align", theCols.get(i).getAlign(), null);

            renderChildAndCheckEmptyness(aContext, theColumn.getFacet("footer"));

            aWriter.endElement("th");
        }

        aWriter.endElement("tr");

    }

    @Override
    @SuppressWarnings("all")
    public void encodeChildren(FacesContext aContext, UIComponent aComponent) throws IOException {

        TableComponent theTableComponent = (TableComponent) aComponent;

        GridbagLayoutSizeDefinitionVector theCols = theTableComponent.getCols();

        ResponseWriter theWriter = aContext.getResponseWriter();

        UIData theData = (UIData) aComponent;

        Vector<UIColumn> theColumns = ListAwareUtils.getColumns(theTableComponent);

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

        Object theCurrentGroupObject = null;

        if (theRow >= theLast) {
            // There is no data, so only render the header
            renderHeader(aContext, aComponent, theColumns, null);

            UIComponent theNoDataFacet = aComponent.getFacet(TableComponent.NODATA_FACET);
            if (theNoDataFacet != null) {
                theWriter.startElement("tr", aComponent);
                theWriter.startElement("td", aComponent);

                if (theTableComponent.isUseStyles()) {
                    theWriter.writeAttribute("class", "mogwaiNoDataCell", null);
                }

                theWriter.writeAttribute("colspan", theColumns.size(), null);

                renderChildAndCheckEmptyness(aContext, theNoDataFacet);

                theWriter.endElement("td");
                theWriter.endElement("tr");
            }
        }

        while (theRow < theLast) {

            theData.setRowIndex(theRow);

            Object theRowData = theData.getRowData();
            if ((!(theRowData instanceof RowGroupProvider)) && (theRowCounter == 0)) {
                // Object is no grouping provider, and it is the first row, so
                // add a plain header
                renderHeader(aContext, aComponent, theColumns, null);
            }

            boolean suppressData = false;

            if ((theRowData instanceof RowGroupProvider)) {

                RowGroupProvider<Object> theProvider = (RowGroupProvider<Object>) theRowData;

                Object theNewGroupObject = theProvider.getGroupingObject();

                if ((theCurrentGroupObject == null) || (!theCurrentGroupObject.equals(theNewGroupObject))) {

                    // Group has changed, render a new header and include the
                    // grouping object
                    renderHeader(aContext, aComponent, theColumns, theNewGroupObject);

                    theCurrentGroupObject = theNewGroupObject;

                    theRowCounter = 0;
                }

                suppressData = theProvider.getValue() == null;

            }

            if (!suppressData) {
                theWriter.startElement("tr", aComponent);

                for (int i = 0; i < theColumns.size(); i++) {

                    UIColumn theChild = theColumns.get(i);

                    theWriter.startElement("td", aComponent);

                    // String theWidth =
                    // GridBagLayoutUtils.getSizeForColumn(theTableComponent,
                    // theCols, i);
                    // theWriter.writeAttribute("width", theWidth, null);

                    if (theTableComponent.isUseStyles()) {
                        String theClass = (theRowCounter % 2) == 0 ? "mogwaiTableCellEven" : "mogwaiTableCellOdd";
                        if (i == theColumns.size() - 1) {
                            theClass += "Last";
                        }

                        theWriter.writeAttribute("class", theClass, null);
                    }

                    renderChildAndCheckEmptyness(aContext, theChild);

                    theWriter.endElement("td");
                }

                theWriter.endElement("tr");
            }

            theRow++;
            theRowCounter++;
        }

        if (theTableComponent.isFillupRows()) {

            // Es sollen noch Leerzeilen angehängt werden
            while (theRowCounter < theRows) {

                theWriter.startElement("tr", aComponent);

                for (int i = 0; i < theColumns.size(); i++) {

                    UIColumn theChild = theColumns.get(i);

                    theWriter.startElement("td", aComponent);

                    if (theTableComponent.isUseStyles()) {
                        String theClass = (theRowCounter % 2) == 0 ? "mogwaiTableCellEven" : "mogwaiTableCellOdd";
                        if (i == theColumns.size() - 1) {
                            theClass += "Last";
                        }

                        theWriter.writeAttribute("class", theClass, null);
                    }

                    theWriter.writeAttribute("align", theCols.get(i).getAlign(), null);

                    UIComponent theEmptyCellFacet = aComponent.getFacet(TableComponent.EMPTYCELL_FACET);
                    if (theEmptyCellFacet != null) {
                        renderChildAndCheckEmptyness(aContext, theEmptyCellFacet);
                    } else {
                        // Alt 255 Hack !!
                        theWriter.write(" ");
                    }

                    theWriter.endElement("td");
                }

                theWriter.endElement("tr");

                theRowCounter++;
            }
        }
    }

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {

        TableComponent theTableComponent = (TableComponent) aComponent;
        ResponseWriter theWriter = aContext.getResponseWriter();

        Vector<UIColumn> theColumns = ListAwareUtils.getColumns(theTableComponent);
        renderFooter(aContext, theWriter, theTableComponent, theColumns);

        theWriter.endElement("table");

        if (theTableComponent.hasFunctions()) {
            theWriter.endElement("td");
            theWriter.startElement("td", aComponent);
            theWriter.writeAttribute("align", "left", null);
            theWriter.writeAttribute("valign", "bottom", null);
            theWriter.writeAttribute("nowrap", "nowrap", null);

            RendererUtils.renderChild(aContext, theTableComponent.getFunctions());

            theWriter.endElement("td");
            theWriter.endElement("tr");
            theWriter.endElement("table");
        }

    }

}