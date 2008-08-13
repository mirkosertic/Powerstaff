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
package de.mogwai.common.web.resolver;

import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

import org.springframework.web.jsf.DelegatingVariableResolver;

import de.mogwai.common.logging.Logger;
import de.mogwai.common.utils.Initable;

/**
 * Ein spezialisierter Variable - Resolver, welcher Beans, die referenziert
 * werden, initialisieren kann. Siehe hierzu das Initable interface. Zudem wird
 * dieser Variable Resolver für die Inter - Bean Kommunikation innerhalb der
 * Anwendung "missbraucht".
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-13 17:33:48 $
 */
public class BeanInitingVariableResolver extends DelegatingVariableResolver {

    private static final Logger LOGGER = new Logger(BeanInitingVariableResolver.class);

    public BeanInitingVariableResolver(VariableResolver aResolver) {
        super(aResolver);
    }

    @Override
    public Object resolveVariable(FacesContext aContext, String aExpression) {

        Object theVariable = super.resolveVariable(aContext, aExpression);

        if (theVariable instanceof Initable) {
            Initable theInitable = (Initable) theVariable;
            if (!theInitable.isInitialized()) {

                LOGGER.logDebug("Forcing initialization on " + theVariable);
                try {
                    theInitable.init();
                } catch (Exception e) {
                    LOGGER.logError("Object could not be initialized", e);

                    throw new EvaluationException("Object could not be initialized", e);
                }
            }
        }
        return theVariable;
    }
}
