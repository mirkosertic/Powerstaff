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
package de.mogwai.common.web.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Base class for every spring aware filter.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:09:01 $
 */
public abstract class SpringAwareFilter extends BaseFilter {

    protected ApplicationContext context;

    /**
     * Initialize.
     * 
     * @param aConfig
     *                configuration
     * @throws ServletException
     *                 is thrown in case of an error
     */
    public void init(FilterConfig aConfig) throws ServletException {
        context = WebApplicationContextUtils.getWebApplicationContext(aConfig.getServletContext());

        logger.logDebug("Using Spring context " + context);
    }

}
