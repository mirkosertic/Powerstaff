package de.powerstaff.web.converter;

import de.powerstaff.business.entity.Person;
import org.apache.commons.lang.NotImplementedException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class PersonConverter implements Converter {

    public Object getAsObject(FacesContext aContext, UIComponent aComponent, String aValue) {
        throw new NotImplementedException();
    }

    public String getAsString(FacesContext aContext, UIComponent aComponent, Object aValue) {
        Person thePerson = (Person) aValue;
        if (thePerson == null) {
            return "";
        }

        StringBuffer theResult = new StringBuffer();
        if (thePerson.getName1() != null) {
            theResult.append(thePerson.getName1());
        }

        if (thePerson.getName2() != null) {
            if (theResult.length() > 0) {
                theResult.append(",");
            }
            theResult.append(thePerson.getName2());
        }

        return theResult.toString();
    }

}
