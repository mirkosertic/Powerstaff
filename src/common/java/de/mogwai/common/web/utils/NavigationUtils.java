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
package de.mogwai.common.web.utils;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;

import org.apache.myfaces.config.RuntimeConfig;
import org.apache.myfaces.config.element.ManagedBean;
import org.apache.myfaces.el.VariableResolverImpl;

import de.mogwai.common.command.ResetNavigationCommand;
import de.mogwai.common.command.ResetNavigationInfo;
import de.mogwai.common.command.UpdateModelCommand;
import de.mogwai.common.web.backingbean.BackingBean;

/**
 * Common navigation utils.
 * 
 * These functions work in combination with the BeanInitingVariableResolver.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:15:59 $
 */
public final class NavigationUtils {

    private static NavigationUtils me;

    private VariableResolver resolver = new VariableResolverImpl();

    private NavigationUtils() {
    }

    public static NavigationUtils getInstance() {

        if (me == null) {
            me = new NavigationUtils();
        }

        return me;
    }

    /**
     * Reset der Navigation auf einer Bean forcieren.
     * 
     * @param aContext
     *                der FacesContext
     * @param aQueueID
     *                die QueueID der Bean
     * @param aCommand
     *                das zu verarbeitende Command
     */
    public void forceResetNavigationOfBeans(FacesContext aContext, String aQueueID, ResetNavigationCommand aCommand) {

        ResetNavigationInfo theInfo = new ResetNavigationInfo(aContext.getViewRoot().getViewId(), aCommand);

        // Nur die Managed Beans verarbeiten
        RuntimeConfig theConfig = RuntimeConfig.getCurrentInstance(aContext.getExternalContext());
        Map theBackingBeans = theConfig.getManagedBeans();
        for (Object theBeanKey : theBackingBeans.keySet()) {
            ManagedBean theBean = (ManagedBean) theBackingBeans.get(theBeanKey);

            Object theObject = resolver.resolveVariable(aContext, theBean.getManagedBeanName());
            if (theObject instanceof BackingBean) {
                BackingBean theBackingBean = (BackingBean) theObject;
                if (aQueueID.equals(theBackingBean.getQueueID())) {
                    theBackingBean.resetNavigation(theInfo);
                }
            }
        }
    }

    /**
     * Update eines Datenmodells einer Bean forcieren.
     * 
     * @param aCaller
     *                die aufrufende BackingBean
     * @param aContext
     *                der aktuelle FacesContext
     * @param aQueueID
     *                die Queue - ID
     * @param aCommand
     *                das Command - Objekt
     */
    public void forceUpdateOfBeans(BackingBean aCaller, FacesContext aContext, String aQueueID,
            UpdateModelCommand aCommand) {

        UpdateModelInfo theInfo = new UpdateModelInfo(aCaller.getQueueID(), aContext.getViewRoot().getViewId(),
                aCommand);

        // Nur die Managed Beans verarbeiten
        RuntimeConfig theConfig = RuntimeConfig.getCurrentInstance(aContext.getExternalContext());
        Map theBackingBeans = theConfig.getManagedBeans();
        for (Object theBeanKey : theBackingBeans.keySet()) {
            ManagedBean theBean = (ManagedBean) theBackingBeans.get(theBeanKey);

            Object theObject = resolver.resolveVariable(aContext, theBean.getManagedBeanName());
            if (theObject instanceof BackingBean) {
                BackingBean theBackingBean = (BackingBean) theObject;
                if (aQueueID.equals(theBackingBean.getQueueID())) {
                    theBackingBean.updateModel(theInfo);
                }
            }
        }
    }
}
