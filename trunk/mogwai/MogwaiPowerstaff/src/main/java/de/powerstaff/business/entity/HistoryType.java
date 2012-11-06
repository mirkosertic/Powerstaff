package de.powerstaff.business.entity;

import de.mogwai.common.business.entity.Entity;

public class HistoryType extends Entity {

    /**
     *
     */
    private static final long serialVersionUID = 8782433866843304479L;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}