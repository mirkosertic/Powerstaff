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
package de.powerstaff.business.service.impl;

import de.powerstaff.business.dao.ProjectDAO;
import de.powerstaff.business.dao.WebsiteDAO;
import de.powerstaff.business.entity.Project;
import de.powerstaff.business.entity.WebProject;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.ServiceLoggerService;
import de.powerstaff.business.service.WebSyncService;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Collection;

public class WebSyncServiceImpl implements WebSyncService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ProfileSearchServiceImpl.class);

    private static final String SERVICE_ID = "WebProjectSync";

    private WebsiteDAO websiteDAO;

    private ServiceLoggerService serviceLogger;

    private PowerstaffSystemParameterService systemParameterService;

    private ProjectDAO projectDAO;

    /**
     * @return the projectDAO
     */
    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    /**
     * @param projectDAO the projectDAO to set
     */
    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public WebsiteDAO getWebsiteDAO() {
        return websiteDAO;
    }

    public void setWebsiteDAO(WebsiteDAO websiteDAO) {
        this.websiteDAO = websiteDAO;
    }

    /**
     * @return the systemParameterService
     */
    public PowerstaffSystemParameterService getSystemParameterService() {
        return systemParameterService;
    }

    /**
     * @param systemParameterService the systemParameterService to set
     */
    public void setSystemParameterService(PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    public void sync() {
        sync(false);
    }

    public void forceSync() {
        sync(true);
    }

    public void sync(boolean aForce) {

        if (!aForce) {
            if (!systemParameterService.isWebSyncEnabled()) {
                LOGGER.info("WebSync wurde deaktiviert");
                return;
            }
        }

        SimpleDateFormat theFormat = new SimpleDateFormat("dd.MM.yyyy");

        serviceLogger.logStart(SERVICE_ID, "");

        try {

            LOGGER.debug("Starting synchronize with web");

            LOGGER.debug("Deleting inaktive projects from web");

            Collection<WebProject> theWebProjects = websiteDAO.getCurrentProjects();
            for (WebProject theWebProject : theWebProjects) {

                Project theProject = (Project) projectDAO.findByPrimaryKey(theWebProject.getId());
                if ((theProject == null) || (!theProject.isVisibleOnWebSite())) {

                    websiteDAO.delete(theWebProject);

                    LOGGER
                            .debug("Deleting project " + theWebProject.getId()
                                    + " from web as it is no longer active");
                }
            }

            Collection theLocalProjects = (Collection) projectDAO.getActiveProjects();
            for (Object theObject : theLocalProjects) {

                Project theProject = (Project) theObject;

                WebProject theWebProject = (WebProject) websiteDAO.getById(theProject.getId());
                boolean isNew = false;
                if (theWebProject == null) {
                    theWebProject = new WebProject();
                    isNew = true;
                }

                theWebProject.setCreationDate(theProject.getCreationDate());
                theWebProject.setCreationUserID(theProject.getCreationUserID());
                theWebProject.setLastModificationDate(theProject.getLastModificationDate());
                theWebProject.setLastModificationUserID(theProject.getLastModificationUserID());
                if (theProject.getEntryDate() != null) {
                    theWebProject.setDate(theFormat.format(theProject.getEntryDate()));
                } else {
                    theWebProject.setDate("");
                }
                theWebProject.setProjectNumber(theProject.getProjectNumber());
                theWebProject.setWorkplace(theProject.getWorkplace());
                if (theProject.getStartDate() != null) {
                    theWebProject.setStart(theFormat.format(theProject.getStartDate()));
                } else {
                    theWebProject.setStart("");
                }
                theWebProject.setDuration(theProject.getDuration());
                theWebProject.setDescriptionShort(theProject.getDescriptionShort());
                theWebProject.setDescriptionLong(theProject.getDescriptionLong());
                theWebProject.setId(theProject.getId());

                if (isNew) {
                    websiteDAO.save(theWebProject);
                } else {
                    websiteDAO.saveOrUpdate(theWebProject);
                }

            }

            serviceLogger.logEnd(SERVICE_ID, "");

        } catch (Exception e) {

            serviceLogger.logEnd(SERVICE_ID, "Failed with error " + e.getMessage());

            LOGGER.error("Failed", e);
        }

        LOGGER.debug("Finished");
    }

    public ServiceLoggerService getServiceLogger() {
        return serviceLogger;
    }

    public void setServiceLogger(ServiceLoggerService serviceLogger) {
        this.serviceLogger = serviceLogger;
    }
}
