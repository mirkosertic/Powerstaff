package de.powerstaff.web.backingbean.project;

import de.mogwai.common.utils.KeyValuePair;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.entity.*;
import de.powerstaff.web.backingbean.NavigatingBackingBeanDataModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectBackingBeanDataModel extends NavigatingBackingBeanDataModel<Project> {

    public final static String TYPE_CUSTOMER = "customer";

    public final static String TYPE_PARTNER = "partner";

    public final static String TYPE_UNKNOWN = "unknown";

    private static final long serialVersionUID = -4432968696961074056L;

    private CollectionDataModel<GenericSearchResult> searchResult = new CollectionDataModel<GenericSearchResult>();

    private List<KeyValuePair> status = new ArrayList<KeyValuePair>();

    private CollectionDataModel<Contact> contacts;

    private CollectionDataModel<ProjectPosition> positions = new CollectionDataModel<ProjectPosition>();

    private CollectionDataModel<SavedProfileSearch> savedSearches = new CollectionDataModel<SavedProfileSearch>();

    private CollectionDataModel<ProjectFirstContact> firstContactPositions = new CollectionDataModel<ProjectFirstContact>();

    private String currentType;

    private String currentTypeId;

    private String newFirstContactName1;

    private String newFirstContactName2;

    private String newFirstContactValue;

    private String newFirstContactComment;

    private ContactType newFirstContactType;

    private List<ContactType> contactTypes = new ArrayList<ContactType>();

    public ProjectBackingBeanDataModel() {
        status.add(new KeyValuePair<Integer, String>(1, "Offen"));
        status.add(new KeyValuePair<Integer, String>(2, "Verloren"));
        status.add(new KeyValuePair<Integer, String>(3, "Canceled"));
        status.add(new KeyValuePair<Integer, String>(4, "Besetzt"));
        status.add(new KeyValuePair<Integer, String>(5, "Search zu"));
    }

    public String getSearchResultRowProjectStatus() {
        GenericSearchResult theResult = (GenericSearchResult) searchResult.getRowData();
        Object theStatus = theResult.get("status");
        for (KeyValuePair theST : status) {
            if (theST.getKey().equals(theStatus)) {
                return (String) theST.getValue();
            }
        }
        return null;
    }

    @Override
    protected void initialize() {
        setEntity(new Project());
    }

    @Override
    public void setEntity(Project aValue) {
        super.setEntity(aValue);

        currentType = TYPE_UNKNOWN;
        currentTypeId = NEW_ENTITY_ID;
        if (aValue.getCustomer() != null) {
            currentType = TYPE_CUSTOMER;
            currentTypeId = aValue.getCustomer().getId().toString();
        }
        if (aValue.getPartner() != null) {
            currentType = TYPE_PARTNER;
            currentTypeId = aValue.getPartner().getId().toString();
        }

        if (aValue.getContactPerson() != null) {
            contacts = new CollectionDataModel<Contact>(aValue.getContactPerson().getContacts());
        } else {
            contacts = new CollectionDataModel<Contact>();
        }

        if (aValue.getFirstContacts() != null) {
            firstContactPositions = new CollectionDataModel<ProjectFirstContact>(aValue.getFirstContacts());
        } else {
            firstContactPositions = new CollectionDataModel<ProjectFirstContact>();
        }
    }

    public CollectionDataModel<GenericSearchResult> getSearchResult() {
        return searchResult;
    }

    public List getStatus() {
        return status;
    }

    public void setStatus(List status) {
        this.status = status;
    }

    public CollectionDataModel<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(CollectionDataModel<Contact> contacts) {
        this.contacts = contacts;
    }

    public CollectionDataModel<ProjectPosition> getPositions() {
        return positions;
    }

    public void setPositions(CollectionDataModel<ProjectPosition> positions) {
        this.positions = positions;
    }

    public CollectionDataModel<SavedProfileSearch> getSavedSearches() {
        return savedSearches;
    }

    public void setSavedSearches(CollectionDataModel<SavedProfileSearch> savedSearches) {
        this.savedSearches = savedSearches;
    }

    public String getCurrentType() {
        return currentType;
    }

    public void setCurrentType(String currentType) {
        this.currentType = currentType;
    }

    public String getCurrentTypeId() {
        return currentTypeId;
    }

    public void setCurrentTypeId(String currentTypeId) {
        this.currentTypeId = currentTypeId;
    }

    public void setCustomer(Customer aCustomer) {
        getEntity().setCustomer(aCustomer);
        contacts = new CollectionDataModel<Contact>(getEntity().getContactPerson().getContacts());
    }

    public void setPartner(Partner aPartner) {
        getEntity().setPartner(aPartner);
        contacts = new CollectionDataModel<Contact>(getEntity().getContactPerson().getContacts());
    }

    public CollectionDataModel<ProjectFirstContact> getFirstContactPositions() {
        return firstContactPositions;
    }

    public void setFirstContactPositions(CollectionDataModel<ProjectFirstContact> firstContactPositions) {
        this.firstContactPositions = firstContactPositions;
    }

    public String getNewFirstContactName1() {
        return newFirstContactName1;
    }

    public void setNewFirstContactName1(String newFirstContactName1) {
        this.newFirstContactName1 = newFirstContactName1;
    }

    public String getNewFirstContactName2() {
        return newFirstContactName2;
    }

    public void setNewFirstContactName2(String newFirstContactName2) {
        this.newFirstContactName2 = newFirstContactName2;
    }

    public String getNewFirstContactValue() {
        return newFirstContactValue;
    }

    public void setNewFirstContactValue(String newFirstContactValue) {
        this.newFirstContactValue = newFirstContactValue;
    }

    public ContactType getNewFirstContactType() {
        return newFirstContactType;
    }

    public void setNewFirstContactType(ContactType newFirstContactType) {
        this.newFirstContactType = newFirstContactType;
    }

    public List<ContactType> getContactTypes() {
        return contactTypes;
    }

    public void setContactTypes(List<ContactType> contactTypes) {
        this.contactTypes = contactTypes;
    }

    public String getNewFirstContactComment() {
        return newFirstContactComment;
    }

    public void setNewFirstContactComment(String newFirstContactComment) {
        this.newFirstContactComment = newFirstContactComment;
    }
}
