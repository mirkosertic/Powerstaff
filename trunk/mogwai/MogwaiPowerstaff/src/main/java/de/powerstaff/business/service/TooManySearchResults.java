package de.powerstaff.business.service;

import java.util.Collection;

import de.powerstaff.business.dao.GenericSearchResult;

/**
 * Esception für zu viele Suchergebnisse.
 * 
 * @author msertic
 */
public class TooManySearchResults extends Exception {

    private Collection<GenericSearchResult> result;

    public TooManySearchResults(Collection<GenericSearchResult> aResult) {
        result = aResult;
    }

    /**
     * @return the result
     */
    public Collection<GenericSearchResult> getResult() {
        return result;
    }
}