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
package de.powerstaff.web.backingbean;

import java.util.List;

import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.HistoryType;
import de.powerstaff.business.entity.Person;
import de.powerstaff.business.entity.FreelancerContact;
import de.powerstaff.business.entity.FreelancerHistory;
import de.powerstaff.web.utils.Comparators;

public abstract class PersonEditorBackingBeanDataModel<T extends Person> extends NavigatingBackingBeanDataModel<T> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7341944583550217823L;

	private ContactType newContactType;

    private String newContactValue;

    private String newHistoryEntry;
    
    private HistoryType newHistoryType;

    private CollectionDataModel<FreelancerContact> contacts;

    private CollectionDataModel<FreelancerHistory> history;

    private CollectionDataModel<GenericSearchResult> searchResult = new CollectionDataModel<GenericSearchResult>();

    private List<ContactType> contactTypes;
    
    private List<HistoryType> historyTypes;

    protected PersonEditorBackingBeanDataModel() {
    }

    protected PersonEditorBackingBeanDataModel(T aEntity) {
        super(aEntity);
    }

    @Override
    public void setEntity(T entity) {
        super.setEntity(entity);

        newContactType = null;
        newContactValue = null;
        contacts = new CollectionDataModel<FreelancerContact>(getEntity().getContacts());
        contacts.sort(Comparators.CONTACTCOMPARATOR);
        history = new CollectionDataModel<FreelancerHistory>(getEntity().getHistory());
        history.sort(Comparators.INVERSECREATIONDATECOMPARATOR);
    }

    /**
     * @return the newContactType
     */
    public ContactType getNewContactType() {
        return newContactType;
    }

    /**
     * @param newContactType the newContactType to set
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
     * @param newContactValue the newContactValue to set
     */
    public void setNewContactValue(String newContactValue) {
        this.newContactValue = newContactValue;
    }

    /**
     * @return the newHistoryEntry
     */
    public String getNewHistoryEntry() {
        return newHistoryEntry;
    }

    /**
     * @param newHistoryEntry the newHistoryEntry to set
     */
    public void setNewHistoryEntry(String newHistoryEntry) {
        this.newHistoryEntry = newHistoryEntry;
    }

    /**
     * @return the contacts
     */
    public CollectionDataModel<FreelancerContact> getContacts() {
        return contacts;
    }

    /**
     * @param contacts the contacts to set
     */
    public void setContacts(CollectionDataModel<FreelancerContact> contacts) {
        this.contacts = contacts;
    }

    /**
     * @return the history
     */
    public CollectionDataModel<FreelancerHistory> getHistory() {
        return history;
    }

    /**
     * @param history the history to set
     */
    public void setHistory(CollectionDataModel<FreelancerHistory> history) {
        this.history = history;
    }

    /**
     * @return the searchResult
     */
    public CollectionDataModel<GenericSearchResult> getSearchResult() {
        return searchResult;
    }

    /**
     * @param searchResult the searchResult to set
     */
    public void setSearchResult(CollectionDataModel<GenericSearchResult> searchResult) {
        this.searchResult = searchResult;
    }

    /**
     * @return the contactTypes
     */
    public List<ContactType> getContactTypes() {
        return contactTypes;
    }

    /**
     * @param contactTypes the contactTypes to set
     */
    public void setContactTypes(List<ContactType> contactTypes) {
        this.contactTypes = contactTypes;
    }

    /**
     * @return the historyTypes
     */
    public List<HistoryType> getHistoryTypes() {
        return historyTypes;
    }

    /**
     * @param historyTypes the historyTypes to set
     */
    public void setHistoryTypes(List<HistoryType> historyTypes) {
        this.historyTypes = historyTypes;
    }

    /**
     * @return the historyType
     */
    public HistoryType getNewHistoryType() {
        return newHistoryType;
    }

    /**
     * @param historyType the historyType to set
     */
    public void setNewHistoryType(HistoryType historyType) {
        this.newHistoryType = historyType;
    }
}
