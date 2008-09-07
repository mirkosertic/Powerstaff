package de.powerstaff.web.backingbean.partner;

import java.util.List;

import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.Partner;
import de.powerstaff.business.entity.PartnerContact;
import de.powerstaff.business.entity.PartnerHistory;
import de.powerstaff.web.backingbean.NavigatingBackingBeanDataModel;
import de.powerstaff.web.utils.Comparators;

public class PartnerBackingBeanDataModel extends NavigatingBackingBeanDataModel<Partner> {

    private ContactType newContactType;

    private String newContactValue;

    private String newHistoryEntry;

    private CollectionDataModel<PartnerContact> contacts;

    private CollectionDataModel<PartnerHistory> history;
    
    private CollectionDataModel<Freelancer> freelancer;

    private CollectionDataModel<GenericSearchResult> searchResult = new CollectionDataModel<GenericSearchResult>();

    private List<ContactType> contactTypes;
    
    private Freelancer originalFreelancer;
    
    private String codeToAdd;

    public PartnerBackingBeanDataModel() {

    }

    public PartnerBackingBeanDataModel(Partner aPartner) {
        super(aPartner);
    }

    @Override
    protected void initialize() {
        setEntity(new Partner());
    }

    @Override
    public void setEntity(Partner aValue) {
        super.setEntity(aValue);
        contacts = new CollectionDataModel<PartnerContact>(aValue.getContacts());
        contacts.sort(Comparators.CONTACTCOMPARATOR);
        history = new CollectionDataModel<PartnerHistory>(aValue.getHistory());
        history.sort(Comparators.INVERSECREATIONDATECOMPARATOR);
        freelancer = new CollectionDataModel<Freelancer>(aValue.getFreelancer());
        newContactType = null;
        newContactValue = null;
    }

    public CollectionDataModel<PartnerContact> getContacts() {
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
    public CollectionDataModel<PartnerHistory> getHistory() {
        return history;
    }

    /**
     * @return the searchResult
     */
    public CollectionDataModel<GenericSearchResult> getSearchResult() {
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

    public Freelancer getOriginalFreelancer() {
        return originalFreelancer;
    }

    public void setOriginalFreelancer(Freelancer originalFreelancer) {
        this.originalFreelancer = originalFreelancer;
    }

    /**
     * @return the freelancer
     */
    public CollectionDataModel<Freelancer> getFreelancer() {
        return freelancer;
    }

    /**
     * @param freelancer the freelancer to set
     */
    public void setFreelancer(CollectionDataModel<Freelancer> freelancer) {
        this.freelancer = freelancer;
    }

    /**
     * @param contacts the contacts to set
     */
    public void setContacts(CollectionDataModel<PartnerContact> contacts) {
        this.contacts = contacts;
    }

    /**
     * @param history the history to set
     */
    public void setHistory(CollectionDataModel<PartnerHistory> history) {
        this.history = history;
    }

    /**
     * @param searchResult the searchResult to set
     */
    public void setSearchResult(CollectionDataModel<GenericSearchResult> searchResult) {
        this.searchResult = searchResult;
    }

    /**
     * @return the codeToAdd
     */
    public String getCodeToAdd() {
        return codeToAdd;
    }

    /**
     * @param codeToAdd the codeToAdd to set
     */
    public void setCodeToAdd(String codeToAdd) {
        this.codeToAdd = codeToAdd;
    }
}