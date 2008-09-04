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
import de.mogwai.common.web.component.layout.ScrollComponent;

/**
 * Beispiel - Backingbean f�r Scrollers.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:18:24 $
 */
public class ScrollExampleBackingBean extends BackingBean {

    private ScrollComponent scrollComponent;

    /**
     * Gibt den Wert des Attributs <code>scrollComponent</code> zur�ck.
     * 
     * @return Wert des Attributs scrollComponent.
     */
    public ScrollComponent getScrollComponent() {
        return scrollComponent;
    }

    /**
     * Setzt den Wert des Attributs <code>scrollComponent</code>.
     * 
     * @param scrollComponent
     *                Wert f�r das Attribut scrollComponent.
     */
    public void setScrollComponent(ScrollComponent scrollComponent) {
        this.scrollComponent = scrollComponent;
    }
}
