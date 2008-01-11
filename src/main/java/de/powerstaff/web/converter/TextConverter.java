package de.powerstaff.web.converter;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.NotImplementedException;

public class TextConverter implements Converter {

	public Object getAsObject(FacesContext aContext, UIComponent aComponent, String aValue) throws ConverterException {
		throw new NotImplementedException("Nicht implementiert");
	}

	public String getAsString(FacesContext aContext, UIComponent aComponent, Object aValue) throws ConverterException {
		StringBuilder result = new StringBuilder();

		StringCharacterIterator iterator = new StringCharacterIterator((String)aValue);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == '<') {
				result.append("&lt;");
			} else if (character == '>') {
				result.append("&gt;");
			} else if (character == '\"') {
				result.append("&quot;");
			} else if (character == '\'') {
				result.append("&#039;");
			} else if (character == '\\') {
				result.append("&#092;");
			} else if (character == '&') {
				result.append("&amp;");
			} else if (character == '\n') {
				result.append("<br/>");
			} else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();		
	}

}
