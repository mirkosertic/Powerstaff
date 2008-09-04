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

import java.util.Vector;

import de.mogwai.common.utils.LabelProvider;
import de.mogwai.common.web.ResourceBundleManager;

/**
 * Eintrag in einem TreeMenu.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:16:05 $
 */
public class TreeMenuItem implements SelectableEntry, LabelProvider {

    private String resourceKey;

    private boolean selected;

    private boolean expanded;

    private Vector<TreeMenuItem> childs = new Vector<TreeMenuItem>();

    private TreeMenuItem parent;

    public TreeMenuItem(String aResourceKey, boolean aSelected, boolean aExpanded) {
        resourceKey = aResourceKey;
        selected = aSelected;
        expanded = aExpanded;
    }

    public TreeMenuItem(String aResourceKey, boolean aSelected) {
        this(aResourceKey, aSelected, false);
    }

    public TreeMenuItem(String aResourceKey) {
        this(aResourceKey, false, false);
    }

    public Vector<TreeMenuItem> getChilds() {
        return childs;
    }

    public void addChild(TreeMenuItem aChild) {
        childs.add(aChild);
        aChild.setParent(this);
    }

    public void setChilds(Vector<TreeMenuItem> childs) {
        this.childs = childs;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean hasChilds() {
        return childs.size() != 0;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public TreeMenuItem getParent() {
        return parent;
    }

    public void setParent(TreeMenuItem parent) {
        this.parent = parent;
    }

    /**
     * Ermittlung der Verschachtelungstiefe des Elementes.
     * 
     * @return die Verschachtelungstiefe
     */
    public int getLevel() {
        TreeMenuItem theParent = parent;
        int aLevel = 0;
        while (theParent != null) {
            theParent = theParent.getParent();
            aLevel++;
        }

        return aLevel;
    }

    /**
     * {@inheritDoc}
     */
    public String getLabel() {
        return ResourceBundleManager.getBundle().getString(resourceKey);
    }
}
