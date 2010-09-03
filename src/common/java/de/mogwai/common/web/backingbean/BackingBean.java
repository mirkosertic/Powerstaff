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

import de.mogwai.common.command.CallbackResultCommand;
import de.mogwai.common.command.Command;
import de.mogwai.common.command.MessageBoxResultCommand;
import de.mogwai.common.command.MessageBoxSetupCommand;
import de.mogwai.common.command.ResetNavigationCommand;
import de.mogwai.common.command.ResetNavigationInfo;
import de.mogwai.common.command.UpdateModelCommand;
import de.mogwai.common.exception.UserMessageException;
import de.mogwai.common.utils.Initable;
import de.mogwai.common.utils.Navigatable;
import de.mogwai.common.web.navigation.ModalPageDescriptor;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.mogwai.common.web.utils.NavigationUtils;
import de.mogwai.common.web.utils.ShutdownModalDialogCommand;
import de.mogwai.common.web.utils.StartModalDialogCommand;
import de.mogwai.common.web.utils.UpdateModelInfo;
import de.mogwai.common.web.utils.Updateable;
import de.mogwai.common.web.utils.Validatable;

/**
 * Oberklasse für alle Backing Beans.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:17:10 $
 */
public abstract class BackingBean implements Initable, Navigatable, Updateable, Validatable, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6152673765217442475L;

	private boolean initialized;

    private String queueID = getClass().getName();

    // This holds information about the last view who causes this bean to update
    // its model
    protected UpdateModelInfo lastUpdateModelInfo;

    /**
     * Behandelt <code>UserMessageException</code>. Die Message wird
     * lokalisert in den <code>FacesContext</code> des aktuellen Requests
     * gestellt.
     * 
     * @param e
     *                zu behandelnde UserMessageException.
     */
    protected void handle(UserMessageException e) {
        JSFMessageUtils.addGlobalInfoMessage(e.getMessageKey(), e.getReplacementValues());
    }

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

    /**
     * {@inheritDoc}
     */
    public void resetNavigation(ResetNavigationInfo aInfo) {
        forceInitialization();

        lastUpdateModelInfo = null;
    }

    /**
     * {@inheritDoc}
     */
    public void updateModel(UpdateModelInfo aInfo) {

        Command theCommand = aInfo.getCommand();
        if (theCommand instanceof MessageBoxResultCommand) {
            shutdownModalDialog();
        }

        lastUpdateModelInfo = aInfo;
    }

    /**
     * Den Reset der Navigation einer BackingBean forcieren.
     * 
     * @param aBeanClass
     *                Klasse der BackingBean
     * @param aCommand
     *                das Command
     */
    public void forceNavigationResetOfBean(Class aBeanClass, ResetNavigationCommand aCommand) {
        NavigationUtils.getInstance().forceResetNavigationOfBeans(FacesContext.getCurrentInstance(),
                aBeanClass.getName(), aCommand);
    }

    /**
     * Den Reset der Navigation einer BackingBean forcieren.
     * 
     * @param aQueueID
     *                der Name der Queue
     * @param aCommand
     *                das Command
     */
    public void forceNavigationResetOfBean(String aQueueID, ResetNavigationCommand aCommand) {
        NavigationUtils.getInstance()
                .forceResetNavigationOfBeans(FacesContext.getCurrentInstance(), aQueueID, aCommand);
    }

    /**
     * Die Aktualisierung eines Datenmodells einer BackingBean forcieren.
     * 
     * @param aBeanClass
     *                die BackingBean - Klasse
     * @param aCommand
     *                das Kommando
     */
    public void forceUpdateOfBean(Class aBeanClass, UpdateModelCommand aCommand) {
        NavigationUtils.getInstance().forceUpdateOfBeans(this, FacesContext.getCurrentInstance(), aBeanClass.getName(),
                aCommand);
    }

    /**
     * Die Aktualisierung eines Datenmodells einer BackingBean forcieren.
     * 
     * @param aQueueID
     *                die Queue - ID der BackingBean
     * @param aCommand
     *                das Kommando
     */
    public void forceUpdateOfBean(String aQueueID, UpdateModelCommand aCommand) {
        NavigationUtils.getInstance().forceUpdateOfBeans(this, FacesContext.getCurrentInstance(), aQueueID, aCommand);
    }

    /**
     * Den Reset der Navigation einer BackingBean forcieren.
     * 
     * @param aBeanClass
     *                die BackingBean - Klasse
     */
    public void forceNavigationResetOfBean(Class aBeanClass) {
        forceNavigationResetOfBean(aBeanClass, new ResetNavigationCommand());
    }

    /**
     * Den Reset der Navigation einer BackingBean forcieren.
     * 
     * @param aQueueID
     *                die ID der Queue
     */
    public void forceNavigationResetOfBean(String aQueueID) {
        forceNavigationResetOfBean(aQueueID, new ResetNavigationCommand());
    }

    /**
     * Überprüfen, ob die Bean sich aktuell in einem Callback befindet.
     * 
     * @return true wenn ja,sonst false
     */
    protected boolean isCallback() {
        return lastUpdateModelInfo != null;
    }

    /**
     * Wenn diese Bean aus einem Callback heraus gestartet wurde, kann über
     * diese Methode das Outcome ermittelt werden, um zu dem Aufrufer zurück zu
     * springen.
     * 
     * @param aValue
     *                das Argument, das dem Aufrufer als Ergebnis des Callbacks
     *                übergeben wird.
     * @return das Outcome, um zum Aufrufer zurückzuspringen.
     */
    protected String returnToCallbackTarget(Object aValue) {

        forceUpdateOfBean(lastUpdateModelInfo.getSenderQueueID(), new CallbackResultCommand(aValue));

        return lastUpdateModelInfo.getCallerViewId();
    }

    /**
     * Rücksprung zur aufrufenden Bean.
     * 
     * An die aufrufende Bean wird das a.g. Kommando geschickt.
     * 
     * @param aCommand
     *                das Kommando
     * @return die URL zur aufrufenden Seite
     */
    protected String returnToCallbackTarget(UpdateModelCommand aCommand) {

        forceUpdateOfBean(lastUpdateModelInfo.getSenderQueueID(), aCommand);
        return lastUpdateModelInfo.getCallerViewId();
    }

    /**
     * {@inheritDoc}
     */
    public boolean validate(FacesContext aContext) {
        return true;
    }

    /**
     * Starten eines modalen Dialoges.
     * 
     * @param aPageDescriptor
     *                Descriptor für den anzuzeigenden Dialog
     */
    public void launchModalDialog(ModalPageDescriptor aPageDescriptor) {
        forceUpdateOfBean(ModalControllerBackingBean.class, new StartModalDialogCommand(this, aPageDescriptor));
    }

    /**
     * Starten einer modalen MessageBox.
     * 
     * @param aPageDescriptor
     *                der Page - Descriptor
     * @param aBackingBeanClass
     *                die BackingBean - Klasse
     * @param aTitle
     *                der Titel
     * @param aMessage
     *                die Nachricht
     */
    public void launchMessageDialog(ModalPageDescriptor aPageDescriptor, Class aBackingBeanClass, String aTitle,
            String aMessage) {

        forceUpdateOfBean(aBackingBeanClass, new MessageBoxSetupCommand(aTitle, aMessage));
        launchModalDialog(aPageDescriptor);
    }

    /**
     * Beenden des aktuellen, modalen Dialoges.
     */
    public void shutdownModalDialog() {
        forceUpdateOfBean(ModalControllerBackingBean.class, new ShutdownModalDialogCommand());
    }

    /**
     * Ermittlung der Queue - ID der Backing Bean.
     * 
     * Per Default ist die Queue - ID der Name der aktuellen Klasse
     * 
     * @return die Queue - ID
     */
    public String getQueueID() {
        return queueID;
    }

    /**
     * Setzen der Queue - ID der Backing Bean.
     * 
     * @param aQueueID
     *                die neue Queue - ID
     */
    public void setQueueID(String aQueueID) {
        queueID = aQueueID;
    }
}