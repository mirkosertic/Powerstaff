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

import de.mogwai.common.business.entity.AuditableEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Eine gespeicherte Profilsuche.
 *
 * @author msertic
 */
public class SavedProfileSearch extends AuditableEntity implements
        Comparable<SavedProfileSearch>, Cloneable {

    private static final long serialVersionUID = 8205335843201371353L;

    private User user;

    private String profileContent;

    private String plz;

    private Long stundensatzVon;

    private Long stundensatzBis;

    private String sortierung;

    private Project project;

    private Set<String> profilesToIgnore = new HashSet<String>();

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the profileContent
     */
    public String getProfileContent() {
        return profileContent;
    }

    /**
     * @param profileContent the profileContent to set
     */
    public void setProfileContent(String profileContent) {
        this.profileContent = profileContent;
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
     * @return the stundensatzVon
     */
    public Long getStundensatzVon() {
        return stundensatzVon;
    }

    /**
     * @param stundensatzVon the stundensatzVon to set
     */
    public void setStundensatzVon(Long stundensatzVon) {
        this.stundensatzVon = stundensatzVon;
    }

    /**
     * @return the stundensatzBis
     */
    public Long getStundensatzBis() {
        return stundensatzBis;
    }

    /**
     * @param stundensatzBis the stundensatzBis to set
     */
    public void setStundensatzBis(Long stundensatzBis) {
        this.stundensatzBis = stundensatzBis;
    }

    public String getSortierung() {
        return sortierung;
    }

    public void setSortierung(String sortierung) {
        this.sortierung = sortierung;
    }

    public Set<String> getProfilesToIgnore() {
        return profilesToIgnore;
    }

    public void setProfilesToIgnore(Set<String> profilesToIgnore) {
        this.profilesToIgnore = profilesToIgnore;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public int compareTo(SavedProfileSearch o) {
        return getCreationDate().compareTo(o.getCreationDate());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SavedProfileSearch theNewObject = new SavedProfileSearch();
        theNewObject.profileContent = profileContent;
        theNewObject.plz = plz;
        theNewObject.stundensatzVon = stundensatzVon;
        theNewObject.stundensatzBis = stundensatzBis;
        theNewObject.sortierung = sortierung;
        theNewObject.profilesToIgnore.addAll(profilesToIgnore);
        return theNewObject;
    }
}