package de.powerstaff.web.converter;

import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.ProjectFirstContact;
import org.apache.commons.lang.NotImplementedException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class ProjectFirstContactConverter implements Converter {

    public Object getAsObject(FacesContext aContext, UIComponent aComponent, String aValue) {
        throw new NotImplementedException();
    }

    public String getAsString(FacesContext aContext, UIComponent aComponent, Object aValue) {
        ProjectFirstContact theContact = (ProjectFirstContact) aValue;
        ContactType theType = theContact.getContactType();

        String theDisplayValue = theContact.getContactTypeValue();
        if (theDisplayValue.length() > 35) {
            theDisplayValue = theDisplayValue.substring(0, 25);
            theDisplayValue += "...";
        }

        if (theType != null) {
            if (theType.isWeb()) {

                String theValue = theContact.getContactTypeValue();
                if ((!theValue.toLowerCase().startsWith("http://")) && (!theValue.toLowerCase().startsWith("https://"))) {
                    theValue = "http://" + theValue;
                }

                return "<a class=\"contact\"  target=\"_blank\" href=\"" + theValue + "\">" + theDisplayValue + "</a>";
            }

            if (theType.isEmail()) {
                return "<a class=\"contact\" href=\"mailto:" + theContact.getContactTypeValue() + "\">" + theDisplayValue + "</a>";
            }

            if (theType.isGulp()) {

                String theValue = theContact.getContactTypeValue();

                theValue = "http://www.gulp.de/profil/" + theValue + ".html";

                return "<a class=\"contact\"  target=\"_blank\" href=\"" + theValue + "\">" + theDisplayValue + "</a>";

            }
        }

        return theDisplayValue;
    }

}
