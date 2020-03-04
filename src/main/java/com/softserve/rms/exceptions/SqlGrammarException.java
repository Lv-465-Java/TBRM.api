package com.softserve.rms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SqlGrammarException extends RuntimeException {

    public SqlGrammarException(String msg) {
        super(msg);
    }
}