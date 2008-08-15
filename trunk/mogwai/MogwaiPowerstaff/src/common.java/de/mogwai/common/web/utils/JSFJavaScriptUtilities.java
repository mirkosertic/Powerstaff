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

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpSession;

import de.mogwai.common.web.Configuration;

/**
 * Utility - Klasse zum Umgang mit JSF und JavaScript.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:09:20 $
 */
public abstract class JSFJavaScriptUtilities {

    protected static final String JAVASCRIPT_ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";

    protected static final String FORM_COMPONENT_FAMILY = "javax.faces.Form";

    protected static final String GUID_FIELD_NAME = "MogwaiGUID";

    protected static final String GUIDS_SESSION_ATTRIBUTE_NAME = "MogwaiUsedGUIDS";

    protected JSFJavaScriptUtilities() {
    }

    /**
     * Find the nearest parent component of a given component family.
     * 
     * @param aComponent
     *                the component to start the search at
     * @param aFamily
     *                the family name
     * @return the found component or null if nothing was found
     */
    public UIComponent findComponentOfFamily(UIComponent aComponent, String aFamily) {

        if (aFamily.equals(aComponent.getFamily())) {
            return aComponent;
        }

        for (int i = 0; i < aComponent.getChildCount(); i++) {
            UIComponent theResult = findComponentOfFamily((UIComponent) aComponent.getChildren().get(i), aFamily);
            if (theResult != null) {
                return theResult;
            }
        }

        return null;
    }

    /**
     * Find the nearest parent component that is a naming container.
     * 
     * @param aComponent
     *                the component to start the search at
     * @return the found component or null if nothing was found
     */
    public UIComponent findNearestNamingContainer(UIComponent aComponent) {
        if (aComponent instanceof NamingContainer) {
            return (aComponent);
        }

        UIComponent theParent = aComponent.getParent();
        if (theParent != null) {
            return findNearestNamingContainer(theParent);
        }

        return null;
    }

    public String getJavaScriptComponentId(FacesContext aContext, UICommand aComponent) {

        UIComponent theFormComponent = findComponentOfFamily(aContext.getViewRoot(), FORM_COMPONENT_FAMILY);
        if (theFormComponent == null) {
            throw new RuntimeException("Cannot find parent form component of type javax.faces.Form for " + aComponent);
        }

        UIComponent theNamingContainer = findNearestNamingContainer(aComponent);
        if (theNamingContainer == null) {
            throw new RuntimeException("Cannot find parent naming container for " + aComponent);
        }

        return theNamingContainer.getClientId(aContext) + NamingContainer.SEPARATOR_CHAR
                + aComponent.getClientId(aContext);
    }

    /**
     * Generate the submit javascript.
     * 
     * @param aCommandComponent
     *                die Komponente
     * @param aContext
     *                der Context
     * @return die JavaScript submit command
     */
    public abstract String getJavaScriptSubmitCommand(FacesContext aContext, UICommand aCommandComponent, String aEvent);

    /**
     * Ermittlung der ID der Komponente, die das FormSubmit ausgelöst hat.
     * 
     * @param aContext
     *                der FacesContext
     * @param aComponent
     *                die Komponente
     * @return die ID der Submit - Komponente
     */
    public abstract String getFormSubmitSource(FacesContext aContext, UIComponent aComponent);

    /**
     * Kodierung von speziellen HTML oder JavaScript Code am Ende einer
     * Komponente.
     * 
     * Alle Komponenten, die eine action haben, sollten im encodeEnd() diese
     * Methode aufrufen, um abschliessend noch eventuell benötigtes HTML oder
     * JavaScript zu generieren.
     * 
     * @param aContext
     *                der FacesContext
     * @param aComponent
     *                die Komponente
     * @param aWriter
     *                der Writer
     * @exception IOException
     *                    the exception
     */
    public abstract void encodeSpecialFormSubmitHTML(FacesContext aContext, UIComponent aComponent,
            ResponseWriter aWriter) throws IOException;

    /**
     * Generierung eines eindeutigen JavaScript Funktionsnamens.
     * 
     * @param aContext
     *                der FacesContext
     * @param aPrefix
     *                das Prefix für den Namen
     * @param aComponent
     *                die Componente, dessen ID verwendet wird
     * @return der eindeutige Funktionsname
     */
    public StringBuilder generateUniqueJavaScriptFunctionNameFor(FacesContext aContext, String aPrefix,
            UIComponent aComponent) {

        String theClientID = aComponent.getClientId(aContext);

        StringBuilder theFunctionName = new StringBuilder(aPrefix);
        for (int i = 0; i < theClientID.length(); i++) {
            char theChar = theClientID.charAt(i);
            if (JAVASCRIPT_ALLOWED_CHARACTERS.indexOf(theChar) >= 0) {
                theFunctionName.append(theChar);
            }
        }

        return theFunctionName;
    }

    public UIForm findForm(FacesContext aContext) {

        UIForm theFormComponent = (UIForm) findComponentOfFamily(aContext.getViewRoot(), FORM_COMPONENT_FAMILY);
        if (theFormComponent == null) {
            throw new RuntimeException("Cannot find parent form component of type javax.faces.Form");
        }

        return theFormComponent;
    }

    protected String getGUIDFormFieldName(FacesContext aContext) {
        UIComponent theForm = findForm(aContext);
        return theForm.getClientId(aContext) + "::" + GUID_FIELD_NAME;
    }

    /**
     * Ermittlung der aktuellen GUID.
     * 
     * @param aContext
     *                der Context
     * @return die GUID oder null, wenn keine vorhanden
     */
    public String getCurrentGUID(FacesContext aContext) {
        String theFormFieldName = getGUIDFormFieldName(aContext);
        String theGUID = (String) aContext.getExternalContext().getRequestParameterMap().get(theFormFieldName);

        return theGUID;
    }

    protected List<String> getUsedGUID(FacesContext aContext) {
        HttpSession theSession = (HttpSession) aContext.getExternalContext().getSession(false);
        List<String> theGUIS = (List<String>) theSession.getAttribute(GUIDS_SESSION_ATTRIBUTE_NAME);
        if (theGUIS == null) {
            theGUIS = new Vector<String>();
            theSession.setAttribute(GUIDS_SESSION_ATTRIBUTE_NAME, theGUIS);
        }

        return theGUIS;
    }

    /**
     * Überprüfung, ob eine GUID bereits genutzt wurde.
     * 
     * @param aContext
     *                der Context
     * @param aGUID
     *                die GUID
     * @return true, wenn bereits genutzt, sonst false
     */
    public boolean isGUIDAlreadyUsed(FacesContext aContext, String aGUID) {

        if (Configuration.isGUIDCheckDisabled()) {
            return false;
        }

        if ((aGUID == null) || ("".equals(aGUID))) {
            return false;
        }

        return getUsedGUID(aContext).contains(aGUID);
    }

    /**
     * Markiert eine GUID als bereits benutzt.
     * 
     * @param aContext
     *                der Context
     * @param aGUID
     *                die GUID
     */
    public void markGUIDAsUsed(FacesContext aContext, String aGUID) {
        if ((aGUID == null) || ("".equals(aGUID))) {
            return;
        }

        List<String> theGUID = getUsedGUID(aContext);
        if (!theGUID.contains(aGUID)) {
            theGUID.add(aGUID);

            if (theGUID.size() > 100) {
                theGUID.remove(0);
            }
        }
    }

    /**
     * Erzeugung der GUID - Komponente.
     * 
     * @param aContext
     *                der Kontext
     * @param aWriter
     *                der Writer
     * @param aComponent
     *                die Komponente
     * @throws IOException
     *                 wird im Falle eines Fehlers geworfen
     */
    protected void renderHiddenGUIDField(FacesContext aContext, ResponseWriter aWriter, UIComponent aComponent)
            throws IOException {

        aWriter.startElement("input", aComponent);
        aWriter.writeAttribute("name", getGUIDFormFieldName(aContext), null);
        aWriter.writeAttribute("type", "hidden", null);
        aWriter.writeAttribute("value", UUID.randomUUID().toString(), null);
        aWriter.endElement("input");
    }
}
