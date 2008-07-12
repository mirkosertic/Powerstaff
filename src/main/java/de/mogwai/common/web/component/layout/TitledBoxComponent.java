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
package de.mogwai.common.web.component.layout;

import de.mogwai.common.utils.LabelProvider;
import de.mogwai.common.web.ResourceBundleManager;

/**
 * Titled box component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:28 $
 */
public class TitledBoxComponent extends TitledBoxComponentBase implements LabelProvider {

    public static final String TITLED_BOX_TYPE = "titledbox";

    public static final String GROUP_BOX_TYPE = "groupbox";

    public TitledBoxComponent() {
    }

    @Override
    public String getLabel() {

        String theKey = getKey();
        if (theKey != null) {
            return ResourceBundleManager.getBundle().getString(theKey);
        }

        return super.getLabel();
    }
}
