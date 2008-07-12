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
package de.mogwai.common.web.component.taglib.facelets.common;

import javax.faces.component.UIComponent;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.MetaRuleset;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.jsf.ComponentConfig;

import de.mogwai.common.web.component.common.TableComponent;
import de.mogwai.common.web.component.layout.GridbagLayoutSizeDefinitionVector;
import de.mogwai.common.web.component.taglib.facelets.BaseComponentHandler;

public class TableHandler extends BaseComponentHandler {

    private final TagAttribute cols;

    public TableHandler(ComponentConfig aConfig) {
        super(aConfig);

        cols = getRequiredAttribute("cols");
    }

    @Override
    protected MetaRuleset createMetaRuleset(Class aClass) {
        MetaRuleset theResult = super.createMetaRuleset(aClass);
        theResult = theResult.ignore("cols");

        return theResult;
    }

    @Override
    protected void onComponentCreated(FaceletContext aContext, UIComponent aComponent, UIComponent aParent) {
        super.onComponentCreated(aContext, aComponent, aParent);
        TableComponent theComponent = (TableComponent) aComponent;
        theComponent.setCols(new GridbagLayoutSizeDefinitionVector(cols.getValue(aContext)));
    }
}
