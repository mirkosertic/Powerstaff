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

import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.dao.GenericSearchResult;
import de.powerstaff.business.entity.Contact;
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.HistoryEntity;
import de.powerstaff.business.entity.Person;
import de.powerstaff.business.service.AdditionalDataService;
import de.powerstaff.business.service.OptimisticLockException;
import de.powerstaff.business.service.PersonService;
import de.powerstaff.business.service.TooManySearchResults;
import de.powerstaff.web.utils.Comparators;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;

public abstract class PersonEditorBackingBean<T extends Person, V extends PersonEditorBackingBeanDataModel<T>, S extends PersonService<T>>
        extends NavigatingBackingBean<T, V, S> {

    private static final long serialVersionUID = -6719464867736964872L;

    private AdditionalDataService additinalDataService;

    private XingConnectorBackingBean xingConnectorBackingBean;

    public void setAdditinalDataService(
            AdditionalDataService additinalDataService) {
        this.additinalDataService = additinalDataService;
    }

    public void setXingConnectorBackingBean(XingConnectorBackingBean xingConnectorBackingBean) {
        this.xingConnectorBackingBean = xingConnectorBackingBean;
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();

        getData().setContactTypes(additinalDataService.getContactTypes());
        getData().setHistoryTypes(additinalDataService.getHistoryTypes());

        commandNew();
    }

    public SocialInfo getSocialInfo() {
        SocialInfo theResult = getData().getCurrentSocialInfo();
        if (theResult == null) {
            theResult = xingConnectorBackingBean.locatePersonData(getData().getEntity().getContacts());
            getData().setCurrentSocialInfo(theResult);
        }
        return theResult;
    }


    public String commandSearch() {

        Collection<GenericSearchResult> theResult;
        try {
            String theContactValue = getData().getNewContactValue();
            ContactType theContactType = getData().getNewContactType();
            if (!StringUtils.isEmpty(theContactValue)
                    && (theContactType != null)) {
                theResult = entityService.performSearchByContact(
                        theContactValue, theContactType);
            } else {
                theResult = entityService.performQBESearch(getData()
                        .getEntity());
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

            GenericSearchResult theResult2 = (GenericSearchResult) theResult
                    .iterator().next();
            getData().setEntity(
                    entityService.findByPrimaryKey((Long) theResult2
                            .get(GenericSearchResult.OBJECT_ID_KEY)));

            afterNavigation();
            return "pretty:" + getNavigationIDPrefix() + "main";
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
        theHistory.setType(getData().getNewHistoryType());

        T thePerson = getData().getEntity();
        thePerson.getHistory().add(theHistory);

        try {
            entityService.save(thePerson);

            getData().setNewHistoryEntry(null);
            getData().setEntity(thePerson);

            JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGESPEICHERT);
        } catch (OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }

    public void commandDeleteHistoryEntry() {

        HistoryEntity theHistory = (HistoryEntity) getData().getHistory()
                .getRowData();
        getData().getHistory().remove(theHistory);

        T thePerson = getData().getEntity();
        try {
            entityService.save(thePerson);

            getData().setEntity(thePerson);
            getData().getHistory().sort(Comparators.INVERSECREATIONDATECOMPARATOR);

            JSFMessageUtils.addGlobalInfoMessage(MSG_ERFOLGREICHGELOESCHT);
        } catch (OptimisticLockException e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_CONCURRENTMODIFICATION);
        }
    }
}