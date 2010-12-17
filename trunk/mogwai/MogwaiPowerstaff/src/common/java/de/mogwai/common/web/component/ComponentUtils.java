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
package de.mogwai.common.web.component;

import java.util.List;
import java.util.Vector;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.component.AjaxComponent;

import de.mogwai.common.web.component.layout.ModalComponent;

/**
 * Hilfsklasse mit div. Funktionen zum Umgang mit Komponenten.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-06 17:05:20 $
 */
public final class ComponentUtils {

	private ComponentUtils() {
	}

	public static void addDynamicContentComponentIDs(List<String> aList,
			UIComponent aComponent) {

		if (aComponent instanceof DynamicContentComponent) {
			aList
					.add(aComponent.getClientId(FacesContext
							.getCurrentInstance()));
		} else {

			if (aComponent instanceof AjaxComponent) {
				aList.add(aComponent.getClientId(FacesContext
						.getCurrentInstance()));
			} else {

				for (int i = 0; i < aComponent.getChildCount(); i++) {
					addDynamicContentComponentIDs(aList,
							(UIComponent) aComponent.getChildren().get(i));
				}
			}
		}
	}

	public static void addModalComponentIDs(List<String> aList,
			UIComponent aComponent) {

		if (aComponent instanceof ModalComponent) {
			ModalComponent theComponent = (ModalComponent) aComponent;
			if (theComponent.isSwitched()) {
				aList.add(theComponent.getClientId(FacesContext
						.getCurrentInstance()));
			} else {
				if (theComponent.isRendered()) {
					for (int i = 0; i < aComponent.getChildCount(); i++) {
						addModalComponentIDs(aList, (UIComponent) aComponent
								.getChildren().get(i));
					}
				}
			}

			return;
		}

		for (int i = 0; i < aComponent.getChildCount(); i++) {
			addModalComponentIDs(aList, (UIComponent) aComponent.getChildren()
					.get(i));
		}
	}

	public static List<String> getDynamicContentComponentIDs(
			UIComponent aComponent) {
		List<String> theResult = new Vector<String>();
		addDynamicContentComponentIDs(theResult, aComponent);
		return theResult;
	}
}
