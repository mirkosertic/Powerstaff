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

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.persistence.Column;

public class Freelancer extends Person<FreelancerContact, FreelancerHistory> {

    private String workplace;

    private String availability;

    private String sallary;

    private String code;

    private String contactPerson;

    private String contactType;

    private String contactReason;

    private String lastContact;

    private String skills;

    private String gulpID;

    private boolean showAgain;

    private Vector<FreelancerProfile> profile = new Vector<FreelancerProfile>();

    private String sallaryPerDay;

    private String sallaryComment;

    private Integer status;

    private Partner partner;

    private String sallaryPartner;

    private String sallaryPartnerPerDay;

    private Set<ProjectPosition> projects = new HashSet<ProjectPosition>();

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
    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
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

    @Column(length = 255)
    public String getSallary() {
        return sallary;
    }

    public void setSallary(String sallary) {
        this.sallary = sallary;
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

    @Column(length = 255)
    public String getSallaryPartner() {
        return sallaryPartner;
    }

    public void setSallaryPartner(String sallaryPartner) {
        this.sallaryPartner = sallaryPartner;
    }

    @Column(length = 255)
    public String getSallaryPartnerPerDay() {
        return sallaryPartnerPerDay;
    }

    public void setSallaryPartnerPerDay(String sallaryPartnerPerDay) {
        this.sallaryPartnerPerDay = sallaryPartnerPerDay;
    }

    @Column(length = 255)
    public String getSallaryPerDay() {
        return sallaryPerDay;
    }

    public void setSallaryPerDay(String sallaryPerDay) {
        this.sallaryPerDay = sallaryPerDay;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
