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
package de.mogwai.common.web.component.input;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import de.mogwai.common.web.component.layout.ModalComponent;

/**
 * Komponenten - Utilities.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:17:34 $
 */
public final class ModalComponentUtils {

    private static final String IN_MODAL_DIALOG_MARKER_REQUEST_PARAMETER = "mogwai.inmodaldialogmarker";

    private ModalComponentUtils() {
    }

    public static ModalComponent getVisibleModalComponent(UIComponent aComponent) {

        if ((aComponent instanceof ModalComponent)) {
            ModalComponent theComponent = (ModalComponent) aComponent;
            if (theComponent.isReallyRendered()) {
                return theComponent;
            }
        }

        for (int i = 0; i < aComponent.getChildCount(); i++) {
            ModalComponent theComponent = getVisibleModalComponent((UIComponent) aComponent.getChildren().get(i));
            if (theComponent != null) {
                return theComponent;
            }
        }

        return null;
    }

    /**
     * Überprüfung, falls sich eine Komponente innerhalb eines modalen Dialoges
     * bedindet oder nicht.
     * 
     * Beim Rendern des Dialoges setzt dieser einen Marker als Request -
     * Parameter, um allen Kind - Komponenten mitzuteilen, dass sich diese
     * innerhalb des Dialoges befindet. Über die Komponentenhierarchie kann dies
     * nicht ermittelt werden, da das jsp:include nur subviews erzeugt, die die
     * Hierarchie verfälschen. Der sicherste Weg sind die Request - Parameter.
     * 
     * @param aComponent
     *                die aufrufende Kopmponente
     * @param aContext
     *                der aktuelle Context
     * @return true, wenn sich die Komponente in einem Modalen Dialog befindet,
     *         sonst false.
     */
    public static boolean isInModalDialog(UIComponent aComponent, FacesContext aContext) {
        return aContext.getExternalContext().getRequestMap().containsKey(IN_MODAL_DIALOG_MARKER_REQUEST_PARAMETER);
    }

    /**
     * Setzen des Markers für Modale Dialoge.
     * 
     * @param aComponent
     *                die aufrufende Kopmponente
     * @param aContext
     *                der aktuelle Context
     */
    public static void setInModalComponent(UIComponent aComponent, FacesContext aContext) {
        aContext.getExternalContext().getRequestMap().put(IN_MODAL_DIALOG_MARKER_REQUEST_PARAMETER, Boolean.TRUE);
    }

    /**
     * Entfernen des Markers für Modale Dialoge.
     * 
     * @param aComponent
     *                die aufrufende Kopmponente
     * @param aContext
     *                der aktuelle Context
     */
    public static void removeInModalComponent(UIComponent aComponent, FacesContext aContext) {
        aContext.getExternalContext().getRequestMap().remove(IN_MODAL_DIALOG_MARKER_REQUEST_PARAMETER);
    }

    /**
     * Überprüfung, ob die aktuelle Komponente nicht von einem modalen Dialog
     * überdekt wird.
     * 
     * Wärend des Renders und des Component - Lifecycles wird von der Modal -
     * Komponente ein Marker mittels der Methoden setInModalComponent und
     * removeInModalComponent gesetzt. Abhängig von diesem Marker und der
     * Struktur innerhalb des Komponentenbaumes wird entschieden, ob die
     * Komponente von einem modalen Dialog überdeckt wird, oder nicht.
     * 
     * @param aComponent
     *                die aufrufende Kopmponente
     * @param aContext
     *                der aktuelle Context
     * @return true, wenn die Komponente von einem Modalen Dialog überdeckt
     *         wird, sonst false
     */
    public static boolean isNotOverlayedByModalDialog(UIComponent aComponent, FacesContext aContext) {

        boolean notOverlayedByModalDialog = true;
        ModalComponent theModalComponent = ModalComponentUtils.getVisibleModalComponent(aContext.getViewRoot());
        if (theModalComponent != null) {
            notOverlayedByModalDialog = ModalComponentUtils.isInModalDialog(aComponent, aContext);
        }

        return notOverlayedByModalDialog;
    }

    /**
     * Ermittlung des aktuellen Wertes einer Komponente.
     * 
     * @param aInputComponent
     *                die Eingabekomponente
     * @return der aktuelle Wert
     */
    public static Object getCurrentComponentValue(UIInput aInputComponent) {

        if (aInputComponent.isValid()) {
            return aInputComponent.getValue();
        }

        Object theSubmittedValue = aInputComponent.getSubmittedValue();
        if (theSubmittedValue != null) {
            if (aInputComponent instanceof ComboboxComponent) {
                if ("-1".equals(theSubmittedValue)) {
                    return null;
                }
            }
        }

        return theSubmittedValue;
    }

    /**
     * Überprüfung, ob die Komponente nicht von einem Modalen Dialog überdeckt
     * wird.
     * 
     * @param aContext
     *                der aktuelle Kontext
     * @param aComponent
     *                die Komponente
     * @return true, wenn die Komponente nicht von einem Modalen Dialog
     *         überdeckt wird
     */
    public static boolean isInModalDialogOrOnSimplePage(FacesContext aContext, UIComponent aComponent) {

        boolean theRenderAsSelect = true;
        ModalComponent theModalComponent = getVisibleModalComponent(aContext.getViewRoot());
        if (theModalComponent != null) {
            theRenderAsSelect = isInModalDialog(aComponent, aContext);
        }

        return theRenderAsSelect;
    }

}
