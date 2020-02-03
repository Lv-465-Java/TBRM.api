package com.softserve.rms.exceptions.handler;

import com.softserve.rms.exceptions.PermissionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<Object> handleResourceTemplateParameterListIsEmpty
            (PermissionException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorMessage(exception));
    }

    /**
     * Method which invokes an exception message and puts it in a map.
     *
     * @param exception global exception
     * @return map with String key and String value, which contains an error message
     * @author Halyna Yatseniuk
     */
    private Map<String, String> generateErrorMessage(Exception exception) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", exception.getMessage());
        return errorResponse;
    }
}
