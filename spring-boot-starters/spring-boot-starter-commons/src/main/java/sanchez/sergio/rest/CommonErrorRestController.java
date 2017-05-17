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
import sanchez.sergio.rest.response.APIResponse;
import sanchez.sergio.rest.response.CommonResponseCode;

/**
 *
 * @author sergio
 */
@ControllerAdvice
public class CommonErrorRestController extends BaseErrorRestController {

    @ExceptionHandler(ResourceNotFoundException.class)
    protected @ResponseBody
    ResponseEntity<APIResponse<String>> handleNoteNotFoundException(ResourceNotFoundException resourceNotFound, HttpServletRequest request, HttpServletResponse response) {
        return createAndSendResponse(HttpStatus.NOT_FOUND, CommonResponseCode.RESOURCE_NOT_FOUND, resourceNotFound.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected @ResponseBody
    ResponseEntity<APIResponse<String>> handleAccessDeniedException(AccessDeniedException accessDeniedException, HttpServletRequest request, HttpServletResponse response) {
        return createAndSendResponse(HttpStatus.UNAUTHORIZED, CommonResponseCode.ACCESS_DENIED, accessDeniedException.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    protected @ResponseBody
    ResponseEntity<APIResponse<String>> handleSecurityException(SecurityException exception, HttpServletRequest request, HttpServletResponse response) {
        return createAndSendResponse(HttpStatus.UNAUTHORIZED, CommonResponseCode.SECURITY_ERROR, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected @ResponseBody
    ResponseEntity<APIResponse<String>> handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        return createAndSendResponse(HttpStatus.BAD_REQUEST, CommonResponseCode.GENERIC_ERROR, exception.getMessage());
    }
}
