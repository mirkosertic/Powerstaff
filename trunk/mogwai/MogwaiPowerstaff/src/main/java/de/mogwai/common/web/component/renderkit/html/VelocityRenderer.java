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
package de.mogwai.common.web.component.renderkit.html;

import java.io.Writer;
import java.util.HashMap;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import de.mogwai.common.logging.Logger;

/**
 * Renderer that supports velocity templates.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:54 $
 */
public class VelocityRenderer extends BaseRenderer {

    private final static Logger LOGGER = new Logger(VelocityRenderer.class);

    private VelocityEngine theEngine = new VelocityEngine();

    public VelocityRenderer() {
        initVelocity();
    }

    protected void renderTemplate(HashMap<String, Object> aParams, String aTemplateName, Writer aWriter)
            throws Exception {

        VelocityContext theContext = new VelocityContext();

        for (String theKey : aParams.keySet()) {
            theContext.put(theKey, aParams.get(theKey));
        }

        LOGGER.logDebug("Rendering template " + aTemplateName);

        Template theTemplate = theEngine.getTemplate(aTemplateName);

        try {
            theTemplate.merge(theContext, aWriter);
        } catch (Exception e) {
            LOGGER.logError("Error during rendering", e);
            throw e;
        }
    }

    protected void initVelocity() {

        Properties theProperies = new Properties();
        theProperies.put("resource.loader", "templates");
        theProperies.put("templates.resource.loader.class", "de.mogwai.common.web.component.TemplateResourceLoader");
        theProperies.put("runtime.log.logsystem", "org.apache.velocity.runtime.log.Log4JLogSystem");

        try {
            theEngine.init(theProperies);
        } catch (Exception e) {
            LOGGER.logError("Error initializing velocity engine", e);
            throw new RuntimeException(e);
        }
    }
}
