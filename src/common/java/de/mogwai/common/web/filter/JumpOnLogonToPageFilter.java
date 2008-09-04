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

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Filter, welcher automatisch zu einer bestimmten Seite verzweigt, falls die
 * Anwendung zum ersten mal gestartet wird. Es wird ein Token in der aktuellen
 * Session hinterlegt, um zu tracken, ob die Anwendung zum ersten mal gestartet
 * wurde, oder nicht.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:19:11 $
 */
public class JumpOnLogonToPageFilter extends BaseFilter {

    private static final String INIT_PARAM_START_PAGE = "startPage";

    private static final String SESSION_TOKEN = "MogwaiJumpOnLogonToPageFilterSessionToken";

    private String startPage;

    /**
     * Initialize.
     * 
     * @param aConfig
     *                configuration
     * @throws ServletException
     *                 is thrown in case of an error
     */
    public void init(FilterConfig aConfig) throws ServletException {

        startPage = aConfig.getInitParameter(INIT_PARAM_START_PAGE);
        if ((startPage == null) || ("".equals(startPage))) {
            throw new ServletException("No " + INIT_PARAM_START_PAGE + " configured!");
        }

        logger.logInfo("Filter started");
    }

    public void doFilter(ServletRequest aRequest, ServletResponse aResponse, FilterChain aChain) throws IOException,
            ServletException {
        HttpServletRequest theRequest = (HttpServletRequest) aRequest;
        HttpServletResponse theResponse = (HttpServletResponse) aResponse;

        HttpSession theSession = theRequest.getSession();
        if (theSession.getAttribute(SESSION_TOKEN) != null) {

            String theRequestedURI = theRequest.getRequestURI();
            String theContextPath = theRequest.getContextPath();

            // Falls das Root angesprochen wird, und ein Session Token gesetzt
            // ist, soll
            // auch die Startseite angezeigt werden
            if ((theRequestedURI.endsWith("/")) || (theContextPath.equals(theRequestedURI))) {
                aRequest.getRequestDispatcher(startPage).forward(theRequest, theResponse);
                return;
            }

            aChain.doFilter(theRequest, theResponse);
            return;
        }

        theSession.setAttribute(SESSION_TOKEN, Boolean.TRUE);
        aRequest.getRequestDispatcher(startPage).forward(theRequest, theResponse);
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
        logger.logInfo("Filter destroyed");
    }
}
