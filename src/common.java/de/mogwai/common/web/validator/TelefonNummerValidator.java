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
package de.mogwai.common.web.validator;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.ResourceBundleManager;

/**
 * Telefon/Fax - Nummer-Validator. Eine Nummer besthend aus min. 10 Ziffern wird
 * als gültig akzeptiert.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:04:40 $
 */
public class TelefonNummerValidator implements Validator {
    private static final String VALIDATION_ERROR = "validator.invalidphonenumber";

    private static final Logger LOGGER = new Logger(TelefonNummerValidator.class);

    public void validate(FacesContext context, UIComponent component, Object checkValue) {

        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        if (!(component instanceof EditableValueHolder)) {
            LOGGER.logDebug("Validator kann nur an EditableValueHolder-Komponente angebunden werden");
            return;
        }

        if (checkValue == null || checkValue.equals("")) {
            return;
        }

        StringBuilder value = new StringBuilder(checkValue.toString());
        int numberCount = 0;

        for (int i = 0; i < value.length(); i++) {

            if (Character.isDigit(value.charAt(i))) {
                numberCount++;
            }
        }

        if (numberCount >= 10) {
            ((EditableValueHolder) component).setValid(true);
        } else {
            ((EditableValueHolder) component).setValid(false);
            ResourceBundle bndl = ResourceBundleManager.getBundle();
            FacesMessage errMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, bndl.getString(VALIDATION_ERROR), "");

            throw new ValidatorException(errMsg);
        }

    }

}
