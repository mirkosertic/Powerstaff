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
package de.mogwai.common.web.backingbean.example;

import de.mogwai.common.web.backingbean.BackingBean;

public class TextInputBackingBean extends BackingBean {

    private String text1;

    private String text2;

    private String text3;

    private String text4;

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String submit() {
        return null;
    }

    /**
     * Gibt den Wert des Attributs <code>text4</code> zurück.
     * 
     * @return Wert des Attributs text4.
     */
    public String getText4() {
        return text4;
    }

    /**
     * Setzt den Wert des Attributs <code>text4</code>.
     * 
     * @param text4
     *                Wert für das Attribut text4.
     */
    public void setText4(String text4) {
        this.text4 = text4;
    }
}
