package de.powerstaff.web.converter;

import de.powerstaff.business.entity.Tag;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class TagConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) throws ConverterException {
        throw new NotImplementedException();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) throws ConverterException {
        Tag theTag = (Tag) o;
        return theTag.getName();
    }
}
