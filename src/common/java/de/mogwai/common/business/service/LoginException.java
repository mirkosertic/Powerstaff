package de.mogwai.common.business.service;

public class LoginException extends Exception {

	private static final long serialVersionUID = -4435752518620774055L;

	public LoginException(String aMessage, Exception e) {
        super(aMessage, e);
    }
}
