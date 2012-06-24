package de.powerstaff.web.backingbean;

import de.powerstaff.business.entity.Project;
import de.powerstaff.business.service.ProjectService;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Map;

public class ContextUtils {

    private static final String PROJECT_SESSION_ID = "projectId";

    private ProjectService projectService;

    private static ThreadLocal<Map<String, Object>> REQUESTCACHE = new ThreadLocal<Map<String, Object>>();

    public ProjectService getProjectService() {
        return projectService;
    }

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void setCurrentProject(Project aProject) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(PROJECT_SESSION_ID, aProject.getId());
    }

    public boolean isContextSet() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey(PROJECT_SESSION_ID);
    }

    public void commandClearContext() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(PROJECT_SESSION_ID);
    }

    public static Map<String, Object> getCurrentCache() {
        Map<String, Object> theCurrent = REQUESTCACHE.get();
        if (theCurrent == null) {
            theCurrent = new HashMap<String, Object>();
            REQUESTCACHE.set(theCurrent);
        }
        return theCurrent;
    }

    public Project getCurrentProject() {
        Project theCurrentProject = (Project) getCurrentCache().get(PROJECT_SESSION_ID);
        if (theCurrentProject == null) {
            ExternalContext theExternalContext = FacesContext.getCurrentInstance().getExternalContext();
            Long theId = (Long) theExternalContext.getSessionMap().get(PROJECT_SESSION_ID);
            if (theId != null) {
                theCurrentProject = projectService.findByPrimaryKey(theId);
                getCurrentCache().put(PROJECT_SESSION_ID, theCurrentProject);
            }
        }

        return theCurrentProject;
    }

    public String getCurrrentProjectDescription() {
        Project theProject = getCurrentProject();
        if (theProject != null) {
            return theProject.getProjectNumber();
        }
        return "";
    }

    public static void cleanupCache() {
        REQUESTCACHE.remove();
    }
}
