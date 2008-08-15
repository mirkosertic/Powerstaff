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
package de.mogwai.common.web.backingbean.messagebox;

import de.mogwai.common.command.MessageBoxResultCommand;
import de.mogwai.common.web.enums.MessageResult;

/**
 * Backing bin der MessageBoxOkCancel.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:04:48 $
 */
public class MessageBoxOkCancelBackingBean extends MessageBoxBackingBean {

    /**
     * {@inheritDoc}
     */
    @Override
    public String ok() {
        return returnToCallbackTarget(MessageResult.OK);
    }

    /**
     * Action Methode für den Cancel-Button.
     * 
     * @return JSF Action String
     */
    public String cancel() {
        return returnToCallbackTarget(MessageResult.CANCEL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String returnToCallbackTarget(Object aValue) {

        forceUpdateOfBean(lastUpdateModelInfo.getSenderQueueID(), new MessageBoxResultCommand<Object>(aValue));

        return lastUpdateModelInfo.getCallerViewId();

    }

}
