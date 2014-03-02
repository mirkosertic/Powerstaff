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

import de.powerstaff.business.lucene.analysis.ProfileAnalyzer;
import de.powerstaff.business.service.FreelancerFieldBridge;
import org.hibernate.search.annotations.*;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.*;

@Indexed
@ClassBridge(name = "freelancer", impl = FreelancerFieldBridge.class)
@Analyzer(impl = ProfileAnalyzer.class)
public class Freelancer extends Person<FreelancerContact, FreelancerHistory> {

    private static final long serialVersionUID = 3142067482465272515L;

    private String workplace;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution = Resolution.DAY)
    private Date availabilityAsDate;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @NumericField
    private Long sallaryLong;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    private String code;

    private String contactPerson;

    private String contactType;

    private String contactReason;

    private String lastContact;

    private String skills;

    private String gulpID;

    private boolean showAgain;

    private Long sallaryPerDayLong;

    private String sallaryComment;

    private Integer status;

    private Partner partner;

    private Long sallaryPartnerLong;

    private Long sallaryPartnerPerDayLong;

    private Set<FreelancerToTag> tags;
    private List<? extends FreelancerToTag> bemerkungenTags;
    private Collection<? extends FreelancerToTag> einsatzorteTags;
    private Collection<? extends FreelancerToTag> schwerpunkteTags;
    private Object firstContactEMail;

    public Freelancer() {
        tags = new HashSet<FreelancerToTag>();
    }

    @Column(length = 255)
    public String getCode() {
        if (code != null) {
            code = code.trim();
        }
        return code;
    }

    public void setCode(String code) {
        if (code != null) {
            code = code.trim();
        }
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

    public Date getAvailabilityAsDate() {
        return availabilityAsDate;
    }

    public void setAvailabilityAsDate(Date availabilityAsDate) {
        this.availabilityAsDate = availabilityAsDate;
    }

    public Long getSallaryPerDayLong() {
        return sallaryPerDayLong;
    }

    public void setSallaryPerDayLong(Long sallaryPerDayLong) {
        this.sallaryPerDayLong = sallaryPerDayLong;
    }

    public Long getSallaryPartnerLong() {
        return sallaryPartnerLong;
    }

    public void setSallaryPartnerLong(Long sallaryPartnerLong) {
        this.sallaryPartnerLong = sallaryPartnerLong;
    }

    public Long getSallaryPartnerPerDayLong() {
        return sallaryPartnerPerDayLong;
    }

    public void setSallaryPartnerPerDayLong(Long sallaryPartnerPerDayLong) {
        this.sallaryPartnerPerDayLong = sallaryPartnerPerDayLong;
    }

    public Long getSallaryLong() {
        return sallaryLong;
    }

    public void setSallaryLong(Long sallaryLong) {
        this.sallaryLong = sallaryLong;
    }

    public Set<FreelancerToTag> getTags() {
        return tags;
    }

    public void setTags(Set<FreelancerToTag> tags) {
        this.tags = tags;
    }

    public List<FreelancerToTag> getBemerkungenTags() {
        return getTagsByType(TagType.BEMERKUNG);
    }

    public List<FreelancerToTag> getEinsatzorteTags() {
        return getTagsByType(TagType.EINSATZORT);
    }

    public List<FreelancerToTag> getFunktionenTags() {
        return getTagsByType(TagType.FUNKTION);
    }

    public List<FreelancerToTag> getSchwerpunkteTags() {
        return getTagsByType(TagType.SCHWERPUNKT);
    }

    protected List<FreelancerToTag> getTagsByType(TagType aTagType) {
        List<FreelancerToTag> theResult = new ArrayList<FreelancerToTag>();
        for (FreelancerToTag theTags : getTags()) {
            if (theTags.getType() == aTagType) {
                theResult.add(theTags);
            }
        }
        Collections.sort(theResult, new Comparator<FreelancerToTag>() {
            @Override
            public int compare(FreelancerToTag o1, FreelancerToTag o2) {
                return o1.getCreationDate().compareTo(o2.getCreationDate());
            }
        });
        return theResult;
    }

    public List<FreelancerToTag> getAllTagsSorted() {
        List<FreelancerToTag> theResult = new ArrayList<FreelancerToTag>(tags);
        Collections.sort(theResult, new Comparator<FreelancerToTag>() {
            @Override
            public int compare(FreelancerToTag o1, FreelancerToTag o2) {
                return o1.getCreationDate().compareTo(o2.getCreationDate());
            }
        });
        return theResult;
    }

    public boolean hasAllTags(Set<Long> aTagIDs) {
        Set<Long> theCurrentIDs = new HashSet<Long>();
        for (FreelancerToTag theTag : getTags()) {
            theCurrentIDs.add(theTag.getTag().getId());
        }
        return theCurrentIDs.containsAll(aTagIDs);
    }

    public boolean hasTag(Tag aTag) {
        for (FreelancerToTag theTag : getTags()) {
            if (theTag.getTag().equals(aTag)) {
                return true;
            }
        }
        return false;
    }
}