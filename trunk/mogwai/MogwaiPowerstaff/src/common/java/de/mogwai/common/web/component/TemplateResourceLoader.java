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
package de.mogwai.common.web.component;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Velocity resource loader for templates.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:36:01 $
 */
public class TemplateResourceLoader extends ResourceLoader {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TemplateResourceLoader.class);

    @Override
    public void init(ExtendedProperties aProperties) {
    }

    @Override
    public InputStream getResourceStream(String aTemplateName) {
        try {

            String theName = "/templates/" + aTemplateName;

            LOGGER.debug("Trying to resolve resource {}", theName);

            return getClass().getClassLoader().getResourceAsStream(theName);

        } catch (Exception e) {

            LOGGER.error("Error resolving resource", e);

            throw new ResourceNotFoundException("Cannot load template " + aTemplateName);
        }
    }

    @Override
    public boolean isSourceModified(Resource aResource) {
        return true;
    }

    @Override
    public long getLastModified(Resource aResource) {
        return System.currentTimeMillis();
    }

}
