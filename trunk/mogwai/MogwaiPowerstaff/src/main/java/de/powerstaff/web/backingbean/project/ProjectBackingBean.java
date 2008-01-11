package de.powerstaff.web.backingbean.project;

import java.util.Collection;

import de.mogwai.common.command.EditEntityCommand;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.mogwai.common.web.utils.UpdateModelInfo;
import de.powerstaff.business.entity.Customer;
import de.powerstaff.business.entity.Project;
import de.powerstaff.business.service.ProjectService;
import de.powerstaff.web.backingbean.NavigatingBackingBean;
import de.powerstaff.web.backingbean.customer.CustomerBackingBean;

public class ProjectBackingBean extends
		NavigatingBackingBean<Project, ProjectBackingBeanDataModel, ProjectService> {

	@Override
	protected ProjectBackingBeanDataModel createDataModel() {
		return new ProjectBackingBeanDataModel();
	}

	public String commandSearch() {

		Collection theResult = entityService.performQBESearch(getData().getEntity());

		if (theResult.size() < 1) {
			JSFMessageUtils.addGlobalErrorMessage(MSG_KEINEDATENGEFUNDEN);
			return null;
		}

		if (theResult.size() == 1) {
			getData().setEntity((Project) theResult.iterator().next());
			return null;
		}

		getData().getSearchResult().setWrappedData(theResult);
		return "PROJEKT_SEARCHRESULT";
	}

	@Override
	public void init() {
		super.init();
		commandFirst();
	}


	@Override
	public void commandSave() {
		try {
			if (getData().getEntity().getCustomer() != null) {
				super.commandSave();
			} else {
				JSFMessageUtils.addGlobalErrorMessage(MSG_KEINKUNDE);
			}
		} catch (Exception e) {
			JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIMSPEICHERN, e
					.getMessage());
		}
	}

	public String commandBack() {
		return "PROJEKT_STAMMDATEN";
	}

	public String commandStammdaten() {
		return "PROJEKT_STAMMDATEN";
	}

	public String commandSelectSearchResult() {

		getData().setEntity((Project) getData().getSearchResult().getRowData());
		return "PROJEKT_STAMMDATEN";
	}
	
	public String commandShowCustomer() {
		Customer theCustomer = getData().getEntity().getCustomer();
		if (theCustomer!=null) {
			
			forceUpdateOfBean(CustomerBackingBean.class, new EditEntityCommand<Customer>(theCustomer));			
			return "CUSTOMER_STAMMDATEN";
		}
		
		JSFMessageUtils.addGlobalErrorMessage(MSG_KEINKUNDE);
		return null;
	}
	
	@Override
	public void updateModel(UpdateModelInfo aInfo) {
		super.updateModel(aInfo);
		if (aInfo.getCommand() instanceof EditEntityCommand) {
			EditEntityCommand<Customer> theCommand = (EditEntityCommand<Customer>) aInfo.getCommand();
			
			init();
			
			Project theProject = new Project();
			theProject.setCustomer(theCommand.getValue());
			
			getData().setEntity(theProject);
			afterNavigation();
		}
	}

	@Override
	protected Project createNew() {
		return new Project();
	}
}