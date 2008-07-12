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

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

import de.mogwai.common.logging.Logger;

public class MogwaiApplicationFactory extends ApplicationFactory {

    private final static Logger LOGGER = new Logger(MogwaiApplicationFactory.class);

    private Application application;

    public MogwaiApplicationFactory() {
        LOGGER.logDebug("Initing ApplicationFactory");
        application = new MogwaiApplicationImpl();
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public void setApplication(Application aApplication) {
        application = aApplication;
    }

}
