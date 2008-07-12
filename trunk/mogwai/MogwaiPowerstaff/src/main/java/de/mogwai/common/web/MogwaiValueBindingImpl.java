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
package de.mogwai.common.web;

import javax.faces.context.FacesContext;

import org.apache.myfaces.el.ValueBindingImpl;

import de.mogwai.common.logging.Logger;

public class MogwaiValueBindingImpl extends ValueBindingImpl implements FieldInformationProvider {

    private final static Logger LOGGER = new Logger(MogwaiValueBindingImpl.class);

    public MogwaiValueBindingImpl(MogwaiApplicationImpl aApplication, String aExpression) {
        super(aApplication, aExpression);
    }

    public MogwaiValueBindingImpl() {
    }

    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    protected FieldInformationResolver getFieldInformationResolver() {
        return ((MogwaiApplicationImpl) _application).getFieldInformationResolver();
    }

    public Integer getMaxLength(FacesContext aContext) {
        Object theValue;
        try {
            theValue = resolveToBaseAndProperty(getFacesContext());
        } catch (Exception e) {
            LOGGER.logError("Cannot resolve " + getExpressionString(), e);
            return null;
        }

        if (theValue instanceof String) {
            return null;
        }

        Object[] theClassAndProperty = (Object[]) theValue;
        Object theBase = theClassAndProperty[0];
        String thePropertyName = (String) theClassAndProperty[1];

        if (theBase == null) {
            return null;
        }

        return getFieldInformationResolver().getMaxLengthInformationProvided(_application, aContext, theBase,
                thePropertyName);
    }

    public Boolean isRequired(FacesContext aContext) {
        Object theValue;
        try {
            theValue = resolveToBaseAndProperty(getFacesContext());
        } catch (Exception e) {
            LOGGER.logError("Cannot resolve " + getExpressionString(), e);
            return null;
        }

        if (theValue instanceof String) {
            return null;
        }

        Object[] theClassAndProperty = (Object[]) theValue;
        Object theBase = theClassAndProperty[0];
        String thePropertyName = (String) theClassAndProperty[1];

        if (theBase == null) {
            return null;
        }

        return getFieldInformationResolver().getRequiredInformation(_application, aContext, theBase, thePropertyName);
    }
}
