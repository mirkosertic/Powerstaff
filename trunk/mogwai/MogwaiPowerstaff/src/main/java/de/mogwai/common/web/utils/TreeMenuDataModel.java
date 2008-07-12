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
package de.mogwai.common.web.utils;

import java.io.Serializable;

import javax.faces.model.DataModel;

/**
 * Ein hierarisches Datenmodell.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:09:50 $
 */
public class TreeMenuDataModel extends DataModel implements Serializable {

    private TreeMenuItem rootItem;

    private int rowIndex = -1;

    private TreeMenuItem row;

    protected int getRowCount(TreeMenuItem aRootItem, int aCounter) {

        for (TreeMenuItem theItem : aRootItem.getChilds()) {
            aCounter++;

            if (theItem.isExpanded()) {
                aCounter += getRowCount(theItem, 0);
            }
        }

        return aCounter;
    }

    protected TreeMenuItem findRow(TreeMenuItem aRootItem, int aCounter) {

        for (TreeMenuItem theItem : aRootItem.getChilds()) {
            aCounter--;

            if (aCounter < 0) {
                return theItem;
            }

            if (theItem.isExpanded()) {
                TreeMenuItem theRow = findRow(theItem, aCounter);
                if (theRow != null) {
                    return theRow;
                }

                aCounter -= getRowCount(theItem, 0);
            }
        }

        return null;

    }

    public TreeMenuDataModel(TreeMenuItem aRootItem) {
        rootItem = aRootItem;
        rootItem.setExpanded(true);
    }

    @Override
    public int getRowCount() {
        return getRowCount(rootItem, 0); // RootItem ist Row 0!
    }

    @Override
    public Object getRowData() {
        return row;
    }

    @Override
    public int getRowIndex() {
        return rowIndex;
    }

    @Override
    public Object getWrappedData() {
        return rootItem;
    }

    @Override
    public boolean isRowAvailable() {
        return rowIndex >= 0;
    }

    @Override
    public void setRowIndex(int aRowIndex) {

        rowIndex = aRowIndex;

        if (aRowIndex < 0) {
            row = null;
        } else {
            row = findRow(rootItem, rowIndex);
        }
    }

    @Override
    public void setWrappedData(Object aData) {
        rootItem = (TreeMenuItem) aData;
    }

    protected void select(TreeMenuItem aRoot, TreeMenuItem aItem) {
        aRoot.setSelected(aRoot.equals(aItem));
        for (TreeMenuItem theItem : aRoot.getChilds()) {
            select(theItem, aItem);
        }
    }

    /**
     * Selektieren eines Eintrages im Datenmodell.
     * 
     * @param aItem
     *                der zu selektierende Eintrag
     */
    public void select(TreeMenuItem aItem) {
        select(rootItem, aItem);
    }
}
