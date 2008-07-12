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
package de.mogwai.common.velocity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.log4j.Logger;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

public class FileResourceLoader extends ResourceLoader {

    private Logger logger = Logger.getLogger(getClass());

    @Override
    public long getLastModified(Resource aResource) {
        return System.currentTimeMillis();
    }

    @Override
    public InputStream getResourceStream(String aResourceName) {
        try {
            return new FileInputStream(aResourceName);
        } catch (FileNotFoundException e) {
            throw new ResourceNotFoundException(aResourceName);
        }
    }

    @Override
    public void init(ExtendedProperties aProperties) {
        logger.debug("Init");
    }

    @Override
    public boolean isSourceModified(Resource arg0) {
        return true;
    }
}
