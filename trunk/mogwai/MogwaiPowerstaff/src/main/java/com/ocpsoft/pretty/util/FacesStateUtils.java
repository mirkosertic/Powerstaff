package com.ocpsoft.pretty.util;

import javax.faces.context.FacesContext;

public class FacesStateUtils
{
    public boolean isPostback()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return !facesContext.getExternalContext().getRequestParameterMap().containsKey("javax.faces.ViewState");
    }
}