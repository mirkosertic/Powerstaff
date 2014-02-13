package de.powerstaff.business.entity;

import de.mogwai.common.business.entity.AuditableEntity;

import javax.persistence.Column;

public class Tag extends AuditableEntity {

    @Column(length = 255)
    private String name;

    private TagType type;

    public Tag() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TagType getType() {
        return type;
    }

    public void setType(TagType type) {
        this.type = type;
    }
}
