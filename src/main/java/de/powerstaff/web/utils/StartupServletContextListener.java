/**
 * Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.powerstaff.web.utils;

import de.powerstaff.business.service.DomainHelper;
import de.powerstaff.business.service.ProfileIndexerService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class StartupServletContextListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent aEvent) {

        WebApplicationContext theContext = WebApplicationContextUtils.getWebApplicationContext(aEvent.getServletContext());

        DomainHelper.getInstance().registerApplicationContext(theContext);

        ProfileIndexerService theService = (ProfileIndexerService) theContext.getBean("profileIndexerService");
        theService.rebuildIndex();
    }

    @Override
    public void contextDestroyed(ServletContextEvent aEvent) {
    }
}