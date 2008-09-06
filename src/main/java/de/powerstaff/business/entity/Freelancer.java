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

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.persistence.Column;

public class Freelancer extends Person<FreelancerContact, FreelancerHistory> implements UDFSupport {

    private Map<String, UserDefinedField> udf = new HashMap<String, UserDefinedField>();
    
    private String workplace;

    private Date availabilityAsDate;

    private Long sallaryLong;

    private String code;

    private String contactPerson;

    private String contactType;

    private String contactReason;

    private String lastContact;

    private String skills;

    private String gulpID;

    private boolean showAgain;

    private Vector<FreelancerProfile> profile = new Vector<FreelancerProfile>();

    private Long sallaryPerDayLong;

    private String sallaryComment;

    private Integer status;

    private Partner partner;

    private Long sallaryPartnerLong;

    private Long sallaryPartnerPerDayLong;

    private Set<ProjectPosition> projects = new HashSet<ProjectPosition>();
    
    private String geburtsdatum;

    public Set<ProjectPosition> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectPosition> orders) {
        this.projects = orders;
    }

    public Freelancer() {
    }

    public Vector<FreelancerProfile> getProfile() {
        return profile;
    }

    public void setProfile(Vector<FreelancerProfile> profile) {
        this.profile = profile;
    }

    @Column(length = 255)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 255)
    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    @Column(length = 255)
    public String getContactReason() {
        return contactReason;
    }

    public void setContactReason(String contactReason) {
        this.contactReason = contactReason;
    }

    @Column(length = 255)
    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    @Column(length = 255)
    public String getGulpID() {
        return gulpID;
    }

    public void setGulpID(String gulpID) {
        this.gulpID = gulpID;
    }

    @Column(length = 255)
    public String getLastContact() {
        return lastContact;
    }

    public void setLastContact(String lastContact) {
        this.lastContact = lastContact;
    }

    public boolean isShowAgain() {
        return showAgain;
    }

    public void setShowAgain(boolean showAgain) {
        this.showAgain = showAgain;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    @Column(length = 255)
    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    @Column(length = 255)
    public String getSallaryComment() {
        return sallaryComment;
    }

    public void setSallaryComment(String sallaryComment) {
        this.sallaryComment = sallaryComment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map<String, UserDefinedField> getUdf() {
        return udf;
    }

    public void setUdf(Map<String, UserDefinedField> udf) {
        this.udf = udf;
    }

    /**
     * @return the geburtsdatum
     */
    @Column(length = 255)
    public String getGeburtsdatum() {
        return geburtsdatum;
    }

    /**
     * @param geburtsdatum the geburtsdatum to set
     */
    public void setGeburtsdatum(String geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    /**
     * @return the availabilityAsDate
     */
    public Date getAvailabilityAsDate() {
        return availabilityAsDate;
    }

    /**
     * @param availabilityAsDate the availabilityAsDate to set
     */
    public void setAvailabilityAsDate(Date availabilityAsDate) {
        this.availabilityAsDate = availabilityAsDate;
    }

    /**
     * @return the sallaryPerDayLong
     */
    public Long getSallaryPerDayLong() {
        return sallaryPerDayLong;
    }

    /**
     * @param sallaryPerDayLong the sallaryPerDayLong to set
     */
    public void setSallaryPerDayLong(Long sallaryPerDayLong) {
        this.sallaryPerDayLong = sallaryPerDayLong;
    }

    /**
     * @return the sallaryPartnerLong
     */
    public Long getSallaryPartnerLong() {
        return sallaryPartnerLong;
    }

    /**
     * @param sallaryPartnerLong the sallaryPartnerLong to set
     */
    public void setSallaryPartnerLong(Long sallaryPartnerLong) {
        this.sallaryPartnerLong = sallaryPartnerLong;
    }

    /**
     * @return the sallaryPartnerPerDayLong
     */
    public Long getSallaryPartnerPerDayLong() {
        return sallaryPartnerPerDayLong;
    }

    /**
     * @param sallaryPartnerPerDayLong the sallaryPartnerPerDayLong to set
     */
    public void setSallaryPartnerPerDayLong(Long sallaryPartnerPerDayLong) {
        this.sallaryPartnerPerDayLong = sallaryPartnerPerDayLong;
    }

    /**
     * @return the sallaryLong
     */
    public Long getSallaryLong() {
        return sallaryLong;
    }

    /**
     * @param sallaryLong the sallaryLong to set
     */
    public void setSallaryLong(Long sallaryLong) {
        this.sallaryLong = sallaryLong;
    }
}