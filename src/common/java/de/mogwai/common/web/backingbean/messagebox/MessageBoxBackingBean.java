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

import de.mogwai.common.command.Command;
import de.mogwai.common.command.MessageBoxResultCommand;
import de.mogwai.common.command.MessageBoxSetupCommand;
import de.mogwai.common.web.backingbean.WrappingBackingBean;
import de.mogwai.common.web.utils.UpdateModelInfo;

/**
 * BackingBean für Messageboxen.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:15:54 $
 */
public class MessageBoxBackingBean extends WrappingBackingBean<MessageBoxDataModel> {

    @Override
    protected MessageBoxDataModel createDataModel() {
        return new MessageBoxDataModel();
    }

    public String ok() {

        return returnToCallbackTarget((Object) null);
    }

    @Override
    public void updateModel(UpdateModelInfo aInfo) {

        super.updateModel(aInfo);

        Command theCommand = aInfo.getCommand();
        if (theCommand instanceof MessageBoxSetupCommand) {
            MessageBoxSetupCommand theSetupCommand = (MessageBoxSetupCommand) theCommand;

            MessageBoxDataModel theDataModel = getData();

            theDataModel.setMessage(theSetupCommand.getMessage());
            theDataModel.setTitle(theSetupCommand.getTitle());
        }
    }

    @Override
    protected String returnToCallbackTarget(Object aValue) {

        forceUpdateOfBean(lastUpdateModelInfo.getSenderQueueID(), new MessageBoxResultCommand<Object>());

        return lastUpdateModelInfo.getCallerViewId();
    }
}
