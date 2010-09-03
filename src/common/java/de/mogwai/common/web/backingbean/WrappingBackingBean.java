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

import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import de.mogwai.common.web.utils.ValidateModalDataCommand;

/**
 * Basisklasse für eine MVC - Struktur innerhalb der JSF - Anwendung.
 * 
 * Ein Backing - Bean ist der Controller, er reagiert auf Benutzerereignisse.
 * Ein WrappingBackingBean erweitert den Controller um ein Datenmodell, welches
 * die Daten und den aktuellen Zustand der View speichert. Die View sind die JSP -
 * Seiten, welche über das WrappingBackingBean und die Methode getData() auf das
 * Datenmodell zugreifen können und die Daten somit darstellen.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:17:07 $
 * @param <T>
 *                Typ des Datenmodells, welches and die BackingBean gebunden
 *                wird
 */
public abstract class WrappingBackingBean<T extends BackingBeanDataModel> extends BackingBean {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6493790613060807490L;
	private T dataModel;

    /**
     * Constructor.
     * 
     * Dieser Constructor ruft die abstrakte Methode createDataModel auf, welche
     * von Unterklassen des WrappingBackingBean implementiert werden müssen. Das
     * Datenmodell wird über diesen Aufruf somit auf einen Default
     * vorinitialisiert.
     */
    public WrappingBackingBean() {
        dataModel = createDataModel();
    }

    /**
     * Methode zum erzeugen eines neuen Datenmodells.
     * 
     * @return das neu erzeugte Datenmodell
     */
    protected abstract T createDataModel();

    public T getData() {
        return dataModel;
    }

    public void setData(T aDataModel) {
        this.dataModel = aDataModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(FacesContext aContext) {

        ValidateModalDataCommand theCommand = new ValidateModalDataCommand(getData());
        try {
            forceUpdateOfBean(ModalControllerBackingBean.class, theCommand);

            if (theCommand.isValidated()) {
                return true;
            }

        } catch (ValidatorException e) {

            aContext.addMessage(null, e.getFacesMessage());

            return false;

        }

        return super.validate(aContext);
    }

}
