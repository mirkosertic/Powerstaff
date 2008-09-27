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

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.entity.Contact;
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.HistoryEntity;
import de.powerstaff.business.entity.Person;
import de.powerstaff.business.service.AdditionalDataService;
import de.powerstaff.business.service.PersonService;
import de.powerstaff.business.service.TooManySearchResults;
import de.powerstaff.web.utils.Comparators;

public abstract class PersonEditorBackingBean<T extends Person, V extends PersonEditorBackingBeanDataModel<T>, S extends PersonService<T>> extends NavigatingBackingBean<T, V, S> {

    private AdditionalDataService additinalDataService;
    
    /**
     * @return the additinalDataService
     */
    public AdditionalDataService getAdditinalDataService() {
        return additinalDataService;
    }

    /**
     * @param additinalDataService
     *                the additinalDataService to set
     */
    public void setAdditinalDataService(AdditionalDataService additinalDataService) {
        this.additinalDataService = additinalDataService;
    }

    @Override
    public void init() {
        super.init();

        getData().setContactTypes((List<ContactType>) additinalDataService.getContactTypes());

        commandNew();
    }
    
    public String commandSearch() {

        Collection<GenericSearchResult> theResult = null;
        try {
            String theContactValue = getData().getNewContactValue();
            ContactType theContactType = getData().getNewContactType();
            if (!StringUtils.isEmpty(theContactValue) && (theContactType != null)) {
                theResult = entityService.performSearchByContact(theContactValue, theContactType);
            } else {
                theResult = entityService.performQBESearch(getData().getEntity());
            }
        } catch (TooManySearchResults e) {
            theResult = e.getResult();
            JSFMessageUtils.addGlobalErrorMessage(MSG_ZUVIELESUCHERGEBNISSE);            
        }

        if (theResult.size() < 1) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_KEINEDATENGEFUNDEN);
            return null;
        }

        if (theResult.size() == 1) {

            GenericSearchResult theResult2 = (GenericSearchResult) theResult.iterator().next();
            getData().setEntity(entityService.findByPrimaryKey((Long) theResult2.get(GenericSearchResult.OBJECT_ID_KEY)));
            
            afterNavigation();
            return null;
        }

        getData().getSearchResult().setWrappedData(theResult);
        return "SEARCHRESULT";
    }
    
    protected abstract Contact createNewContact();
    
    protected abstract HistoryEntity createNewHistory();

    public void commandAddContact() {

        if (StringUtils.isEmpty(getData().getNewContactValue())) {

            JSFMessageUtils.addGlobalErrorMessage(MSG_KEINE_KONTAKTINFOS);
            return;
        }

        PersonEditorBackingBeanDataModel<T> theModel = getData();

        T thePerson = theModel.getEntity();

        Contact theContact = createNewContact();
        theContact.setType(theModel.getNewContactType());
        theContact.setValue(theModel.getNewContactValue());

        thePerson.getContacts().add(theContact);
        
        theModel.setEntity(thePerson);
    }

    public void commandDeleteContact() {

        PersonEditorBackingBeanDataModel<T> theModel = getData();

        T thePerson = theModel.getEntity();

        Contact theContact = (Contact) theModel.getContacts().getRowData();
        thePerson.getContacts().remove(theContact);

        theModel.setEntity(thePerson);
    }  
    
    public void commandAddNewHistoryEntry() {
        
        HistoryEntity theHistory = createNewHistory();
        theHistory.setDescription(getData().getNewHistoryEntry());

        T thePerson = getData().getEntity();
        thePerson.getHistory().add(theHistory);

        entityService.save(thePerson);

        getData().setNewHistoryEntry(null);
        getData().setEntity(thePerson);

        JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGESPEICHERT);
    }   
    
    public void commandDeleteHistoryEntry() {

        HistoryEntity theHistory = (HistoryEntity) getData().getHistory().getRowData();
        getData().getHistory().remove(theHistory);

        T thePerson = getData().getEntity();
        entityService.save(thePerson);

        getData().setEntity(thePerson);
        getData().getHistory().sort(Comparators.INVERSECREATIONDATECOMPARATOR);

        JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGELOESCHT);
    }
    
    public String commandSelectSearchResult() {

        GenericSearchResult theResult = (GenericSearchResult) getData().getSearchResult().getRowData();
        T theEntity = entityService.findByPrimaryKey((Long) theResult.get(GenericSearchResult.OBJECT_ID_KEY));
        getData().setEntity(theEntity);
        afterNavigation();
        return "STAMMDATEN";
    }    
    
    public String commandBack() {
        return "STAMMDATEN";
    }

    public String commandStammdaten() {
        return "STAMMDATEN";
    }

    public String commandHistorie() {
        return "HISTORIE";
    }
}