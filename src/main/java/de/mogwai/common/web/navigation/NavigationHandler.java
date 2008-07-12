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
package de.mogwai.common.web.navigation;

import javax.faces.context.FacesContext;

import org.apache.myfaces.application.NavigationHandlerImpl;
import org.apache.myfaces.config.element.NavigationCase;

/**
 * A specialized navigation handler supporting the backing bean and its
 * specialized navigation behavior using the navigation utilities. If an
 * updateModel is forced by another backing bean, the updated backing bean
 * stores the information about who updated it. It can now generate a
 * specialized outcome with the getReturnToLastUpdaterOutcome method to return
 * to its updaters view. This outcome is handled in this specialized navigation
 * handler.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:21 $
 */
public class NavigationHandler extends NavigationHandlerImpl {

    public static class NavigationCaseImpl implements NavigationCase {

        private String action;

        private String outcome;

        private String viewId;

        public NavigationCaseImpl(String aAction, String aOutcome, String aViewId) {
            action = aAction;
            outcome = aOutcome;
            viewId = aViewId;
        }

        public String getFromAction() {
            return action;
        }

        public String getFromOutcome() {
            return outcome;
        }

        public String getToViewId() {
            return viewId;
        }

        public boolean isRedirect() {
            return false;
        }

    }

    @Override
    public NavigationCase getNavigationCase(FacesContext facesContext, String fromAction, String outcome) {

        // Special treatment for direct outcomes, these ignore the navigation
        // rules!
        if (outcome.startsWith("/")) {
            NavigationCase theCase = new NavigationCaseImpl(fromAction, outcome, outcome);
            return theCase;
        }

        return super.getNavigationCase(facesContext, fromAction, outcome);
    }
}
