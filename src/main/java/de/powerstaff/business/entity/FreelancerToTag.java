package de.powerstaff.business.entity;

import de.mogwai.common.business.entity.AuditableEntity;

public class FreelancerToTag extends AuditableEntity {

    private TagType type;
    private Tag tag;

    public FreelancerToTag() {
    }

    public TagType getType() {
        return type;
    }

    public void setType(TagType type) {
        this.type = type;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
