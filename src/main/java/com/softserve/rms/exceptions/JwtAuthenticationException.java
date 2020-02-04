package com.softserve.rms.exceptions;

/**
 * Exception that we get when jwt authentication failed.
 * @author Kravets Maryana
 */
public class JwtAuthenticationException extends RuntimeException {

    /**
     * Constructor for JwtAuthenticationException.
     *
     * @param msg - giving message.
     */
    public JwtAuthenticationException(String msg){
        super(msg);
    }
}
