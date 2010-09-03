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

import de.mogwai.common.web.backingbean.MultiPageNavigationBackingBean;
import de.mogwai.common.web.navigation.PageDescriptor;

public class MultiPageBackingBean extends MultiPageNavigationBackingBean<MultiPageDataModel> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1298129744945813846L;

	private PageDescriptor page1 = new PageDescriptor("/includes/page1.jsp");

    private PageDescriptor page2 = new PageDescriptor("/includes/page2.jsp");

    private PageDescriptor page3 = new PageDescriptor("/includes/page3.jsp");

    public MultiPageBackingBean() {
        pageModel.add(page1);
        pageModel.add(page2);
        pageModel.add(page3);
    }

    @Override
    protected MultiPageDataModel createDataModel() {
        return new MultiPageDataModel();
    }

    public String jumpToPage1() {
        jumpToPageModelIndex(0);
        return null;
    }

    public String jumpToPage2() {
        jumpToPageModelIndex(1);
        return null;
    }

    public String jumpToPage3() {
        jumpToPageModelIndex(2);
        return null;
    }
}
