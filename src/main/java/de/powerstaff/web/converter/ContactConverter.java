package de.powerstaff.web.converter;

import de.powerstaff.business.entity.Contact;
import de.powerstaff.business.entity.ContactType;
import org.apache.commons.lang.NotImplementedException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class ContactConverter implements Converter {

    public Object getAsObject(FacesContext aContext, UIComponent aComponent, String aValue) {
        throw new NotImplementedException();
    }

    public String getAsString(FacesContext aContext, UIComponent aComponent, Object aValue) {
        Contact theContact = (Contact) aValue;
        ContactType theType = theContact.getType();

        String theDisplayValue = theContact.getValue();
        if (theDisplayValue.length() > 35) {
            theDisplayValue = theDisplayValue.substring(0, 25);
            theDisplayValue += "...";
        }

        if (theType != null) {
            if (theType.isWeb()) {

                String theValue = theContact.getValue();
                if ((!theValue.toLowerCase().startsWith("http://")) && (!theValue.toLowerCase().startsWith("https://"))) {
                    theValue = "http://" + theValue;
                }

                return "<a class=\"contact\"  target=\"_blank\" href=\"" + theValue + "\">" + theDisplayValue + "</a>";
            }

            if (theType.isEmail()) {
                return "<a class=\"contact\" href=\"mailto:" + theContact.getValue() + "\">" + theDisplayValue + "</a>";
            }

            if (theType.isGulp()) {

                String theValue = theContact.getValue();

                theValue = "http://www.gulp.de/profil/" + theValue + ".html";

                return "<a class=\"contact\"  target=\"_blank\" href=\"" + theValue + "\">" + theDisplayValue + "</a>";

            }
        }

        return theDisplayValue;
    }

}
