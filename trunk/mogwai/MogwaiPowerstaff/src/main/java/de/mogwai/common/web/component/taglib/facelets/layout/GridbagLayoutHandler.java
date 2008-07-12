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
package de.mogwai.common.web.component.taglib.facelets.layout;

import javax.faces.component.UIComponent;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.MetaRuleset;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.jsf.ComponentConfig;

import de.mogwai.common.web.component.layout.GridbagLayoutComponent;
import de.mogwai.common.web.component.layout.GridbagLayoutSizeDefinitionVector;
import de.mogwai.common.web.component.taglib.facelets.BaseComponentHandler;

public class GridbagLayoutHandler extends BaseComponentHandler {

    private final TagAttribute cols;

    private final TagAttribute rows;

    public GridbagLayoutHandler(ComponentConfig aConfig) {
        super(aConfig);

        cols = getRequiredAttribute("cols");
        rows = getRequiredAttribute("rows");
    }

    @Override
    protected MetaRuleset createMetaRuleset(Class aClass) {
        MetaRuleset theRules = super.createMetaRuleset(aClass);
        theRules = theRules.ignore("rows");
        theRules = theRules.ignore("cols");
        return theRules;
    }

    @Override
    protected void onComponentCreated(FaceletContext aContext, UIComponent aComponent, UIComponent aParent) {
        super.onComponentCreated(aContext, aComponent, aParent);
        GridbagLayoutComponent theComponent = (GridbagLayoutComponent) aComponent;

        theComponent.setCols(new GridbagLayoutSizeDefinitionVector(cols.getValue(aContext)));
        theComponent.setRows(new GridbagLayoutSizeDefinitionVector(rows.getValue(aContext)));
    }
}
