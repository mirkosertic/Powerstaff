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

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import de.mogwai.common.web.ResourceBundleManager;

public class JSFMessageUtils {

    public static void addGlobalInfoMessage(String key) {
        addMessage(FacesMessage.SEVERITY_INFO, null, key);
    }

    public static void addGlobalWarningMessage(String key) {
        addMessage(FacesMessage.SEVERITY_WARN, null, key);
    }

    public static void addGlobalErrorMessage(String key) {
        addMessage(FacesMessage.SEVERITY_ERROR, null, key);
    }

    public static void addGlobalInfoMessage(String key, String... replacementValues) {
        addMessage(FacesMessage.SEVERITY_INFO, null, key, replacementValues);
    }

    public static void addGlobalWarningMessage(String key, String... replacementValues) {
        addMessage(FacesMessage.SEVERITY_WARN, null, key, replacementValues);
    }

    public static void addGlobalErrorMessage(String key, String... replacementValues) {
        addMessage(FacesMessage.SEVERITY_WARN, null, key, replacementValues);
    }

    public static void addMessage(Severity aSeverity, String clientId, String key) {

        String text = null;

        try {
            ResourceBundle bundle = ResourceBundleManager.getBundle();
            text = bundle.getString(key);
        } catch (Exception e) {
            text = "MISSING: " + key + " :MISSING";
        }

        FacesMessage theMessage = new FacesMessage(text);
        theMessage.setSeverity(aSeverity);

        FacesContext.getCurrentInstance().addMessage(clientId, theMessage);
    }

    public static void addMessage(Severity aSeverity, String clientId, String key, String... replacementValues) {

        String text = null;

        try {
            ResourceBundle bundle = ResourceBundleManager.getBundle();
            text = bundle.getString(key);
        } catch (Exception e) {
            text = "MISSING: " + key + " :MISSING";
        }

        if ((replacementValues != null) && (replacementValues.length > 0)) {
            text = MessageFormat.format(text, (Object[]) replacementValues);
        }

        FacesMessage theMessage = new FacesMessage(text);
        theMessage.setSeverity(aSeverity);

        FacesContext.getCurrentInstance().addMessage(clientId, theMessage);
    }

}
