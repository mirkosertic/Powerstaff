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
package de.mogwai.common.web.component.taglib.jsp.common;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagUtils;

import de.mogwai.common.web.component.common.SaveStateComponent;

/**
 * @author w2k admin
 */
public class SaveStateTag extends UIComponentTag {

    private String value;

    private String name;

    @Override
    public String getComponentType() {
        return SaveStateComponent.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return null;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *                the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *                the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void setProperties(UIComponent aComponent) {
        super.setProperties(aComponent);
        UIComponentTagUtils.setValueProperty(getFacesContext(), aComponent, value);
        UIComponentTagUtils.setStringProperty(getFacesContext(), aComponent, "name", name);
    }

    @Override
    public void release() {
        super.release();
        value = null;
        name = null;
    }
}