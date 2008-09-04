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
package de.mogwai.common.web.model;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import de.mogwai.common.business.entity.EnumItem;
import de.mogwai.common.business.enums.BaseEnumItemEnum;
import de.mogwai.common.utils.ObjectProvider;
import de.mogwai.common.utils.StringPresentationProvider;

/**
 * Internationalisiertes JSF SelectItem.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:38:56 $
 */
@SuppressWarnings("serial")
public class EnumItemMultiLanguageSelectItem extends SelectItem implements StringPresentationProvider, ObjectProvider {

    /**
     * Instanziierung mit EnumItem, der als Value des SelectItem gesetzt wird.
     * 
     * @param enumItem
     *                EnumItem
     */
    public EnumItemMultiLanguageSelectItem(EnumItem enumItem) {
        super(enumItem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        if (this.getValue() instanceof EnumItem) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                return ((EnumItem) this.getValue()).getLabel(context.getViewRoot().getLocale());
            }
        }
        return super.getLabel();
    }

    /**
     * {@inheritDoc}
     */
    public String getStringPresentation() {
        return getLabel();
    }

    /**
     * {@inheritDoc}
     */
    public Object getProvidedObject() {
        return getValue();
    }

    /**
     * Die Equals - Methode.
     * 
     * Das Objekt kann auch mit einem BaseEnumItemEnum verglichen werden !!
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     * @param aObject
     *                das Objekt
     * @return true wenn gleich, sonst false
     */
    @Override
    public boolean equals(Object aObject) {

        if (aObject instanceof BaseEnumItemEnum) {
            return ((BaseEnumItemEnum) aObject).getId().equals(((EnumItem) getValue()).getId());
        }

        return super.equals(aObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {

        return super.hashCode();
    }
}
