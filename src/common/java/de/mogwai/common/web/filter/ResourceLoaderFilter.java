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

import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Filter, mit welchem Resourcen geladen werden können.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:19:06 $
 */
public class ResourceLoaderFilter implements Filter {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ResourceLoaderFilter.class);

    private static final String INIT_PARAM_BASE_RESOURCE_NAME = "baseResourceName";

    private static final String PATH = "/resource/";

    private String baseResourceName;

    public void init(FilterConfig aConfig) throws ServletException {
        baseResourceName = aConfig.getInitParameter(INIT_PARAM_BASE_RESOURCE_NAME);
        if ((baseResourceName == null) || ("".equals(baseResourceName))) {
            throw new ServletException("No " + INIT_PARAM_BASE_RESOURCE_NAME + " configured!");
        }

        if (!baseResourceName.endsWith("/")) {
            baseResourceName += "/";
        }

        LOGGER.info("Configured for " + baseResourceName);
    }

    public void doFilter(ServletRequest aRequest, ServletResponse aResponse, FilterChain aChain) throws IOException,
            ServletException {
        HttpServletRequest theRequest = (HttpServletRequest) aRequest;
        HttpServletResponse theResponse = (HttpServletResponse) aResponse;

        theResponse.setHeader("Cache-Control", "no-store, no-cache");
        theResponse.setHeader("Pragma", "no-cache");
        theResponse.setDateHeader("Expires", 0);

        String theRequestURI = theRequest.getRequestURI();
        int p = theRequestURI.lastIndexOf(PATH);
        if (p >= 0) {
            theRequestURI = theRequestURI.substring(p + PATH.length());
        } else {
            throw new ServletException("Filter is not configured for path " + PATH);
        }

        String theResource = baseResourceName + theRequestURI;

        LOGGER.debug("Trying to access resource {}", theResource);

        InputStream theInputStream = getClass().getResourceAsStream(theResource);
        if (theInputStream != null) {

            OutputStream theStream = theResponse.getOutputStream();
            byte[] theData = new byte[8192];
            int theLength = theInputStream.read(theData);
            while (theLength > 0) {
                theStream.write(theData, 0, theLength);
                theLength = theInputStream.read(theData);
            }

            theStream.close();

        } else {
            throw new ServletException("Invalid resource name " + theResource);
        }
    }

    public void destroy() {
        LOGGER.info("Filter destroyed");
    }
}
