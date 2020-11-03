/*
  Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.

  This library is free software; you can redistribute it and/or modify it under
  the terms of the GNU Lesser General Public License as published by the Free
  Software Foundation; either version 2.1 of the License, or (at your option)
  any later version.

  This library is distributed in the hope that it will be useful, but WITHOUT
  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
  FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
  details.

  You should have received a copy of the GNU Lesser General Public License
  along with this library; if not, write to the Free Software Foundation, Inc.,
  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
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

    private Set<String> profilesToIgnore = new HashSet<>();

    private String selectedTags;

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(final User user) {
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
    public void setProfileContent(final String profileContent) {
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
    public void setPlz(final String plz) {
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
    public void setStundensatzVon(final Long stundensatzVon) {
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
    public void setStundensatzBis(final Long stundensatzBis) {
        this.stundensatzBis = stundensatzBis;
    }

    public String getSortierung() {
        return sortierung;
    }

    public String getSortierungField() {
        if (sortierung != null) {
            if (sortierung.startsWith("+") || sortierung.startsWith("-")) {
                return sortierung.substring(1);
            }
        }
        return sortierung;
    }

    public boolean isSortierungReverse() {
        return sortierung != null && sortierung.startsWith("-");
    }

    public void setSortierung(final String aSortierung) {
        if (aSortierung != null && (aSortierung.startsWith("+") || aSortierung.startsWith("-"))) {
            // HIbernate or clone access
            sortierung = aSortierung;
            return;
        }
        if (sortierung!= null && sortierung.contains(aSortierung)) {
            if (sortierung.startsWith("+")) {
                sortierung = "-" + aSortierung;
            } else {
                sortierung = "+" + aSortierung;
            }
        } else {
            this.sortierung = "+" + aSortierung;
        }
    }

    public Set<String> getProfilesToIgnore() {
        return profilesToIgnore;
    }

    public void setProfilesToIgnore(final Set<String> profilesToIgnore) {
        this.profilesToIgnore = profilesToIgnore;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }

    @Override
    public int compareTo(final SavedProfileSearch o) {
        return getCreationDate().compareTo(o.getCreationDate());
    }

    public String getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(final String selectedTags) {
        this.selectedTags = selectedTags;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        final SavedProfileSearch theNewObject = new SavedProfileSearch();
        theNewObject.profileContent = profileContent;
        theNewObject.plz = plz;
        theNewObject.stundensatzVon = stundensatzVon;
        theNewObject.stundensatzBis = stundensatzBis;
        theNewObject.sortierung = sortierung;
        theNewObject.profilesToIgnore.addAll(profilesToIgnore);
        theNewObject.selectedTags = selectedTags;
        return theNewObject;
    }
}