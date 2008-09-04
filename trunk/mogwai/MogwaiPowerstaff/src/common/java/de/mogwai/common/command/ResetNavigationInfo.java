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
package de.mogwai.common.command;

/**
 * Navigation helper class.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:23:49 $
 */
public class ResetNavigationInfo {

    private ResetNavigationCommand command;

    private String callerViewId;

    public ResetNavigationInfo(String aCallerViewId, ResetNavigationCommand aCommand) {
        command = aCommand;
        callerViewId = aCallerViewId;
    }

    public String getCallerViewId() {
        return callerViewId;
    }

    public void setCallerViewId(String callerViewId) {
        this.callerViewId = callerViewId;
    }

    public ResetNavigationCommand getCommand() {
        return command;
    }

    public void setCommand(ResetNavigationCommand command) {
        this.command = command;
    }
}
