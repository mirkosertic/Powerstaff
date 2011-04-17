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
package de.mogwai.common.web.backingbean;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import de.mogwai.common.command.ResetNavigationInfo;
import de.mogwai.common.command.UpdateModelCommand;
import de.mogwai.common.utils.Initable;
import de.mogwai.common.utils.Navigatable;
import de.mogwai.common.web.utils.Updateable;
import de.mogwai.common.web.utils.Validatable;

/**
 * Oberklasse für alle Backing Beans.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:17:10 $
 */
public abstract class BackingBean implements Initable, Navigatable, Updateable,
		Validatable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6152673765217442475L;

	private boolean initialized;

	/**
	 * Refresh Action - Methode.
	 * 
	 * Kann überschrieben werden, um ein spezialisiertes Verhalten beim Refresh
	 * zu erzeugen. Per Default macht diese Methode nichts.
	 * 
	 * @return das SUCCESS outcome.
	 */
	public String refresh() {
		return ActionOutcome.SUCCESS.value();
	}

	/**
	 * @see de.mogwai.common.utils.Initable#init()
	 */
	public void init() {

		initialized = true;
	}

	/**
	 * @see de.mogwai.common.utils.Initable#forceInitialization()
	 */
	public void forceInitialization() {
		initialized = false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean aStatus) {
		initialized = aStatus;
	}

	public void resetNavigation() {
		forceInitialization();
	}

	public void updateModel(UpdateModelCommand aInfo) {
	}

    public boolean validate(FacesContext aContext) {
		return true;
	}
}