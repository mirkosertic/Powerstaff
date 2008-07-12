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
package de.mogwai.common.web.utils;

import javax.faces.context.FacesContext;

/**
 * Interface für Objekte, die validiert werden können.
 * 
 * Die JSP - Komponenten unterstützen nur eine komponentenbasierte Validierung.
 * Über den ValidatingActionListener kann diese Validierung um eine BackingBean
 * basierte Validierung erweitert werden. Der ValidatingActionListener ruft vor
 * dem Aufruf einer Methode auf einer BackingBean die validate() Methode auf.
 * Nur, wenn das Ergebnis dieses Aufrufs true ist, wird die eigentliche Action -
 * Methode aufgerufen. Es wird somit das JSF - Framework um eine Form -
 * Validierung, wie bereits aus Struts bekannt, erweitert.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:09:51 $
 */
public interface Validatable {

    /**
     * Ausführung einer Validierung auf dem Objekt, das dieses Interface
     * implementiert.
     * 
     * @param aContext
     *                der aktuelle Faces Context
     * @return true, wenn die Verarbeitung fortgeführt werden soll, sonst false
     */
    boolean validate(FacesContext aContext);
}
