package de.powerstaff.web.backingbean.customer;

import java.util.List;

import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.Customer;
import de.powerstaff.business.entity.CustomerContact;
import de.powerstaff.business.entity.CustomerHistory;
import de.powerstaff.web.backingbean.NavigatingBackingBeanDataModel;
import de.powerstaff.web.utils.Comparators;

public class CustomerBackingBeanDataModel extends NavigatingBackingBeanDataModel<Customer> {

    private ContactType newContactType;

    private String newContactValue;

    private String newHistoryEntry;

    private CollectionDataModel<CustomerContact> contacts;

    private CollectionDataModel<CustomerHistory> history;

    private CollectionDataModel<Customer> searchResult = new CollectionDataModel<Customer>();

    private List<ContactType> contactTypes;

    public CustomerBackingBeanDataModel() {

    }

    public CustomerBackingBeanDataModel(Customer aCustomer) {
        super(aCustomer);
    }

    @Override
    protected void initialize() {
        setEntity(new Customer());
    }

    @Override
    public void setEntity(Customer aValue) {
        super.setEntity(aValue);
        contacts = new CollectionDataModel<CustomerContact>(aValue.getContacts());
        contacts.sort(Comparators.CONTACTCOMPARATOR);
        history = new CollectionDataModel<CustomerHistory>(aValue.getHistory());
        newContactType = null;
        newContactValue = null;
    }

    public CollectionDataModel<CustomerContact> getContacts() {
        return contacts;
    }

    /**
     * @return the newContactType
     */
    public ContactType getNewContactType() {
        return newContactType;
    }

    /**
     * @param newContactType
     *                the newContactType to set
     */
    public void setNewContactType(ContactType newContactType) {
        this.newContactType = newContactType;
    }

    /**
     * @return the newContactValue
     */
    public String getNewContactValue() {
        return newContactValue;
    }

    /**
     * @param newContactValue
     *                the newContactValue to set
     */
    public void setNewContactValue(String newContactValue) {
        this.newContactValue = newContactValue;
    }

    /**
     * @return the contactTypes
     */
    public List<ContactType> getContactTypes() {
        return contactTypes;
    }

    /**
     * @param contactTypes
     *                the contactTypes to set
     */
    public void setContactTypes(List<ContactType> contactTypes) {
        this.contactTypes = contactTypes;
    }

    /**
     * @return the history
     */
    public CollectionDataModel<CustomerHistory> getHistory() {
        return history;
    }

    /**
     * @return the searchResult
     */
    public CollectionDataModel<Customer> getSearchResult() {
        return searchResult;
    }

    /**
     * @return the newHistoryEntry
     */
    public String getNewHistoryEntry() {
        return newHistoryEntry;
    }

    /**
     * @param newHistoryEntry
     *                the newHistoryEntry to set
     */
    public void setNewHistoryEntry(String newHistoryEntry) {
        this.newHistoryEntry = newHistoryEntry;
    }
}
