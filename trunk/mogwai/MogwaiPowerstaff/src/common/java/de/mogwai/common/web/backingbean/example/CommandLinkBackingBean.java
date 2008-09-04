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

public class CommandLinkBackingBean extends BackingBean {

    public CommandLinkBackingBean() {
    }

    public String jumpToPage1() {
        System.out.println("Page 1");
        return null;
    }

    public String jumpToPage2() {
        System.out.println("Page 2");
        return null;
    }

    public String jumpToPage3() {
        System.out.println("Page 3");
        return null;
    }

    public String submit() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String refresh() {

        System.out.println("Page refreshed");

        return super.refresh();
    }
}
