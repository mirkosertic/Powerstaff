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
package de.powerstaff.business.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;

import de.mogwai.common.business.entity.AuditableEntity;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

public class Person<T extends Contact, H extends HistoryEntity> extends
		AuditableEntity {

	private static final long serialVersionUID = 1255375037203457359L;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private String titel;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private String name1;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private String name2;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private String company;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private String street;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private String country;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private String plz;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private String city;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private String nationalitaet;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private String geburtsdatum;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private String comments;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private boolean contactforbidden;

	private Set<T> contacts = new HashSet<T>();

	private Set<H> history = new HashSet<H>();

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private String kreditorNr;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private String debitorNr;

	public Person() {
	}

	@Column(length = 255)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(length = 255)
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public boolean isContactforbidden() {
		return contactforbidden;
	}

	public void setContactforbidden(boolean contactforbidden) {
		this.contactforbidden = contactforbidden;
	}

	@Column(length = 255)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Set<T> getContacts() {
		return contacts;
	}

	public void setContacts(Set<T> kontakte) {
		this.contacts = kontakte;
	}

	@Column(length = 255)
	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	@Column(length = 255)
	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	@Column(length = 255)
	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	@Column(length = 255)
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Set<H> getHistory() {
		return history;
	}

	public void setHistory(Set<H> history) {
		this.history = history;
	}

	/**
	 * @return the kreditorNr
	 */
	@Column(length = 255)
	public String getKreditorNr() {
		return kreditorNr;
	}

	/**
	 * @param kreditorNr
	 *            the kreditorNr to set
	 */
	public void setKreditorNr(String kreditorNr) {
		this.kreditorNr = kreditorNr;
	}

	/**
	 * @return the debitorNr
	 */
	@Column(length = 255)
	public String getDebitorNr() {
		return debitorNr;
	}

	/**
	 * @param debitorNr
	 *            the debitorNr to set
	 */
	public void setDebitorNr(String debitorNr) {
		this.debitorNr = debitorNr;
	}

	/**
	 * @return the titel
	 */
	@Column(length = 255)
	public String getTitel() {
		return titel;
	}

	/**
	 * @param titel
	 *            the titel to set
	 */
	public void setTitel(String titel) {
		this.titel = titel;
	}

	/**
	 * @return the nationalitaet
	 */
	@Column(length = 255)
	public String getNationalitaet() {
		return nationalitaet;
	}

	/**
	 * @param nationalitaet
	 *            the nationalitaet to set
	 */
	public void setNationalitaet(String nationalitaet) {
		this.nationalitaet = nationalitaet;
	}

	/**
	 * @return the geburtsdatum
	 */
	@Column(length = 255)
	public String getGeburtsdatum() {
		return geburtsdatum;
	}

	/**
	 * @param geburtsdatum
	 *            the geburtsdatum to set
	 */
	public void setGeburtsdatum(String geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}

    public List<T> getEMailContacts() {
        List<T> theResult = new ArrayList<T>();
        for (T theEntry : getContacts()) {
            if (theEntry.getType().isEmail()) {
                theResult.add(theEntry);
            }
        }
        return theResult;
    }

    public List<T> getWebContacts() {
        List<T> theResult = new ArrayList<T>();
        for (T theEntry : getContacts()) {
            if (theEntry.getType().isWeb()) {
                theResult.add(theEntry);
            }
        }
        return theResult;
    }

}
