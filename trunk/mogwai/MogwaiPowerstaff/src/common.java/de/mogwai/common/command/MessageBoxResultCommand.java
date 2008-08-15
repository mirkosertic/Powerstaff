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
 * Result Command f�r die R�ckgabe von Werten aus einer MessageBox.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:08:37 $
 * @param <T>
 *                Typ des �bergabewertes des Commands
 */
public class MessageBoxResultCommand<T> extends UpdateModelCommand {

    private T value;

    public MessageBoxResultCommand(T aValue) {
        value = aValue;
    }

    public MessageBoxResultCommand() {
        this(null);
    }

    public T getValue() {
        return value;
    }

}
