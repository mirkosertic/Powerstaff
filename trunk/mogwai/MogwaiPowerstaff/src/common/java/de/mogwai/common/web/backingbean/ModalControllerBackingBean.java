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

import de.mogwai.common.command.UpdateModelCommand;
import de.mogwai.common.web.component.layout.ModalComponent;
import de.mogwai.common.web.navigation.ModalPageDescriptor;
import de.mogwai.common.web.navigation.PageDescriptor;
import de.mogwai.common.web.utils.ShutdownModalDialogCommand;
import de.mogwai.common.web.utils.StartModalDialogCommand;
import de.mogwai.common.web.utils.UpdateModelInfo;
import de.mogwai.common.web.utils.ValidateModalDataCommand;

/**
 * Controller Backing - Bean für modale Dialoge.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:17:08 $
 */
public class ModalControllerBackingBean extends BackingBean {

    private BackingBean modalInitiator;

    private ModalPageDescriptor currentModalPageDescriptor;

    private ModalComponent modalComponent;

    @Override
    public void updateModel(UpdateModelInfo aInfo) {
        super.updateModel(aInfo);

        UpdateModelCommand theCommand = aInfo.getCommand();

        if (theCommand instanceof StartModalDialogCommand) {
            StartModalDialogCommand theStartCommand = (StartModalDialogCommand) theCommand;
            currentModalPageDescriptor = theStartCommand.getPageDescriptor();
            modalInitiator = theStartCommand.getInitiator();
        }

        if (theCommand instanceof ShutdownModalDialogCommand) {
            currentModalPageDescriptor = null;
            modalInitiator = null;
        }

        if (theCommand instanceof ValidateModalDataCommand) {
            ValidateModalDataCommand theValidateCommand = (ValidateModalDataCommand) theCommand;
            if (currentModalPageDescriptor != null) {

                currentModalPageDescriptor.validate(FacesContext.getCurrentInstance(), FacesContext
                        .getCurrentInstance().getViewRoot(), theValidateCommand.getData());

                theValidateCommand.setValidated(true);
            }
        }

        modalComponent.setRendered(currentModalPageDescriptor != null);
    }

    public BackingBean getModalInitiator() {
        return modalInitiator;
    }

    /**
     * Überprüfung, ob der Modale dialog, gebunden an dieses Backing - Bean,
     * angezeigt werden soll oder nicht.
     * 
     * @return true wenn der modale dialog angezeigt werden soll, sonst false
     */
    public boolean isModalDialog() {
        return currentModalPageDescriptor != null;
    }

    /**
     * Getter für den Page - Descriptor des aktuellen modalen Dialoges.
     * 
     * @return den Page - Descriptor für den aktuellen modelen Dialog
     */
    public PageDescriptor getCurrentModalPageDescriptor() {
        return currentModalPageDescriptor;
    }

    /**
     * @return the modalComponent
     */
    public ModalComponent getModalComponent() {
        return modalComponent;
    }

    /**
     * @param modalComponent
     *                the modalComponent to set
     */
    public void setModalComponent(ModalComponent modalComponent) {
        this.modalComponent = modalComponent;
    }
}
