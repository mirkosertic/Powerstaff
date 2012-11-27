package de.powerstaff.business.service;

/**
 * Fehler, welcher beim Speichern durch das optimitsic Locking auftreten kann.
 */
public class OptimisticLockException extends RuntimeException {

    public OptimisticLockException(Exception e) {
        super(e);
    }
}
