package com.softserve.rms.exceptions;

/**
 * Exception that we get when user enter bad credential.
 * @author Kravets Maryana
 */
public class BadCredentialException extends RuntimeException {

    /**
     * Constructor for BadCredentialException.
     *
     * @param msg - giving message.
     */
    public BadCredentialException(String msg){
        super(msg);
    }
}
