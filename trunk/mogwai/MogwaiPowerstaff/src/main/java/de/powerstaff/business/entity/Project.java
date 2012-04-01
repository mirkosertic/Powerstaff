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

public class Project extends AuditableEntity implements UDFSupport {

    /**
     *
     */
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

    private List<ProjectHistory> history = new Vector<ProjectHistory>();

    private Map<String, UserDefinedField> udf = new HashMap<String, UserDefinedField>();

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

    public Map<String, UserDefinedField> getUdf() {
        return udf;
    }

    public void setUdf(Map<String, UserDefinedField> udf) {
        this.udf = udf;
    }

    /**
     * @return the entryDate
     */
    public Date getEntryDate() {
        return entryDate;
    }

    /**
     * @param entryDate the entryDate to set
     */
    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the stundensatzVK
     */
    public Long getStundensatzVK() {
        return stundensatzVK;
    }

    /**
     * @param stundensatzVK the stundensatzVK to set
     */
    public void setStundensatzVK(Long stundensatzVK) {
        this.stundensatzVK = stundensatzVK;
    }

    /**
     * @return the skills
     */
    public String getSkills() {
        return skills;
    }

    /**
     * @param skills the skills to set
     */
    public void setSkills(String skills) {
        this.skills = skills;
    }

    /**
     * @return the partner
     */
    public Partner getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Person getContactPerson() {
        if (customer != null) {
            return customer;
        }
        return partner;
    }

    /**
     * @return the kreditorNr
     */
    public String getKreditorNr() {
        return kreditorNr;
    }

    /**
     * @param kreditorNr the kreditorNr to set
     */
    public void setKreditorNr(String kreditorNr) {
        this.kreditorNr = kreditorNr;
    }

    /**
     * @return the debitorNr
     */
    public String getDebitorNr() {
        return debitorNr;
    }

    /**
     * @param debitorNr the debitorNr to set
     */
    public void setDebitorNr(String debitorNr) {
        this.debitorNr = debitorNr;
    }

    public void addPosition(ProjectPosition aPosition) {
        positions.add(aPosition);
    }

    public void removePosition(ProjectPosition aPosition) {
        positions.remove(aPosition);
    }

    public ProjectPositionStatus findStatusFor(long aFreelancerId) {
        for (ProjectPosition thePosition : positions) {
            if (thePosition.getFreelancerId() == aFreelancerId) {
                return thePosition.getStatus();
            }
        }
        return null;
    }
}