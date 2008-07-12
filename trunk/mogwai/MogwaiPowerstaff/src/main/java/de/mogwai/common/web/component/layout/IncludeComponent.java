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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Include component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:27 $
 */
public class IncludeComponent extends IncludeComponentBase implements NamingContainer {

    private String lastIncludedPage;

    public IncludeComponent() {
    }

    @Override
    public Object processSaveState(FacesContext aContext) {
        if (isTransient()) {
            return null;
        }
        Map facetMap = null;
        for (Iterator it = getFacets().entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            if (facetMap == null) {
                facetMap = new HashMap();
            }
            UIComponent component = (UIComponent) entry.getValue();
            if (!component.isTransient()) {
                facetMap.put(entry.getKey(), component.processSaveState(aContext));
            }
        }
        List childrenList = null;
        if ((getPage() != null)) {

            if ((getChildCount() > 0) && (getPage().equals(getLastIncludedPage()))) {
                for (Iterator it = getChildren().iterator(); it.hasNext();) {
                    UIComponent child = (UIComponent) it.next();
                    if (childrenList == null) {
                        childrenList = new ArrayList(getChildCount());
                    }
                    Object childState = child.processSaveState(aContext);
                    if (childState != null) {
                        childrenList.add(childState);
                    }
                }
            }
        }
        return new Object[] { saveState(aContext), facetMap, childrenList };
    }

    @Override
    public void processRestoreState(FacesContext aContext, Object aState) {
        Object myState = ((Object[]) aState)[0];
        Map facetMap = (Map) ((Object[]) aState)[1];
        List childrenList = (List) ((Object[]) aState)[2];
        if (facetMap != null) {
            for (Iterator it = getFacets().entrySet().iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                Object facetState = facetMap.get(entry.getKey());
                if (facetState != null) {
                    UIComponent component = (UIComponent) entry.getValue();
                    component.processRestoreState(aContext, facetState);
                } else {
                    aContext.getExternalContext().log("No state found to restore facet " + entry.getKey());
                }
            }
        }

        if ((getPage() != null)) {

            if (childrenList != null && getChildCount() > 0 && getPage().equals(getLastIncludedPage())) {
                int idx = 0;
                for (Iterator it = getChildren().iterator(); it.hasNext();) {
                    UIComponent child = (UIComponent) it.next();
                    if (!child.isTransient()) {
                        Object childState = childrenList.get(idx++);
                        if (childState != null) {
                            child.processRestoreState(aContext, childState);
                        } else {
                            aContext.getExternalContext().log("No state found to restore child of component " + getId());
                        }
                    }
                }
            }
        }
        restoreState(aContext, myState);
    }

    @Override
    public void restoreState(FacesContext aContext, Object aState) {

        Object[] theState = (Object[]) aState;
        super.restoreState(aContext, theState[0]);

        lastIncludedPage = (String) theState[1];
    }

    @Override
    public Object saveState(FacesContext aContext) {

        Vector<Object> theState = new Vector<Object>();
        theState.add(super.saveState(aContext));
        theState.add(lastIncludedPage);
        return theState.toArray();
    }

    @Override
    public void processDecodes(FacesContext aContext) {

        Object theValue = getValue();

        if (theValue != null) {

            aContext.getExternalContext().getRequestMap().put(getVar(), theValue);

            super.processDecodes(aContext);

            aContext.getExternalContext().getRequestMap().remove(getVar());

        } else {

            super.processDecodes(aContext);
        }
    }

    /**
     * Gibt den Wert des Attributs <code>lastIncludedPage</code> zurück.
     * 
     * @return Wert des Attributs lastIncludedPage.
     */
    public String getLastIncludedPage() {
        return lastIncludedPage;
    }

    /**
     * Setzt den Wert des Attributs <code>lastIncludedPage</code>.
     * 
     * @param lastIncludedPage
     *                Wert für das Attribut lastIncludedPage.
     */
    public void setLastIncludedPage(String lastIncludedPage) {
        this.lastIncludedPage = lastIncludedPage;
    }
}