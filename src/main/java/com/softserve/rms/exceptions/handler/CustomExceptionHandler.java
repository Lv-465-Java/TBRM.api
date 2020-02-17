package com.softserve.rms.exceptions.handler;

import com.softserve.rms.exceptions.NotDeletedException;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotUniqueNameException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateParameterListIsEmpty;
import com.softserve.rms.exceptions.NotFoundException;
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

    /**
     * Method which handles {@link RuntimeException} exception.
     *
     * @param exception  {@link RuntimeException}
     * @return ResponseEntity which contains an error message
     * @author Halyna Yatseniuk
     */
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<Object> handleRuntimeException
//    (RuntimeException exception) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorMessage(exception));
//    }

    /**
     * Method with handles {@link PermissionException} exception.
     *
     * @param exception {@link PermissionException}
     * @return ResponseEntity which contains an error message
     * @author Artur Sydor
     */
    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<Object> handleDeniedAccessException(PermissionException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(generateErrorMessage(exception));
    }

    /**
     * Method which handles {@link NotFoundException} exception.
     *
     * @param exception  {@link NotFoundException}
     * @return ResponseEntity which contains an error message
     * @author Halyna Yatseniuk
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException
    (NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorMessage(exception));
    }

    /**
     * Method which handles {@link NotDeletedException} exception.
     *
     * @param exception  {@link NotDeletedException}
     * @return ResponseEntity which contains an error message
     * @author Halyna Yatseniuk
     */
    @ExceptionHandler(NotDeletedException.class)
    public ResponseEntity<Object> handleNotDeletedException
    (NotDeletedException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorMessage(exception));
    }

    /**
     * Method which handles {@link ResourceTemplateParameterListIsEmpty} exception.
     *
     * @param exception  {@link ResourceTemplateParameterListIsEmpty}
     * @return ResponseEntity which contains an error message
     * @author Halyna Yatseniuk
     */
    @ExceptionHandler(ResourceTemplateParameterListIsEmpty.class)
    public ResponseEntity<Object> handleResourceTemplateParameterListIsEmpty
    (ResourceTemplateParameterListIsEmpty exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorMessage(exception));
    }

    /**
     * Method which handles {@link NotUniqueNameException} exception.
     *
     * @param exception  {@link NotUniqueNameException}
     * @return ResponseEntity which contains an error message
     * @author Halyna Yatseniuk
     */
    @ExceptionHandler(NotUniqueNameException.class)
    public ResponseEntity<Object> handleNameIsNotUniqueException
    (NotUniqueNameException exception) {
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
