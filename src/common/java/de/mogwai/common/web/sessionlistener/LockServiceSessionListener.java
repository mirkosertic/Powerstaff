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
package de.mogwai.common.web.sessionlistener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import de.mogwai.common.business.service.LockService;
import de.mogwai.common.logging.Logger;
import de.mogwai.common.usercontext.UserContext;

/**
 * Session Lister mit Kopplung an den LockService.
 * 
 * Sobald eine HttpSession entfernt wird, werden alle Locks , die für diese
 * HttpSession generiert wurdem, entfernt.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:38:07 $
 */
public class LockServiceSessionListener implements HttpSessionListener, UserContextWebConstants {

    private static final Logger LOGGER = new Logger(LockServiceSessionListener.class);

    public void sessionCreated(HttpSessionEvent theEvent) {

        LOGGER.logInfo("Session created : " + theEvent.getSession());
    }

    public void sessionDestroyed(HttpSessionEvent theEvent) {

        LOGGER.logInfo("Session destroyed : " + theEvent.getSession());

        HttpSession theSession = theEvent.getSession();

        UserContext theUserContext = (UserContext) theSession.getAttribute(SESSION_ATTRIBUTE_CONTEXT);
        if (theUserContext != null) {

            ApplicationContext theContext = WebApplicationContextUtils.getWebApplicationContext(theSession
                    .getServletContext());

            LockService lockService = (LockService) theContext.getBean("lockService");
            lockService.removeLocksForSession(theUserContext.getSessionId());
        }
    }

}
