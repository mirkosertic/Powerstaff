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
 * Setupcommand für die MessageMoxBackingBean.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:23:50 $
 */
public class MessageBoxSetupCommand extends UpdateModelCommand {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6615681268857465213L;

	private String title;

    private String message;

    public MessageBoxSetupCommand(String aTitle, String aMessage) {
        title = aTitle;
        message = aMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}