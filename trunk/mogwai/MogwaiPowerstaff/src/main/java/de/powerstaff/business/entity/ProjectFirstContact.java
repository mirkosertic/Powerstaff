package de.powerstaff.business.entity;

import de.mogwai.common.business.entity.AuditableEntity;

public class ProjectFirstContact extends AuditableEntity {

    private String name1;

    private String name2;

    private ContactType contactType;

    private String contactTypeValue;

    private Freelancer matchingFreelancer;

    private String comment;

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getContactTypeValue() {
        return contactTypeValue;
    }

    public void setContactTypeValue(String contactTypeValue) {
        this.contactTypeValue = contactTypeValue;
    }

    public Freelancer getMatchingFreelancer() {
        return matchingFreelancer;
    }

    public void setMatchingFreelancer(Freelancer matchingFreelancer) {
        this.matchingFreelancer = matchingFreelancer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}