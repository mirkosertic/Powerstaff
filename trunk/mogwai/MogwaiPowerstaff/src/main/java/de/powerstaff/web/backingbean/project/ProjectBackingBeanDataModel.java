package de.powerstaff.web.backingbean.project;

import java.util.ArrayList;
import java.util.List;

import de.mogwai.common.utils.KeyValuePair;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.entity.Contact;
import de.powerstaff.business.entity.Project;
import de.powerstaff.web.backingbean.NavigatingBackingBeanDataModel;

public class ProjectBackingBeanDataModel extends NavigatingBackingBeanDataModel<Project> {

    private CollectionDataModel<GenericSearchResult> searchResult = new CollectionDataModel<GenericSearchResult>();

    private List<KeyValuePair> status = new ArrayList<KeyValuePair>();

    private CollectionDataModel<Contact> contacts;

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

    public ProjectBackingBeanDataModel(Project aProject) {
        super(aProject);
    }

    @Override
    protected void initialize() {
        setEntity(new Project());
    }

    @Override
    public void setEntity(Project aValue) {
        super.setEntity(aValue);
        if (aValue.getContactPerson() != null) {
            contacts = new CollectionDataModel<Contact>(getEntity().getContactPerson().getContacts());
        } else {
            contacts = new CollectionDataModel<Contact>();
        }
    }

    /**
     * @return the searchResult
     */
    public CollectionDataModel<GenericSearchResult> getSearchResult() {
        return searchResult;
    }

    /**
     * @return the status
     */
    public List getStatus() {
        return status;
    }

    /**
     * @param status
     *                the status to set
     */
    public void setStatus(List status) {
        this.status = status;
    }

    /**
     * @return the contacts
     */
    public CollectionDataModel<Contact> getContacts() {
        return contacts;
    }

    /**
     * @param contacts
     *                the contacts to set
     */
    public void setContacts(CollectionDataModel<Contact> contacts) {
        this.contacts = contacts;
    }
}
