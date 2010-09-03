/**
 * Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.powerstaff.web.backingbean.customer;

import de.mogwai.common.command.EditEntityCommand;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.mogwai.common.web.utils.UpdateModelInfo;
import de.powerstaff.business.entity.Customer;
import de.powerstaff.business.entity.CustomerContact;
import de.powerstaff.business.entity.HistoryEntity;
import de.powerstaff.business.service.CustomerService;
import de.powerstaff.web.backingbean.PersonEditorBackingBean;
import de.powerstaff.web.backingbean.project.ProjectBackingBean;

public class CustomerBackingBean extends PersonEditorBackingBean<Customer, CustomerBackingBeanDataModel, CustomerService> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1019229554217528125L;

	@Override
    protected CustomerBackingBeanDataModel createDataModel() {
        return new CustomerBackingBeanDataModel();
    }

    public String commandNewProject() {

        Customer theCustomer = getData().getEntity();
        if (theCustomer.getId() == null) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_KEINKUNDE);
            return null;
        }

        forceUpdateOfBean(ProjectBackingBean.class, new EditEntityCommand<Customer>(theCustomer));
        return "PROJEKT_STAMMDATEN";
    }

    @Override
    public void updateModel(UpdateModelInfo aInfo) {
        super.updateModel(aInfo);
        if (aInfo.getCommand() instanceof EditEntityCommand) {
            EditEntityCommand<Customer> theCommand = (EditEntityCommand<Customer>) aInfo.getCommand();
            init();
            Customer theEntity = (Customer) entityService.findByPrimaryKey(theCommand.getValue().getId());
            getData().setEntity(theEntity);

            afterNavigation();
        }
    }

    @Override
    protected Customer createNew() {
        return new Customer();
    }

    @Override
    protected CustomerContact createNewContact() {
        return new CustomerContact();
    }

    @Override
    protected HistoryEntity createNewHistory() {
        return new HistoryEntity();
    }
}
