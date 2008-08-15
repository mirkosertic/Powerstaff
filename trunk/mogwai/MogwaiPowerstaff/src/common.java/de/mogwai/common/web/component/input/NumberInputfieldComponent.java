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
package de.mogwai.common.web.component.input;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.faces.context.FacesContext;

import de.mogwai.common.web.ResourceBundleManager;

/**
 * Input field for numbers.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:09:45 $
 */
public class NumberInputfieldComponent extends NumberInputfieldComponentBase {

    public NumberInputfieldComponent() {
    }

    /**
     * Erstellung eines NumberFormat - Objektes, mit dem eine formatierte Ein -
     * und Ausgabe anhand des aktuellen numberFormatResourceKey erzeugt wird.
     * Ist kein numberFormatResourceKey verf�gbar, gibt die Methode null zur�ck.
     * 
     * @return das NumberFormat
     */
    public NumberFormat createNumberFormat() {

        String theFormat = getNumberFormatResourceKey();
        if (theFormat == null) {
            return null;
        }

        DecimalFormatSymbols theSymbols = new DecimalFormatSymbols(FacesContext.getCurrentInstance().getViewRoot()
                .getLocale());
        theSymbols = new DecimalFormatSymbols();
        DecimalFormat theDecimalFormat = new DecimalFormat(ResourceBundleManager.getBundle().getString(theFormat),
                theSymbols);
        return theDecimalFormat;
    }
}
