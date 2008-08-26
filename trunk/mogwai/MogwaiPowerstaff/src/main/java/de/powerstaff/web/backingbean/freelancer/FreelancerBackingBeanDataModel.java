package de.powerstaff.web.backingbean.freelancer;

import java.util.List;
import java.util.Vector;

import de.mogwai.common.utils.KeyValuePair;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerContact;
import de.powerstaff.business.entity.FreelancerHistory;
import de.powerstaff.business.entity.FreelancerProfile;
import de.powerstaff.web.backingbean.NavigatingBackingBeanDataModel;
import de.powerstaff.web.utils.Comparators;

public class FreelancerBackingBeanDataModel extends NavigatingBackingBeanDataModel<Freelancer> {

    private ContactType newContactType;

    private String newContactValue;

    private String newHistoryEntry;

    private CollectionDataModel<FreelancerContact> contacts;

    private CollectionDataModel<FreelancerHistory> history;

    private CollectionDataModel<GenericSearchResult> searchResult = new CollectionDataModel<GenericSearchResult>();

    private CollectionDataModel<FreelancerProfile> profiles = new CollectionDataModel<FreelancerProfile>();

    private List<ContactType> contactTypes;

    private List partnerList;

    private List status = new Vector();

    public FreelancerBackingBeanDataModel() {
        status.add(new KeyValuePair<Integer, String>(1, "Angestellter"));
        status.add(new KeyValuePair<Integer, String>(2, "Freiberufler"));
        status.add(new KeyValuePair<Integer, String>(null, "Undefiniert"));
    }

    public FreelancerBackingBeanDataModel(Freelancer aFreelancer) {
        super(aFreelancer);
    }

    @Override
    protected void initialize() {
        setEntity(new Freelancer());
    }

    @Override
    public void setEntity(Freelancer aValue) {
        super.setEntity(aValue);
        newContactType = null;
        newContactValue = null;
        contacts = new CollectionDataModel<FreelancerContact>(getEntity().getContacts());
        contacts.sort(Comparators.CONTACTCOMPARATOR);
        history = new CollectionDataModel<FreelancerHistory>(getEntity().getHistory());
        history.sort(Comparators.INVERSECREATIONDATECOMPARATOR);
    }

    public CollectionDataModel<FreelancerContact> getContacts() {
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
    public CollectionDataModel<FreelancerHistory> getHistory() {
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

    /**
     * @return the partnerList
     */
    public List getPartnerList() {
        return partnerList;
    }

    /**
     * @param partnerList
     *                the partnerList to set
     */
    public void setPartnerList(List partnerList) {
        this.partnerList = partnerList;
    }

    /**
     * @return the profiles
     */
    public CollectionDataModel<FreelancerProfile> getProfiles() {
        return profiles;
    }

    /**
     * @return the status
     */
    public List getStatus() {
        return status;
    }
}
