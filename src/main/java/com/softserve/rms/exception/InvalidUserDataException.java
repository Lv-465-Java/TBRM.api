package com.softserve.rms.exception;

import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * Exception that we can get during registration and update
 * user process
 *
 * @author Mariia Shchur
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidUserDataException extends RuntimeException {

    public InvalidUserDataException(String message) {
        super(message);
    }

    /**
     * Constructor for InvalidUserRegistrationDataException.
     *
     * @param messages - giving map of messages.
     */
    public InvalidUserDataException(Map<String, String> messages) {
        super(new JSONObject(messages).toString());
    }
}
