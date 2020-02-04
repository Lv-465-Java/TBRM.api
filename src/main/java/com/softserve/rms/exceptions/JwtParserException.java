package com.softserve.rms.exceptions;

public class JwtParserException extends RuntimeException {

    /**
     * Constructor for BadCredentialException.
     *
     * @param msg - giving message.
     */
    public JwtParserException(String msg){
        super(msg);
    }
}
