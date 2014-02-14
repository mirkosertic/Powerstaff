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

import de.mogwai.common.web.ResourceBundleManager;
import de.mogwai.common.web.component.input.BaseInputComponent;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Validator für Email-Adresse.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:34:38 $
 */
public class MailValidator implements Validator {

    private static final String VALIDATION_ERROR = "validator.invalidemail";

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MailValidator.class);

    public void validate(FacesContext context, UIComponent component, Object checkValue) {

        boolean isvalid = true;
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
        if (!(component instanceof EditableValueHolder)) {
            LOGGER.debug("Validator kann nur an EditableValueHolder-Komponente angebunden werden");
            return;
        }

        EditableValueHolder theValueHolder = (EditableValueHolder) component;

        if (checkValue == null || checkValue.equals("")) {
            return;
        }

        String value = checkValue.toString();

        if (value.length() < 6) {
            isvalid = false;
        }
        int at = value.indexOf("@");
        if (at <= 0) {
            isvalid = false;
        }
        int dot = value.lastIndexOf(".");
        if (dot < 0 || (value.length() - dot > 5)) {
            isvalid = false;
        }
        if (isvalid) {
            theValueHolder.setValid(true);
        } else {
            theValueHolder.setValid(false);

            String theLabel = "";
            if (theValueHolder instanceof BaseInputComponent) {
                theLabel = ((BaseInputComponent) theValueHolder).getDescribingLabel();
            }

            ResourceBundle theBundle = ResourceBundleManager.getBundle();
            String theMessage = MessageFormat.format(theBundle.getString(VALIDATION_ERROR), new Object[] { theLabel });
            FacesMessage theFacesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, theMessage, "");

            throw new ValidatorException(theFacesMessage);
        }

    }

}
