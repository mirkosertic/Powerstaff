package de.powerstaff.web.backingbean.project;

import java.util.List;
import java.util.Vector;

import de.mogwai.common.utils.KeyValuePair;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.entity.CustomerContact;
import de.powerstaff.business.entity.Project;
import de.powerstaff.web.backingbean.NavigatingBackingBeanDataModel;

public class ProjectBackingBeanDataModel extends NavigatingBackingBeanDataModel<Project> {

    private CollectionDataModel<GenericSearchResult> searchResult = new CollectionDataModel<GenericSearchResult>();

    private List status = new Vector();
    
    private CollectionDataModel<CustomerContact> contacts;

    public ProjectBackingBeanDataModel() {
        status.add(new KeyValuePair<Integer, String>(1, "Offen"));
        status.add(new KeyValuePair<Integer, String>(2, "Verloren"));
        status.add(new KeyValuePair<Integer, String>(3, "Canceled"));
        status.add(new KeyValuePair<Integer, String>(4, "Besetzt"));
        status.add(new KeyValuePair<Integer, String>(5, "Search zu"));
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
        if (aValue.getCustomer() != null) {
            contacts = new CollectionDataModel<CustomerContact>(getEntity().getCustomer().getContacts());
        } else {
            contacts = new CollectionDataModel<CustomerContact>();
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
     * @param status the status to set
     */
    public void setStatus(List status) {
        this.status = status;
    }

    /**
     * @return the contacts
     */
    public CollectionDataModel<CustomerContact> getContacts() {
        return contacts;
    }

    /**
     * @param contacts the contacts to set
     */
    public void setContacts(CollectionDataModel<CustomerContact> contacts) {
        this.contacts = contacts;
    }
}
