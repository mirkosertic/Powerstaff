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
package de.mogwai.common.web.fieldinformationresolver.jpa;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.persistence.Column;

import org.apache.commons.beanutils.PropertyUtils;

import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.FieldInformationResolver;

public class JPAAnnotationFieldInformationResolver implements FieldInformationResolver {

    private static final Logger LOGGER = new Logger(JPAAnnotationFieldInformationResolver.class);

    public Integer getMaxLengthInformationProvided(Application aApplication, FacesContext aContext, Object aBase,
            String aProperty) {

        try {
            PropertyDescriptor theDescriptor = PropertyUtils.getPropertyDescriptor(aBase, aProperty);
            if (theDescriptor == null) {
                return null;
            }

            Method theReadMethod = theDescriptor.getReadMethod();
            if (theReadMethod != null) {
                Column theColumn = theReadMethod.getAnnotation(Column.class);
                if (theColumn != null) {
                    return theColumn.length();
                }
            }

            return null;

        } catch (Exception e) {
            LOGGER.logError("Error", e);
            return null;
        }
    }

    public Boolean getRequiredInformation(Application aApplication, FacesContext aContext, Object aBase,
            String aProperty) {
        try {
            PropertyDescriptor theDescriptor = PropertyUtils.getPropertyDescriptor(aBase, aProperty);
            if (theDescriptor == null) {
                return null;
            }

            Method theReadMethod = theDescriptor.getReadMethod();
            if (theReadMethod != null) {
                Column theColumn = theReadMethod.getAnnotation(Column.class);
                if (theColumn != null) {
                    return !theColumn.nullable();
                }
            }

            return null;

        } catch (Exception e) {
            LOGGER.logError("Error", e);
            return null;
        }
    }

}
