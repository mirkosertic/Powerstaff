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
package de.mogwai.common.command;

/**
 * Command für das Ergebnis eines Callbacks.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:23:53 $
 */
public class CallbackResultCommand extends UpdateModelCommand {

    /**
	 * 
	 */
	private static final long serialVersionUID = -9081680741360395366L;
	private Object value;

    public CallbackResultCommand(Object aValue) {
        value = aValue;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}