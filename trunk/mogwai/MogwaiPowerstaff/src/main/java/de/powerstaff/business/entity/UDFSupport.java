package de.powerstaff.business.entity;

import java.util.Map;

/**
 * Schnittstelle f�r UDF Unterst�tzung.
 *
 * @author msertic
 */
public interface UDFSupport {

    Map<String, UserDefinedField> getUdf();

    void setUdf(Map<String, UserDefinedField> udf);
}
