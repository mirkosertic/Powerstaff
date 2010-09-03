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
package de.mogwai.common.web.navigation;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;

import de.mogwai.common.web.ResourceBundleManager;

/**
 * Beschreibungsklasse für ein Seitenelement oder eine Seite.
 * 
 * Mit dieser Klasse werden Elemente aus einem ProcessTrain oder einem
 * MultipageNavigationDataModel beschrieben. Jede Seite ist an eine Resource
 * gebunden, hat einen Titel und einen optionalen Validator, mit dem diese Seite
 * validiert wird.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:28:55 $
 */
public class PageDescriptor implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -874855834816231207L;

	private String page;

    private String descriptionKey;

    private Validator validator;

    private boolean visible;

    private boolean enabled;

    /**
     * Konstructor.
     * 
     * @param aPage
     *                URI der Seite
     */
    public PageDescriptor(String aPage) {
        this(aPage, null);
    }

    /**
     * Konstruktur.
     * 
     * @param aPage
     *                URI der Seite
     * @param aDescriptionKey
     *                ResourceKey, welcher die Beschreibung definiert
     */
    public PageDescriptor(String aPage, String aDescriptionKey) {
        this(aPage, aDescriptionKey, true);
    }

    /**
     * Konstruktor.
     * 
     * @param aPage
     *                URI der Seite
     * @param aDescriptionKey
     *                ResourceKey, welcher die Beschreibung definiert
     * @param aVisible
     *                true, wenn sichtbar, sonst false
     */
    public PageDescriptor(String aPage, String aDescriptionKey, boolean aVisible) {
        page = aPage;
        descriptionKey = aDescriptionKey;
        visible = aVisible;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public void setDescriptionKey(String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    public String getDescription() {
        return ResourceBundleManager.getBundle().getString(descriptionKey);
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(FacesContext aContext, UIComponent aComponent, Object aValue) {
        if (validator != null) {
            validator.validate(aContext, aComponent, aValue);
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDisabled() {
        return !isEnabled();
    }
}