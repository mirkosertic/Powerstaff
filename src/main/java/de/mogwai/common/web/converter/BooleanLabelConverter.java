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
package de.mogwai.common.web.converter;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.ResourceBundleManager;

/**
 * Konvertiert die Booleanaustruecke der Benutzertabelle um.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-13 17:33:49 $
 */

public class BooleanLabelConverter implements Converter {

    private static final Logger LOGGER = new Logger(BooleanLabelConverter.class);

    /**
     * {@inheritDoc}
     */
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        String msg = "Konvertierung von Nachricht zu Boolean wird nicht unterstüzt";
        LOGGER.logError(msg);
        throw new ConverterException(msg);
    }

    /**
     * {@inheritDoc}
     */
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        ResourceBundle bundle = ResourceBundleManager.getBundle();

        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue() ? bundle.getString("booleanlabelconverter.true") : bundle
                    .getString("booleanlabelconverter.false");
        }

        String msg = MessageFormat.format(
                "Objekt ist vom Typ {0} statt Boolean und kann daher nicht konvertiert werden.", value.getClass());
        LOGGER.logError(msg);
        throw new ConverterException(msg);

    }

}
