package de.powerstaff.web.converter;

import java.text.SimpleDateFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang.NotImplementedException;

import de.mogwai.common.business.entity.AuditableEntity;
import de.mogwai.common.web.ResourceBundleManager;

public class CreationAuditInformationConverter implements Converter {

    public Object getAsObject(FacesContext aContext, UIComponent aComponent, String aValue) {
        throw new NotImplementedException();
    }

    public String getAsString(FacesContext aContext, UIComponent aComponent, Object aValue) {

        SimpleDateFormat theFormat = new SimpleDateFormat(ResourceBundleManager.getBundle().getString(
                "global.timestampformat"));

        AuditableEntity theEntity = (AuditableEntity) aValue;
        StringBuffer theResult = new StringBuffer();

        if (theEntity.getCreationDate() != null) {
            theResult.append("Erstellt ");
            theResult.append(theFormat.format(theEntity.getCreationDate()));
            theResult.append("(" + theEntity.getCreationUserID() + ")");
        }

        return theResult.toString();
    }

}
