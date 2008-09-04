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
 * Result Command für die Rückgabe von Werten aus einer MessageBox. Neben dem
 * Wert kann noch ein Objekt mit zurückgegeben.
 * 
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:24:13 $
 * @param <T>
 *                Typ des Übergabewertes des Commands.
 * @param <O>
 *                Object, dass noch mitgegeben werden kann.
 */
public class MessageBoxResultObjectCommand<T, O> extends UpdateModelCommand {

    private T value;

    private O obj;

    /**
     * Konstrukter der Klasse.
     */
    public MessageBoxResultObjectCommand() {
        this(null);
    }

    /**
     * Konstrukter der Klasse.
     * 
     * @param value
     *                Wert der Meldung
     */
    public MessageBoxResultObjectCommand(T value) {
        this.value = value;
    }

    /**
     * Konstrukter der Klasse.
     * 
     * @param value
     *                Wert der Meldung
     * @param object
     *                zusätzliches Objekt, das mitgegeben werden kann
     */
    public MessageBoxResultObjectCommand(T value, O object) {
        this.value = value;
        this.obj = object;
    }

    public O getObj() {
        return obj;
    }

    public void setObj(O obj) {
        this.obj = obj;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

}
