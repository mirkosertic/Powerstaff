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
package de.mogwai.common.web.component.layout;

import javax.faces.component.UIComponent;

/**
 * CellInfo.
 * 
 * This class is needed by the GridbagLayout component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:29:18 $
 */
public class GridbagLayoutCellInfo {

    private int x;

    private int y;

    private int width;

    private int height;

    private String align;

    private String valign;

    private UIComponent component;

    /**
     * Check if this cell is at a given position.
     * 
     * @param col
     *                the column(x)
     * @param row
     *                the row(x)
     * @return true if the cell is at the position, else false
     */
    public boolean isAt(int col, int row) {
        return x == col && y == row;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public UIComponent getComponent() {
        return component;
    }

    public void setComponent(UIComponent component) {
        this.component = component;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getValign() {
        return valign;
    }

    public void setValign(String valign) {
        this.valign = valign;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}