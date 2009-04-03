package de.powerstaff.business.entity;

import de.mogwai.common.business.entity.AuditableEntity;

public class HistoryEntity extends AuditableEntity {

    private String description;

    private HistoryType type;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HistoryType getType() {
        return type;
    }

    public void setType(HistoryType type) {
        this.type = type;
    }
}