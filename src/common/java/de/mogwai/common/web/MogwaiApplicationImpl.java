/*
  Copyright 2002 - 2007 the Mogwai Project.

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.mogwai.common.web;

import de.mogwai.common.web.fieldinformationresolver.jpa.JPAAnnotationFieldInformationResolver;
import org.apache.myfaces.application.ApplicationImpl;
import org.slf4j.LoggerFactory;

public class MogwaiApplicationImpl extends ApplicationImpl {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MogwaiApplicationImpl.class);

    private FieldInformationResolver fieldInformationResolver;

    public FieldInformationResolver getFieldInformationResolver() {
        if (fieldInformationResolver == null) {
            fieldInformationResolver = new JPAAnnotationFieldInformationResolver();
        }
        return fieldInformationResolver;
    }
}
