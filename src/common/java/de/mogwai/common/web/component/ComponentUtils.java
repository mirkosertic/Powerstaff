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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.AjaxComponent;
import org.richfaces.component.UISuggestionBox;

import de.mogwai.common.web.component.input.ModalComponentUtils;
import de.mogwai.common.web.component.layout.ModalComponent;

/**
 * Hilfsklasse mit div. Funktionen zum Umgang mit Komponenten.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-06 07:43:17 $
 */
public final class ComponentUtils {

    private ComponentUtils() {
    }

    public static void debugPrint(UIComponent aComponent, int aLevel) {
        String theId = aComponent.getClientId(FacesContext.getCurrentInstance());
        StringBuffer theInset = new StringBuffer("");
        for (int i = 0; i < aLevel; i++) {
            theInset.append(" ");
        }

        System.out.println(theInset + theId + " (" + aComponent.getClass().getName() + ")");
        Iterator theFacetsAndChilds = aComponent.getFacetsAndChildren();
        while (theFacetsAndChilds.hasNext()) {
            UIComponent theChild = (UIComponent) theFacetsAndChilds.next();
            debugPrint(theChild, aLevel + 1);
        }
    }

    public static void addDynamicContentComponentIDs(List<String> aList, UIComponent aComponent) {

        if (aComponent instanceof DynamicContentComponent) {
            aList.add(aComponent.getClientId(FacesContext.getCurrentInstance()));
        } else {

            if (aComponent instanceof AjaxComponent) {
                aList.add(aComponent.getClientId(FacesContext.getCurrentInstance()));
            } else {

                for (int i = 0; i < aComponent.getChildCount(); i++) {
                    addDynamicContentComponentIDs(aList, (UIComponent) aComponent.getChildren().get(i));
                }
            }
        }
    }

    public static void addModalComponentIDs(List<String> aList, UIComponent aComponent) {

        if (aComponent instanceof ModalComponent) {
            ModalComponent theComponent = (ModalComponent) aComponent;
            if (theComponent.isSwitched()) {
                aList.add(theComponent.getClientId(FacesContext.getCurrentInstance()));
            } else {
                if (theComponent.isRendered()) {
                    for (int i = 0; i < aComponent.getChildCount(); i++) {
                        addModalComponentIDs(aList, (UIComponent) aComponent.getChildren().get(i));
                    }
                }
            }

            return;
        }

        for (int i = 0; i < aComponent.getChildCount(); i++) {
            addModalComponentIDs(aList, (UIComponent) aComponent.getChildren().get(i));
        }
    }

    public static List<String> getDynamicContentComponentIDs(UIComponent aComponent) {
        List<String> theResult = new Vector<String>();
        addDynamicContentComponentIDs(theResult, aComponent);
        return theResult;
    }

    /**
     * Emulierung des kompletten LifeCycles einer Komponente.
     * 
     * Die Komponente wird dekodiert, validiert und das Datenmodell wird
     * aktualisiert. Es kann vorkommen, dass durch ein immediate = true nicht
     * alle Komponenten aktualisiert werden. Um dies jedoch trotzdem zu tun,
     * kann mittels dieser Methode der Lifecycle einer Komponente "nachgefahren"
     * werden.
     * 
     * @param aComponent
     *                die zu bearbeitende Komponente
     */
    public static void processFullLifecycleFor(UIComponent aComponent) {

        if (aComponent == null) {
            return;
        }

        FacesContext theContext = FacesContext.getCurrentInstance();

        // Bedingt durch den modifizierten Lifecycle eines modalen Dialoges
        // muss davon ausgegangen werden, dass sich diese Komponente in einem
        // modalen Dialog befindet.
        ModalComponentUtils.setInModalComponent(aComponent, theContext);

        aComponent.decode(theContext);
        aComponent.processValidators(theContext);

        // Würde der Modal - Market nicht gesetzt sein, hätte dies keinen
        // einfluss
        aComponent.processUpdates(theContext);

        ModalComponentUtils.removeInModalComponent(aComponent, theContext);
    }

    /**
     * Emulierung des kompletten LifeCycles einer Komponente und all ihrer
     * Kinder.
     * 
     * Es werden dabei nur UIInputs berücksichtigt.
     * 
     * Die Komponente wird dekodiert, validiert und das Datenmodell wird
     * aktualisiert. Es kann vorkommen, dass durch ein immediate = true nicht
     * alle Komponenten aktualisiert werden. Um dies jedoch trotzdem zu tun,
     * kann mittels dieser Methode der Lifecycle einer Komponente "nachgefahren"
     * werden.
     * 
     * @param aComponent
     *                die zu bearbeitende Komponente
     */
    public static void processFullLifecycleForChildsOf(UIComponentBase aComponent) {

        if (aComponent instanceof UIInput) {
            processFullLifecycleFor(aComponent);
        } else {
            for (Iterator theChilds = aComponent.getChildren().iterator(); theChilds.hasNext();) {
                UIComponentBase theChild = (UIComponentBase) theChilds.next();
                processFullLifecycleForChildsOf(theChild);
            }
        }
    }
}
