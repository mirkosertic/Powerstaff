package de.powerstaff.web.backingbean.project;

import de.mogwai.common.utils.KeyValuePair;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.entity.Contact;
import de.powerstaff.business.entity.Project;
import de.powerstaff.business.entity.ProjectPosition;
import de.powerstaff.business.entity.SavedProfileSearch;
import de.powerstaff.web.backingbean.NavigatingBackingBeanDataModel;

import java.util.ArrayList;
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

    private String currentType;

    private String currentTypeId;

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
            contacts = new CollectionDataModel<Contact>(getEntity().getContactPerson().getContacts());
        } else {
            contacts = new CollectionDataModel<Contact>();
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
}
