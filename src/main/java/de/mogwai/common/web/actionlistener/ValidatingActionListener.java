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
package de.mogwai.common.web.actionlistener;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.ActionSource;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.servlet.jsp.el.ELException;

import org.apache.myfaces.application.ActionListenerImpl;
import org.apache.myfaces.el.ValueBindingImpl;

import de.mogwai.common.logging.Logger;
import de.mogwai.common.usercontext.UserContext;
import de.mogwai.common.usercontext.UserContextHolder;
import de.mogwai.common.web.utils.Validatable;

/**
 * Ein spezialisierter Action - Listener, welcher Validierung vor dem
 * Methodenaufruf unterstützt.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:12:09 $
 */
public class ValidatingActionListener extends ActionListenerImpl {

    private final static Logger LOGGER = new Logger(ValidatingActionListener.class);

    private class ValueBindingWrapper extends ValueBindingImpl {
        public ValueBindingWrapper(FacesContext aContext, String aExpression) {
            super(aContext.getApplication(), aExpression);
        }

        @Override
        public Object resolveToBaseAndProperty(FacesContext facesContext) throws ELException {
            return super.resolveToBaseAndProperty(facesContext);
        }
    }

    @Override
    public void processAction(ActionEvent aActionEvent) {
        FacesContext theContext = FacesContext.getCurrentInstance();
        Application theApplication = theContext.getApplication();

        ActionSource theActionSource = (ActionSource) aActionEvent.getComponent();
        MethodBinding theMethodBinding = theActionSource.getAction();

        String theFromAction;
        String theOutcome = null;
        if (theMethodBinding == null) {
            theFromAction = null;
            theOutcome = null;
        } else {
            theFromAction = theMethodBinding.getExpressionString();
            try {
                // Try to guess the affected class
                ValueBindingWrapper theWrapper = new ValueBindingWrapper(theContext, theFromAction);
                Object[] theBaseAndProperty = (Object[]) theWrapper.resolveToBaseAndProperty(theContext);
                Object theBase = theBaseAndProperty[0];

                LOGGER.logDebug("Executing method " + theBaseAndProperty[1] + " on bean " + theBase);

                boolean theValidationResult = true;

                // Set the current JSF - Locale to the User - Context
                UserContext theUserContext = UserContextHolder.getUserContext();
                if (theUserContext != null) {
                    theUserContext.setSessionValue(UserContext.LOCALE, theContext.getViewRoot().getLocale());
                }

                // If the target object is validatable, perform a validation
                // But only if its not set to immediate
                if ((theBase instanceof Validatable) && (!theActionSource.isImmediate())) {

                    theValidationResult = ((Validatable) theBase).validate(theContext);

                    LOGGER.logDebug("Validation result is " + theValidationResult);
                }

                // Only perform the real method invocation, if the validation
                // result is positive. Else, do nothing
                if (theValidationResult) {

                    theOutcome = (String) theMethodBinding.invoke(theContext, null);

                    // For the case that the user locale was changed, update the
                    // locale in
                    // the current session
                    if (theUserContext != null) {
                        theUserContext.setSessionValue(UserContext.LOCALE, theContext.getViewRoot().getLocale());
                    }

                    LOGGER.logDebug("Result outcome is " + theOutcome);
                }

                // Log the Messages
                Iterator theMessagesIterator = theContext.getMessages();
                while (theMessagesIterator.hasNext()) {
                    FacesMessage theMessage = (FacesMessage) theMessagesIterator.next();

                    LOGGER.logDebug("Message : " + theMessage.getSeverity() + " " + theMessage.getSummary());
                }

            } catch (ELException e1) {

                throw new RuntimeException("Error on method binding expression " + theFromAction, e1);

            } catch (EvaluationException e) {
                Throwable cause = e.getCause();
                if (cause != null && cause instanceof AbortProcessingException) {
                    throw (AbortProcessingException) cause;
                } else {
                    throw new FacesException("EvaluationException calling action method " + theFromAction
                            + " of component with id " + aActionEvent.getComponent().getClientId(theContext), e
                            .getCause());
                }
            } catch (RuntimeException e) {
                throw new FacesException("RuntimeException calling action method " + theFromAction
                        + " of component with id " + aActionEvent.getComponent().getClientId(theContext), e);
            }
        }

        NavigationHandler theNavigationHandler = theApplication.getNavigationHandler();
        theNavigationHandler.handleNavigation(theContext, theFromAction, theOutcome);
        theContext.renderResponse();
    }
}