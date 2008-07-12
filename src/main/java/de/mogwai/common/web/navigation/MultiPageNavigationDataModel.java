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
package de.mogwai.common.web.navigation;

import java.io.Serializable;
import java.util.Vector;

import javax.faces.model.ListDataModel;

/**
 * Data model for wizard dialog flow.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:21 $
 */
public class MultiPageNavigationDataModel extends ListDataModel implements Serializable {

    private int index;

    private PageDescriptor overrideDescriptor;

    private NavigationType navigationType = NavigationType.ONEBYONE;

    public MultiPageNavigationDataModel() {
        super(new Vector<PageDescriptor>());
    }

    public void add(PageDescriptor aDescriptor) {
        getPages().add(aDescriptor);
    }

    @SuppressWarnings("unchecked")
    public Vector<PageDescriptor> getPages() {
        return (Vector<PageDescriptor>) getWrappedData();
    }

    public void setPages(Vector<PageDescriptor> pages) {
        setWrappedData(pages);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        if (overrideDescriptor != null) {
            overrideDescriptor = null;
        }
        this.index = index;
    }

    public PageDescriptor getOverrideDescriptor() {
        return overrideDescriptor;
    }

    public void setOverrideDescriptor(PageDescriptor overrideDescriptor) {
        this.overrideDescriptor = overrideDescriptor;
    }

    public PageDescriptor getCurrentPage() {
        if (overrideDescriptor != null) {
            return overrideDescriptor;
        }
        return getPages().get(index);
    }

    public NavigationType getNavigationType() {
        return navigationType;
    }

    public void setNavigationType(NavigationType navigationType) {
        this.navigationType = navigationType;
    }

    public boolean isActive(PageDescriptor aDescriptor) {

        int theRequestedIndex = getPages().indexOf(aDescriptor);

        return index == theRequestedIndex;
    }

    public boolean isEnabled(PageDescriptor aDescriptor) {

        if (navigationType.equals(NavigationType.FREE)) {
            return true;
        }

        int theRangeStart = index - 1;
        int theRangeEnd = index + 1;

        int theRequestedIndex = getPages().indexOf(aDescriptor);

        return ((theRequestedIndex >= theRangeStart) && (theRequestedIndex <= theRangeEnd));
    }
}