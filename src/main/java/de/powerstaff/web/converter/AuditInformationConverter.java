package de.powerstaff.web.converter;

import de.mogwai.common.business.entity.AuditableEntity;
import de.mogwai.common.web.ResourceBundleManager;
import org.apache.commons.lang.NotImplementedException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.text.SimpleDateFormat;

public class AuditInformationConverter implements Converter {

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

        if (theEntity.getLastModificationDate() != null) {
            theResult.append(" Zuletzt bearbeitet ");
            theResult.append(theFormat.format(theEntity.getLastModificationDate()));
            theResult.append("(" + theEntity.getLastModificationUserID() + ")");
        }

        return theResult.toString();
    }

}
