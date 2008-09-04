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

import java.util.Vector;

import javax.faces.context.FacesContext;

import de.mogwai.common.utils.LabelProvider;
import de.mogwai.common.web.ResourceBundleManager;

/**
 * Base labeled component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:17:38 $
 */
public abstract class LabeledComponent extends BaseInputComponent implements LabeledComponentConstants, LabelProvider {

    private String label;

    private String key;

    /**
     * {@inheritDoc}
     */
    public String getLabel() {
        if (key != null) {
            return ResourceBundleManager.getBundle().getString(key);
        }

        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void restoreState(FacesContext aContext, Object aState) {

        Object[] theValues = (Object[]) aState;

        super.restoreState(aContext, theValues[0]);

        label = (String) theValues[1];
        key = (String) theValues[2];
    }

    @Override
    public Object saveState(FacesContext aContext) {

        Vector<Object> theState = new Vector<Object>();
        theState.add(super.saveState(aContext));
        theState.add(label);
        theState.add(key);

        return theState.toArray();
    }
}
