package sanchez.sergio.rest;

import java.nio.file.AccessDeniedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import sanchez.sergio.rest.exceptions.ResourceNotFoundException;

/**
 *
 * @author sergio
 */
@ControllerAdvice
public class CommonErrorRestController extends BaseErrorRestController {

    @ExceptionHandler(ResourceNotFoundException.class)
    protected @ResponseBody
    ResponseEntity<RestApiError> handleNoteNotFoundException(ResourceNotFoundException resourceNotFound, HttpServletRequest request, HttpServletResponse response) {
        return createAndSendResponse(HttpStatus.NOT_FOUND, ApiErrorEnum.RESOURCE_NOT_FOUND, resourceNotFound.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected @ResponseBody
    ResponseEntity<RestApiError> handleAccessDeniedException(AccessDeniedException accessDeniedException, HttpServletRequest request, HttpServletResponse response) {
        return createAndSendResponse(HttpStatus.UNAUTHORIZED, ApiErrorEnum.ACCESS_DENIED, accessDeniedException.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    protected @ResponseBody
    ResponseEntity<RestApiError> handleSecurityException(SecurityException exception, HttpServletRequest request, HttpServletResponse response) {
        return createAndSendResponse(HttpStatus.UNAUTHORIZED, ApiErrorEnum.SECURITY, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected @ResponseBody
    ResponseEntity<RestApiError> handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        return createAndSendResponse(HttpStatus.BAD_REQUEST, ApiErrorEnum.GENERIC, exception.getMessage());
    }
}
