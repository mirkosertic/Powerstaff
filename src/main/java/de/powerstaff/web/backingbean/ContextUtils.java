package de.powerstaff.web.backingbean;

import de.powerstaff.business.entity.Project;
import de.powerstaff.business.service.ProjectService;

import javax.faces.context.FacesContext;

public class ContextUtils {

    private static final String SESSION_ID = "projectId";

    private ProjectService projectService;

    public ProjectService getProjectService() {
        return projectService;
    }

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void setCurrentProject(Project aProject) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(SESSION_ID, aProject.getId());
    }

    public boolean isContextSet() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey(SESSION_ID);
    }

    public void commandClearContext() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(SESSION_ID);
    }

    public Project getCurrentProject() {
        Long theId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(SESSION_ID);
        if (theId != null) {
            return projectService.findByPrimaryKey(theId);
        }
        return null;
    }

    public String getCurrrentProjectDescription() {
        Project theProject = getCurrentProject();
        if (theProject != null) {
            return theProject.getProjectNumber();
        }
        return "";
    }
}
