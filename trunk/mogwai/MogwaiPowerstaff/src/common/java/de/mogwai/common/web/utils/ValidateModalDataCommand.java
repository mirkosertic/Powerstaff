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

import de.mogwai.common.command.UpdateModelCommand;

/**
 * Command zum Validieren des aktuellen modalen Dialoges.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:16:11 $
 */
public class ValidateModalDataCommand extends UpdateModelCommand {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8474084667155022478L;

	private Object data;

    private boolean validated;

    public ValidateModalDataCommand(Object aData) {
        data = aData;
    }

    public Object getData() {
        return data;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
