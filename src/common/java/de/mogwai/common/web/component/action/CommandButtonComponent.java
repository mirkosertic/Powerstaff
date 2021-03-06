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
package de.mogwai.common.web.component.action;

import de.mogwai.common.utils.LabelProvider;
import de.mogwai.common.web.ResourceBundleManager;

/**
 * Command button component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:24:45 $
 */
public class CommandButtonComponent extends CommandButtonComponentBase
		implements LabelProvider {

	public static final String BUTTON_TYPE = "button";

	public CommandButtonComponent() {
	}

	public String getLabel() {
		String theKey = getKey();
		if (theKey != null) {
			return ResourceBundleManager.getBundle().getString(theKey);
		}

		return getValue().toString();
	}

	@Override
	public Object getValue() {
		String theKey = getKey();
		if (theKey != null) {
			return ResourceBundleManager.getBundle().getString(theKey);
		}

		return super.getValue();
	}
}