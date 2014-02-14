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

import de.mogwai.common.web.fieldinformationresolver.jpa.JPAAnnotationFieldInformationResolver;
import org.apache.myfaces.application.ApplicationImpl;
import org.apache.myfaces.shared_impl.util.BiLevelCacheMap;
import org.slf4j.LoggerFactory;

import javax.faces.el.ValueBinding;
import java.util.Map;

public class MogwaiApplicationImpl extends ApplicationImpl {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MogwaiApplicationImpl.class);

    private FieldInformationResolver fieldInformationResolver;

    private Map valueBindingCache = new BiLevelCacheMap(90) {

        @Override
        protected Object newInstance(Object aKey) {
            return new MogwaiValueBindingImpl(MogwaiApplicationImpl.this, (String) aKey);
        }
    };

    @Override
    public ValueBinding createValueBinding(String reference) {
        if ((reference == null) || (reference.length() == 0)) {
            LOGGER.error("createValueBinding: reference = null is not allowed");
            throw new NullPointerException("createValueBinding: reference = null is not allowed");
        }

        return (ValueBinding) valueBindingCache.get(reference);
    }

    public FieldInformationResolver getFieldInformationResolver() {
        if (fieldInformationResolver == null) {
            fieldInformationResolver = new JPAAnnotationFieldInformationResolver();
        }
        return fieldInformationResolver;
    }
}
