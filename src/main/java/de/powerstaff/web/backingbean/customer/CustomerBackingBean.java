package de.powerstaff.web.backingbean.customer;

import java.util.Collection;

import de.mogwai.common.command.EditEntityCommand;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.mogwai.common.web.utils.UpdateModelInfo;
import de.powerstaff.business.entity.Customer;
import de.powerstaff.business.entity.CustomerContact;
import de.powerstaff.business.entity.CustomerHistory;
import de.powerstaff.business.service.AdditionalDataService;
import de.powerstaff.business.service.CustomerService;
import de.powerstaff.web.backingbean.NavigatingBackingBean;
import de.powerstaff.web.backingbean.project.ProjectBackingBean;
import de.powerstaff.web.utils.Comparators;

public class CustomerBackingBean
		extends
		NavigatingBackingBean<Customer, CustomerBackingBeanDataModel, CustomerService> {

	private AdditionalDataService additinalDataService;

	@Override
	protected CustomerBackingBeanDataModel createDataModel() {
		return new CustomerBackingBeanDataModel();
	}

	/**
	 * @return the additinalDataService
	 */
	public AdditionalDataService getAdditinalDataService() {
		return additinalDataService;
	}

	/**
	 * @param additinalDataService
	 *            the additinalDataService to set
	 */
	public void setAdditinalDataService(
			AdditionalDataService additinalDataService) {
		this.additinalDataService = additinalDataService;
	}

	public String commandSearch() {

		Collection theResult = entityService.performQBESearch(getData()
				.getEntity());

		if (theResult.size() < 1) {
			JSFMessageUtils.addGlobalErrorMessage(MSG_KEINEDATENGEFUNDEN);
			return null;
		}

		if (theResult.size() == 1) {
			getData().setEntity((Customer) theResult.iterator().next());
			return null;
		}

		getData().getSearchResult().setWrappedData(theResult);
		return "CUSTOMER_SEARCHRESULT";
	}

	public void commandAddContact() {

		if ((getData().getNewContactValue() == null)
				|| ("".equals(getData().getNewContactValue()))) {

			JSFMessageUtils.addGlobalErrorMessage(MSG_KEINE_KONTAKTINFOS);

			return;
		}

		CustomerBackingBeanDataModel theModel = getData();

		Customer theFreelancer = theModel.getEntity();

		CustomerContact theContact = new CustomerContact();
		theContact.setType(theModel.getNewContactType());
		theContact.setValue(theModel.getNewContactValue());

		theFreelancer.getContacts().add(theContact);

		theModel.setEntity(theFreelancer);
	}

	public void commandDeleteContact() {

		CustomerBackingBeanDataModel theModel = getData();

		Customer theFreelancer = theModel.getEntity();

		CustomerContact theContact = (CustomerContact) theModel.getContacts()
				.getRowData();
		theFreelancer.getContacts().remove(theContact);

		theModel.setEntity(theFreelancer);
		getData().getHistory().sort(Comparators.INVERSECREATIONDATECOMPARATOR);

		JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGELOESCHT);
	}

	@Override
	public void init() {
		super.init();

		getData().setContactTypes(additinalDataService.getContactTypes());
		commandFirst();
	}

	public String commandBack() {
		return "CUSTOMER_STAMMDATEN";
	}

	public String commandStammdaten() {
		return "CUSTOMER_STAMMDATEN";
	}

	public String commandHistorie() {
		return "CUSTOMER_HISTORIE";
	}

	public void commandAddNewHistoryEntry() {
		CustomerHistory theHistory = new CustomerHistory();
		theHistory.setDescription(getData().getNewHistoryEntry());

		getData().setNewHistoryEntry(null);

		Customer theFreelancer = getData().getEntity();
		getData().getHistory().add(theHistory);
		getData().getHistory().sort(Comparators.INVERSECREATIONDATECOMPARATOR);

		entityService.save(theFreelancer);

		getData().setNewHistoryEntry(null);

		JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGESPEICHERT);
	}

	public String commandSelectSearchResult() {

		getData()
				.setEntity((Customer) getData().getSearchResult().getRowData());
		return "CUSTOMER_STAMMDATEN";
	}

	public void commandDeleteHistoryEntry() {

		CustomerHistory theHistory = (CustomerHistory) getData().getHistory()
				.getRowData();
		getData().getHistory().remove(theHistory);

		Customer theFreelancer = getData().getEntity();
		entityService.save(theFreelancer);

		getData().setEntity(theFreelancer);
		getData().getHistory().sort(Comparators.INVERSECREATIONDATECOMPARATOR);

		JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGELOESCHT);
	}

	public String commandNewProject() {

		Customer theCustomer = getData().getEntity();
		if (theCustomer.getId() == null) {
			JSFMessageUtils.addGlobalErrorMessage(MSG_KEINKUNDE);
			return null;
		}

		forceUpdateOfBean(ProjectBackingBean.class,
				new EditEntityCommand<Customer>(theCustomer));
		return "PROJEKT_STAMMDATEN";
	}

	@Override
	public void updateModel(UpdateModelInfo aInfo) {
		super.updateModel(aInfo);
		if (aInfo.getCommand() instanceof EditEntityCommand) {
			EditEntityCommand<Customer> theCommand = (EditEntityCommand<Customer>) aInfo
					.getCommand();
			init();
			Customer theEntity = (Customer) entityService
					.findByPrimaryKey(theCommand.getValue().getId());
			getData().setEntity(theEntity);

			afterNavigation();
		}
	}

	@Override
	protected Customer createNew() {
		return new Customer();
	}
}
