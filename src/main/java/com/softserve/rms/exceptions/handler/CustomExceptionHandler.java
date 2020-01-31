package com.softserve.rms.exceptions.handler;

import com.softserve.rms.exceptions.resourseTemplate.NameIsNotUniqueException;
import com.softserve.rms.exceptions.resourseTemplate.NoSuchResourceTemplateException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateIsPublishedException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateParameterListIsEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Method which handles {@link RuntimeException} exception.
     *
     * @param exception  {@link RuntimeException}
     * @param webRequest {@link WebRequest}
     * @return ResponseEntity which contains an error message
     * @author Halyna Yatseniuk
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException
    (RuntimeException exception, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorMessage(exception));
    }

    /**
     * Method which handles {@link NoSuchResourceTemplateException} exception.
     *
     * @param exception  {@link NoSuchResourceTemplateException}
     * @param webRequest {@link WebRequest}
     * @return ResponseEntity which contains an error message
     * @author Halyna Yatseniuk
     */
    @ExceptionHandler(NoSuchResourceTemplateException.class)
    public ResponseEntity<Object> handleNoSuchResourceTemplateException
    (NoSuchResourceTemplateException exception, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorMessage(exception));
    }

    /**
     * Method which handles {@link ResourceTemplateIsPublishedException} exception.
     *
     * @param exception  {@link ResourceTemplateIsPublishedException}
     * @param webRequest {@link WebRequest}
     * @return ResponseEntity which contains an error message
     * @author Halyna Yatseniuk
     */
    @ExceptionHandler(ResourceTemplateIsPublishedException.class)
    public ResponseEntity<Object> handleResourceTemplateIsPublishedException
    (ResourceTemplateIsPublishedException exception, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorMessage(exception));
    }

    /**
     * Method which handles {@link ResourceTemplateParameterListIsEmpty} exception.
     *
     * @param exception  {@link ResourceTemplateParameterListIsEmpty}
     * @param webRequest {@link WebRequest}
     * @return ResponseEntity which contains an error message
     * @author Halyna Yatseniuk
     */
    @ExceptionHandler(ResourceTemplateParameterListIsEmpty.class)
    public ResponseEntity<Object> handleResourceTemplateParameterListIsEmpty
    (ResourceTemplateParameterListIsEmpty exception, WebRequest webRequest) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorMessage(exception));
    }

    /**
     * Method which handles {@link NameIsNotUniqueException} exception.
     *
     * @param exception  {@link NameIsNotUniqueException}
     * @param webRequest {@link WebRequest}
     * @return ResponseEntity which contains an error message
     * @author Halyna Yatseniuk
     */
    @ExceptionHandler(NameIsNotUniqueException.class)
    public ResponseEntity<Object> handleNameIsNotUniqueException
    (NameIsNotUniqueException exception, WebRequest webRequest) {
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