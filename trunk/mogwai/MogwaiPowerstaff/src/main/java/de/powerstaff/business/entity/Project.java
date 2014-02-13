/**
 * Mogwai KIAS. Copyright (C) 2002 The Mogwai Project.
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

import javax.persistence.Column;
import java.util.*;

public class Project extends AuditableEntity {

    private static final long serialVersionUID = -5246378338479768068L;

    private Date entryDate;

    private String projectNumber;

    private String workplace;

    private Date startDate;

    private String duration;

    private String descriptionShort;

    private String descriptionLong;

    private String skills;

    private boolean visibleOnWebSite;

    private int status = 1;

    private Long stundensatzVK;

    private Customer customer;

    private Partner partner;

    private String kreditorNr;

    private String debitorNr;

    private Set<ProjectPosition> positions = new HashSet<ProjectPosition>();

    private Set<ProjectFirstContact> firstContacts = new HashSet<ProjectFirstContact>();

    private List<ProjectHistory> history = new Vector<ProjectHistory>();

    public String getDescriptionLong() {
        return descriptionLong;
    }

    public void setDescriptionLong(String descriptionLong) {
        this.descriptionLong = descriptionLong;
    }

    @Column(length = 255)
    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    @Column(length = 255)
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Set<ProjectPosition> getPositions() {
        return positions;
    }

    public void setPositions(Set<ProjectPosition> positions) {
        this.positions = positions;
    }

    @Column(length = 255)
    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    @Column(length = 255)
    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public boolean isVisibleOnWebSite() {
        return visibleOnWebSite;
    }

    public void setVisibleOnWebSite(boolean visibleOnWebSite) {
        this.visibleOnWebSite = visibleOnWebSite;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<ProjectHistory> getHistory() {
        return history;
    }

    public void setHistory(List<ProjectHistory> history) {
        this.history = history;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getStundensatzVK() {
        return stundensatzVK;
    }

    public void setStundensatzVK(Long stundensatzVK) {
        this.stundensatzVK = stundensatzVK;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Person getContactPerson() {
        if (customer != null) {
            return customer;
        }
        return partner;
    }

    public String getKreditorNr() {
        return kreditorNr;
    }

    public void setKreditorNr(String kreditorNr) {
        this.kreditorNr = kreditorNr;
    }

    public String getDebitorNr() {
        return debitorNr;
    }

    public void setDebitorNr(String debitorNr) {
        this.debitorNr = debitorNr;
    }

    public void addPosition(ProjectPosition aPosition) {
        positions.add(aPosition);
    }

    public void removePosition(ProjectPosition aPosition) {
        positions.remove(aPosition);
    }

    public Set<ProjectFirstContact> getFirstContacts() {
        return firstContacts;
    }

    public void setFirstContacts(Set<ProjectFirstContact> firstContacts) {
        this.firstContacts = firstContacts;
    }
}