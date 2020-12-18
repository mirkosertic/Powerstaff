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
import de.powerstaff.business.lucene.analysis.ProfileAnalyzer;
import de.powerstaff.business.service.FreelancerFieldBridge;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ClassBridge;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

//    private String lastContact;

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution = Resolution.DAY)
    private Date lastContactDate;

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

    private String einsatzdetails;
    private boolean datenschutz;
    private Long sallaryRemote;

    public Freelancer() {
        tags = new HashSet<>();
    }

    @Column()
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

    @Column()
    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(final String contactPerson) {
        this.contactPerson = contactPerson;
    }

    @Column()
    public String getContactReason() {
        return contactReason;
    }

    public void setContactReason(final String contactReason) {
        this.contactReason = contactReason;
    }

    @Column()
    public String getContactType() {
        return contactType;
    }

    public void setContactType(final String contactType) {
        this.contactType = contactType;
    }

    @Column()
    public String getGulpID() {
        return gulpID;
    }

    public void setGulpID(final String gulpID) {
        this.gulpID = gulpID;
    }

/*    @Column(length = 255)
    public String getLastContact() {
        return lastContact;
    }

    public void setLastContact(String lastContact) {
        this.lastContact = lastContact;
    }*/

    public Date getLastContactDate() {
        return lastContactDate;
    }

    public void setLastContactDate(final Date lastContactDate) {
        this.lastContactDate = lastContactDate;
    }

    public boolean isShowAgain() {
        return showAgain;
    }

    public void setShowAgain(final boolean showAgain) {
        this.showAgain = showAgain;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(final String skills) {
        this.skills = skills;
    }

    @Column()
    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(final String workplace) {
        this.workplace = workplace;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(final Partner partner) {
        this.partner = partner;
    }

    @Column()
    public String getSallaryComment() {
        return sallaryComment;
    }

    public void setSallaryComment(final String sallaryComment) {
        this.sallaryComment = sallaryComment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(final Integer status) {
        this.status = status;
    }

    public Date getAvailabilityAsDate() {
        return availabilityAsDate;
    }

    public void setAvailabilityAsDate(final Date availabilityAsDate) {
        this.availabilityAsDate = availabilityAsDate;
    }

    public Long getSallaryPerDayLong() {
        return sallaryPerDayLong;
    }

    public void setSallaryPerDayLong(final Long sallaryPerDayLong) {
        this.sallaryPerDayLong = sallaryPerDayLong;
    }

    public Long getSallaryPartnerLong() {
        return sallaryPartnerLong;
    }

    public void setSallaryPartnerLong(final Long sallaryPartnerLong) {
        this.sallaryPartnerLong = sallaryPartnerLong;
    }

    public Long getSallaryPartnerPerDayLong() {
        return sallaryPartnerPerDayLong;
    }

    public void setSallaryPartnerPerDayLong(final Long sallaryPartnerPerDayLong) {
        this.sallaryPartnerPerDayLong = sallaryPartnerPerDayLong;
    }

    public Long getSallaryLong() {
        return sallaryLong;
    }

    public void setSallaryLong(final Long sallaryLong) {
        this.sallaryLong = sallaryLong;
    }

    public Set<FreelancerToTag> getTags() {
        return tags;
    }

    public void setTags(final Set<FreelancerToTag> tags) {
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

    protected List<FreelancerToTag> getTagsByType(final TagType aTagType) {
        final List<FreelancerToTag> theResult = new ArrayList<>();
        for (final FreelancerToTag theTags : getTags()) {
            if (theTags.getType() == aTagType) {
                theResult.add(theTags);
            }
        }
        theResult.sort(Comparator.comparing(AuditableEntity::getCreationDate));
        return theResult;
    }

    public List<FreelancerToTag> getAllTagsSorted() {
        final List<FreelancerToTag> theResult = new ArrayList<>(tags);
        theResult.sort(Comparator.comparing(AuditableEntity::getCreationDate));
        return theResult;
    }

    public boolean hasAllTags(final Set<Long> aTagIDs) {
        final Set<Long> theCurrentIDs = new HashSet<>();
        for (final FreelancerToTag theTag : getTags()) {
            theCurrentIDs.add(theTag.getTag().getId());
        }
        return theCurrentIDs.containsAll(aTagIDs);
    }

    public boolean hasTag(final Tag aTag) {
        for (final FreelancerToTag theTag : getTags()) {
            if (theTag.getTag().equals(aTag)) {
                return true;
            }
        }
        return false;
    }

    public String getEinsatzdetails() {
        return einsatzdetails;
    }

    public void setEinsatzdetails(final String einsatzdetails) {
        this.einsatzdetails = einsatzdetails;
    }

    public boolean isDatenschutz() {
        return datenschutz;
    }

    public void setDatenschutz(final boolean datenschutz) {
        this.datenschutz = datenschutz;
    }

    public Long getSallaryRemote() {
        return sallaryRemote;
    }

    public void setSallaryRemote(final Long sallaryRemote) {
        this.sallaryRemote = sallaryRemote;
    }
}