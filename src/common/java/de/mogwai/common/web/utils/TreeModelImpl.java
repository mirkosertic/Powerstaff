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

import org.apache.myfaces.custom.tree2.TreeModelBase;

/**
 * Spezialisierung des Standard - TreeModel mit Convenience - Methoden.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:15:55 $
 */
public class TreeModelImpl extends TreeModelBase implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6320021616832449890L;
	private TreeNodeImpl selectedNode;

    /**
     * Constructor.
     * 
     * @param aRootNode
     *                die Wurzel - Node
     */
    public TreeModelImpl(TreeNodeImpl aRootNode) {
        super(aRootNode);
    }

    /**
     * Selekieren einer Node.
     * 
     * @param aNode
     *                die zu selektierende Node
     */
    public void setSelectedNode(TreeNodeImpl aNode) {
        if (aNode != null) {
            getTreeState().setSelected(aNode.getIdentifyingPath());
        } else {
            getTreeState().setSelected(null);
        }
        selectedNode = aNode;
    }

    /**
     * Löschen der aktuell ausgewählten Node.
     */
    public void clearSelection() {
        getTreeState().setSelected(null);
        selectedNode = null;
    }

    /**
     * Ermittlung der selektierten Node.
     * 
     * @return die selektierte Node
     */
    public TreeNodeImpl getSelectedNode() {
        return selectedNode;
    }

    /**
     * Expandieren einer Node.
     * 
     * @param aNode
     *                die Node
     */
    public void expandNode(TreeNodeImpl aNode) {
        TreeNodeImpl theParent = aNode;
        while (theParent != null) {
            getTreeState().expandPath(new String[] { aNode.getIdentifyingPath() });
            theParent = theParent.getParent();
        }
    }

    /**
     * Collabieren einer Node.
     * 
     * @param aNode
     *                die Node
     */
    public void collapseNode(TreeNodeImpl aNode) {
        getTreeState().collapsePath(new String[] { aNode.getIdentifyingPath() });
    }
}
