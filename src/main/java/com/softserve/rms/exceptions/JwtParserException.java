package com.softserve.rms.exceptions;

public class JwtParserException extends RuntimeException {

    public JwtParserException(String msg){
        super(msg);
    }
}
