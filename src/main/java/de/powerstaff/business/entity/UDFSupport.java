package de.powerstaff.business.entity;

import java.util.Map;

/**
 * Schnittstelle für UDF Unterstützung.
 *
 * @author msertic
 */
public interface UDFSupport {

    Map<String, UserDefinedField> getUdf();

    void setUdf(Map<String, UserDefinedField> udf);
}
