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

import de.mogwai.common.business.entity.EnumItemCategory;
import de.mogwai.common.utils.ObjectProvider;
import de.mogwai.common.utils.StringPresentationProvider;

/**
 * Internationalisiertes JSF SelectItemCategory.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:12:00 $
 */
@SuppressWarnings("serial")
public class EnumItemCategoryMultiLanguageSelectItem extends SelectItem implements StringPresentationProvider,
        ObjectProvider {

    /**
     * Instanziierung mit EnumItemCategory, der als Value des SelectItem gesetzt
     * wird.
     * 
     * @param enumItemCategory
     *                EnumItemCategory
     */
    public EnumItemCategoryMultiLanguageSelectItem(EnumItemCategory enumItemCategory) {
        super(enumItemCategory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        if (this.getValue() instanceof EnumItemCategory) {
            FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                return ((EnumItemCategory) this.getValue()).getLabel(context.getViewRoot().getLocale());
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
}
