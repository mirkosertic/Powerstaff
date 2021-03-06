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
package de.powerstaff.business.dto;

import de.powerstaff.business.entity.FreelancerContact;
import de.powerstaff.business.entity.Tag;

import java.io.Serializable;
import java.util.*;

public class ProfileSearchInfoDetail implements Serializable {

    private static final long serialVersionUID = -5622749387352518250L;

    private Long id;

    private String name1;

    private String name2;

    private Date availability;

    private String plz;

    private Long stundensatz;

    private boolean contactforbidden;

    private List<FreelancerContact> contacts;

    private Set<Tag> tags;

    private Date lastContact;

    public ProfileSearchInfoDetail() {
        contacts = new ArrayList<FreelancerContact>();
        tags = new HashSet<Tag>();
    }

    public List<Tag> getSortedTags() {
        List<Tag> theResult = new ArrayList<Tag>();
        theResult.addAll(tags);
        Collections.sort(theResult, new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return theResult;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    /**
     * @return the availability
     */
    public Date getAvailability() {
        return availability;
    }

    /**
     * @param availability the availability to set
     */
    public void setAvailability(Date availability) {
        this.availability = availability;
    }

    /**
     * @return the name1
     */
    public String getName1() {
        return name1;
    }

    /**
     * @param name1 the name1 to set
     */
    public void setName1(String name1) {
        this.name1 = name1;
    }

    /**
     * @return the name2
     */
    public String getName2() {
        return name2;
    }

    /**
     * @param name2 the name2 to set
     */
    public void setName2(String name2) {
        this.name2 = name2;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the plz
     */
    public String getPlz() {
        return plz;
    }

    /**
     * @param plz the plz to set
     */
    public void setPlz(String plz) {
        this.plz = plz;
    }

    /**
     * @return the stundensatz
     */
    public Long getStundensatz() {
        return stundensatz;
    }

    /**
     * @param stundensatz the stundensatz to set
     */
    public void setStundensatz(Long stundensatz) {
        this.stundensatz = stundensatz;
    }

    /**
     * @return the contacts
     */
    public List<FreelancerContact> getContacts() {
        return contacts;
    }

    /**
     * @param contacts the contacts to set
     */
    public void setContacts(List<FreelancerContact> contacts) {
        this.contacts = contacts;
    }

    public boolean isContactforbidden() {
        return contactforbidden;
    }

    public void setContactforbidden(boolean contactforbidden) {
        this.contactforbidden = contactforbidden;
    }

    public FreelancerContact getGulpNameContact() {
        for (FreelancerContact theContact : contacts) {
            if (theContact.getType().isGulp()) {
                return theContact;
            }
        }
        return null;
    }

    public Date getLastContact() {
        return lastContact;
    }

    public void setLastContact(Date lastContact) {
        this.lastContact = lastContact;
    }
}