package de.powerstaff.web.converter;

import de.mogwai.common.web.ResourceBundleManager;
import de.powerstaff.web.backingbean.MessageConstants;
import org.apache.commons.lang.NotImplementedException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements Converter, MessageConstants {

    public Object getAsObject(FacesContext aContext, UIComponent aComponent, String aValue) {
        throw new NotImplementedException();
    }

    public String getAsString(FacesContext aContext, UIComponent aComponent, Object aValue) {
        Date theDate = (Date) aValue;
        if (theDate == null) {
            return "";
        }

        SimpleDateFormat theDateFormat = new SimpleDateFormat(ResourceBundleManager.getBundle().getString(DATE_FORMAT));
        return theDateFormat.format(theDate);
    }

}
