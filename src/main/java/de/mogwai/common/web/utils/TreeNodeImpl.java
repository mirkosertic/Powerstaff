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

import org.apache.myfaces.custom.tree2.TreeModel;
import org.apache.myfaces.custom.tree2.TreeNodeBase;

/**
 * Implementierung einer TreeNode.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:09:54 $
 */
public class TreeNodeImpl extends TreeNodeBase implements Serializable {

    private static long IDCOUNTER = 1;

    private TreeNodeImpl parent;

    private Object userObject1;

    private Object userObject2;

    private Object userObject3;

    /**
     * Constructor für eine Root - Node.
     */
    public TreeNodeImpl() {
        this(null);
    }

    /**
     * Constructor.
     * 
     * Die Node wird erzeugt und automatisch der Eltern - Node als Kind
     * hibnzugefügt. Wenn es die Root - Node sein soll, muss parent null sein.
     * 
     * @param aParent
     *                die Eltern - Node
     */
    public TreeNodeImpl(TreeNodeImpl aParent) {
        synchronized (TreeNodeImpl.class) {
            setIdentifier("" + IDCOUNTER++);
        }
        parent = aParent;
        if (parent != null) {
            parent.getChildren().add(this);
        }
    }

    /**
     * Ermittlung der Eltern - Node.
     * 
     * @return die Eltern - Node
     */
    public TreeNodeImpl getParent() {
        return parent;
    }

    /**
     * Ermittlung der Root - Node.
     * 
     * @return die Root - Node
     */
    public TreeNodeImpl getRootNode() {
        if (parent != null) {
            return parent.getRootNode();
        }

        return this;
    }

    protected void computePath(StringBuffer aBuffer) {
        if (aBuffer.length() > 0) {
            aBuffer.insert(0, TreeModel.SEPARATOR);
        }

        if (parent != null) {
            aBuffer.insert(0, parent.getChildren().indexOf(this));
            parent.computePath(aBuffer);
        } else {
            aBuffer.insert(0, "0");
        }
    }

    /**
     * Ermittlung des identifizierenden Pfades der Node innerhalb des ganzen
     * Baumes.
     * 
     * @return der identifizierende Pfad
     */
    public String getIdentifyingPath() {
        StringBuffer theBuffer = new StringBuffer();
        computePath(theBuffer);

        return theBuffer.toString();
    }

    /**
     * Gibt den Wert des Attributs <code>userObject1</code> zurück.
     * 
     * @return Wert des Attributs userObject1.
     */
    public Object getUserObject1() {
        return userObject1;
    }

    /**
     * Setzt den Wert des Attributs <code>userObject1</code>.
     * 
     * @param userObject1
     *                Wert für das Attribut userObject1.
     */
    public void setUserObject1(Object userObject1) {
        this.userObject1 = userObject1;
    }

    /**
     * Gibt den Wert des Attributs <code>userObject2</code> zurück.
     * 
     * @return Wert des Attributs userObject2.
     */
    public Object getUserObject2() {
        return userObject2;
    }

    /**
     * Setzt den Wert des Attributs <code>userObject2</code>.
     * 
     * @param userObject2
     *                Wert für das Attribut userObject2.
     */
    public void setUserObject2(Object userObject2) {
        this.userObject2 = userObject2;
    }

    /**
     * Gibt den Wert des Attributs <code>userObject3</code> zurück.
     * 
     * @return Wert des Attributs userObject3.
     */
    public Object getUserObject3() {
        return userObject3;
    }

    /**
     * Setzt den Wert des Attributs <code>userObject3</code>.
     * 
     * @param userObject3
     *                Wert für das Attribut userObject3.
     */
    public void setUserObject3(Object userObject3) {
        this.userObject3 = userObject3;
    }
}