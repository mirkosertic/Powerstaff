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

import org.richfaces.event.DropEvent;

import de.mogwai.common.web.backingbean.BackingBean;

public class TestBackingBean extends BackingBean {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1413416428029781877L;

	private String value;

    private String value1;

    private int counter = 0;

    public void submit() {
        System.out.println("Submit " + counter++ + " " + value1);
        value = "" + System.currentTimeMillis();
    }

    public String page1() {
        System.out.println("Page 1");
        return "page1";
    }

    public String page2() {
        System.out.println("Page 2");
        return "page2";
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *                the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the value1
     */
    public String getValue1() {
        return value1;
    }

    /**
     * @param value1
     *                the value1 to set
     */
    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public void drop(DropEvent aEvent) {
        System.out.println(aEvent);
    }
}
