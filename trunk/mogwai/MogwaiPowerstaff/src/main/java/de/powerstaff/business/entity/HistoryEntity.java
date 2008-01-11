package de.powerstaff.business.entity;

import de.mogwai.common.business.entity.AuditableEntity;

public class HistoryEntity extends AuditableEntity {
	
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
