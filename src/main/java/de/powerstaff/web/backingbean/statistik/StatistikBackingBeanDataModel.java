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
package de.powerstaff.web.backingbean.statistik;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.faces.component.UIComponent;

import de.mogwai.common.web.backingbean.BackingBeanDataModel;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.dto.KontakthistorieEntry;
import de.powerstaff.business.entity.User;

public class StatistikBackingBeanDataModel extends BackingBeanDataModel {

    private transient UIComponent viewRoot;
    
    private Date datumVon;
    
    private Date datumBis;
    
    private User benutzer;
    
    private CollectionDataModel<KontakthistorieEntry> searchResult = new CollectionDataModel<KontakthistorieEntry>(
            new Vector<KontakthistorieEntry>());
    
    private List<User> benutzerListe;

    public StatistikBackingBeanDataModel() {
        Date today = new Date();
        datumBis = today;
        
        Calendar theCalendar = Calendar.getInstance();
        theCalendar.setTime(today);
        theCalendar.add(Calendar.MONTH, -1);
        datumVon = theCalendar.getTime();
    }
    

    /**
     * @return the viewRoot
     */
    public UIComponent getViewRoot() {
        return viewRoot;
    }

    /**
     * @param viewRoot
     *                the viewRoot to set
     */
    public void setViewRoot(UIComponent viewRoot) {
        this.viewRoot = viewRoot;
    }

    /**
     * @return the datumVon
     */
    public Date getDatumVon() {
        return datumVon;
    }

    /**
     * @param datumVon the datumVon to set
     */
    public void setDatumVon(Date datumVon) {
        this.datumVon = datumVon;
    }

    /**
     * @return the datumBis
     */
    public Date getDatumBis() {
        return datumBis;
    }

    /**
     * @param datumBis the datumBis to set
     */
    public void setDatumBis(Date datumBis) {
        this.datumBis = datumBis;
    }

    /**
     * @return the benutzer
     */
    public User getBenutzer() {
        return benutzer;
    }

    /**
     * @param benutzer the benutzer to set
     */
    public void setBenutzer(User benutzer) {
        this.benutzer = benutzer;
    }

    /**
     * @return the searchResult
     */
    public CollectionDataModel<KontakthistorieEntry> getSearchResult() {
        return searchResult;
    }

    /**
     * @param searchResult the searchResult to set
     */
    public void setSearchResult(CollectionDataModel<KontakthistorieEntry> searchResult) {
        this.searchResult = searchResult;
    }
    
    public int getSearchResultSize() {
        return searchResult.size();
    }


    /**
     * @return the benutzerListe
     */
    public List<User> getBenutzerListe() {
        return benutzerListe;
    }


    /**
     * @param benutzerListe the benutzerListe to set
     */
    public void setBenutzerListe(List<User> benutzerListe) {
        this.benutzerListe = benutzerListe;
    }
}