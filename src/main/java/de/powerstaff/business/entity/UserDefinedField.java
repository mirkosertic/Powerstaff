package de.powerstaff.business.entity;

import java.util.Date;

import de.mogwai.common.business.entity.AuditableEntity;

/**
 * A user defined field.
 * 
 * @author msertic
 */
public class UserDefinedField extends AuditableEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6428209739024154852L;

	private boolean booleanValue;

    private Date dateValue;

    private int intValue;

    private long longValue;

    private String stringValue;

    private String longStringValue;

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getLongStringValue() {
        return longStringValue;
    }

    public void setLongStringValue(String longStringValue) {
        this.longStringValue = longStringValue;
    }
}
