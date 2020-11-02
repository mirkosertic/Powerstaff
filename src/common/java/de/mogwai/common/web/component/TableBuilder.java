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
package de.mogwai.common.web.component;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

/**
 * Hilfsklasse zur Generierung von Mehrspaltigen Tabellen.
 * 
 * Es ist eine Kapselung um einen Responsewriter. Sollte eine Mehrspaltige
 * Ausgabe gewünscht sein, wird automatisch eine HTML - Tabelle erzeugt.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:35:54 $
 */
public class TableBuilder {

    private final ResponseWriter responseWriter;

    private final int columns;

    private int currentColumn = 0;

    private final UIComponent component;

    public TableBuilder(ResponseWriter aResponseWriter, int aColumns, UIComponent aComponent) {
        responseWriter = aResponseWriter;
        columns = aColumns;
        component = aComponent;
    }

    public boolean isMultiColumn() {
        return columns != 1;
    }

    public void start() throws IOException {
        currentColumn = 0;
        if (isMultiColumn()) {
            responseWriter.startElement("table", component);
            responseWriter.writeAttribute("cellspacing", "0", null);
            responseWriter.writeAttribute("cellpadding", "0", null);
            responseWriter.writeAttribute("border", "0", null);
            responseWriter.startElement("tr", component);
        }
    }

    public void startCell() throws IOException {
        if (isMultiColumn()) {
            responseWriter.startElement("td", component);
        }
    }

    public void endCell(boolean aHasMore) throws IOException {
        if (isMultiColumn()) {
            responseWriter.endElement("td");
            currentColumn++;
            if (currentColumn == columns) {
                responseWriter.endElement("tr");
                if (aHasMore) {
                    responseWriter.startElement("tr", component);
                    currentColumn = 0;
                }
            }
        }
    }

    public void end() throws IOException {
        if (isMultiColumn()) {
            if (currentColumn != columns) {
                while (currentColumn < columns) {
                    responseWriter.startElement("td", component);
                    responseWriter.endElement("td");
                    currentColumn++;
                }
                responseWriter.endElement("tr");
            }
            responseWriter.endElement("table");
        }
    }

}
