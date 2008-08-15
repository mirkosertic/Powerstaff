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

import de.mogwai.common.command.ResetNavigationInfo;
import de.mogwai.common.web.navigation.MultiPageNavigationDataModel;
import de.mogwai.common.web.navigation.PageDescriptor;

/**
 * Backing bean für Seiten mit dynamischen Inhalt.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:03:27 $
 * @param <T>
 *                Typ des Datenmodells, welches an die Backingbean gebunden wird
 */
public abstract class MultiPageNavigationBackingBean<T extends BackingBeanDataModel> extends WrappingBackingBean<T> {

    protected MultiPageNavigationDataModel pageModel = new MultiPageNavigationDataModel();

    public MultiPageNavigationDataModel getPageModel() {
        return pageModel;
    }

    public void setPageModel(MultiPageNavigationDataModel pageModel) {
        this.pageModel = pageModel;
    }

    public String gotoNextPage() {

        if (pageModel.getOverrideDescriptor() != null) {

            pageModel.setOverrideDescriptor(null);

            jumpToPageModelIndex(0);

            return ActionOutcome.SUCCESS.value();
        }

        int theIndex = pageModel.getIndex();
        if (theIndex < pageModel.getRowCount() - 1) {
            theIndex++;
        }

        jumpToPageModelIndex(theIndex);

        return ActionOutcome.SUCCESS.value();
    }

    public String setSelectedPage() {

        int theSelectedPage = pageModel.getRowIndex();

        jumpToPageModelIndex(theSelectedPage);

        return ActionOutcome.SUCCESS.value();
    }

    public String save() {

        return ActionOutcome.SUCCESS.value();
    }

    public String cancel() {

        return ActionOutcome.SUCCESS.value();
    }

    @Override
    public void resetNavigation(ResetNavigationInfo aInfo) {
        super.resetNavigation(aInfo);

        jumpToPageModelIndex(0);
    }

    public void jumpToPageModelIndex(int aIndex) {
        pageModel.setIndex(aIndex);
    }

    @Override
    public boolean validate(FacesContext aContext) {

        boolean theMainValidationResult = super.validate(aContext);
        if (!theMainValidationResult) {
            return theMainValidationResult;
        }

        try {

            PageDescriptor theDescriptor = pageModel.getCurrentPage();
            if (theDescriptor != null) {
                theDescriptor.validate(aContext, aContext.getViewRoot(), getData());
            }

            return true;

        } catch (ValidatorException aException) {

            aContext.addMessage(aContext.getViewRoot().getClientId(aContext), aException.getFacesMessage());

            return false;
        }
    }
}
