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
package de.mogwai.common.web.backingbean.example;

import org.apache.myfaces.custom.tree2.HtmlTree;

import de.mogwai.common.web.backingbean.BackingBean;
import de.mogwai.common.web.utils.TreeModelImpl;
import de.mogwai.common.web.utils.TreeNodeImpl;

public class TreeBackingBean extends BackingBean {

    private TreeNodeImpl rootNode = new TreeNodeImpl();

    private TreeModelImpl treeModel = new TreeModelImpl(rootNode);

    private HtmlTree tree;

    public TreeBackingBean() {

        rootNode.setType("root");
        rootNode.setDescription("Root - " + rootNode.getIdentifier());

        for (int i = 0; i < 10; i++) {
            TreeNodeImpl theChild = new TreeNodeImpl(rootNode);
            theChild.setType("folder");
            theChild.setDescription("C - " + theChild.getIdentifier());

            for (int j = 0; j < 10; j++) {
                TreeNodeImpl theChild2 = new TreeNodeImpl(theChild);
                theChild2.setType("folder2");

                theChild2.setDescription("D - " + theChild2.getIdentifier());
            }
        }
    }

    /**
     * Gibt den Wert des Attributs <code>tree</code> zurück.
     * 
     * @return Wert des Attributs tree.
     */
    public HtmlTree getTree() {
        return tree;
    }

    /**
     * Setzt den Wert des Attributs <code>tree</code>.
     * 
     * @param tree
     *                Wert für das Attribut tree.
     */
    public void setTree(HtmlTree tree) {
        this.tree = tree;
    }

    /**
     * Gibt den Wert des Attributs <code>treeModel</code> zurück.
     * 
     * @return Wert des Attributs treeModel.
     */
    public TreeModelImpl getTreeModel() {
        return treeModel;
    }

    /**
     * Setzt den Wert des Attributs <code>treeModel</code>.
     * 
     * @param treeModel
     *                Wert für das Attribut treeModel.
     */
    public void setTreeModel(TreeModelImpl treeModel) {
        this.treeModel = treeModel;
    }

    public String select() throws Exception {

        TreeNodeImpl theNode = (TreeNodeImpl) tree.getNode();

        treeModel.setSelectedNode(theNode);

        return null;
    }

    private int counter;

    public String addNode() throws Exception {

        TreeNodeImpl theParent = treeModel.getSelectedNode();

        TreeNodeImpl theChild = new TreeNodeImpl(theParent);
        theChild.setDescription("Child" + counter++);
        theChild.setType("folder");
        treeModel.setSelectedNode(theChild);
        treeModel.expandNode(theParent);

        return null;
    }

}
