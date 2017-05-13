package sanchez.sergio.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sanchez.sergio.exceptions.UsernameAlredyExistsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import sanchez.sergio.exceptions.EmailAlredyExistsException;
import sanchez.sergio.rest.ApiErrorEnum;
import sanchez.sergio.rest.BaseErrorRestController;

/**
 * @author sergio
 */
@ControllerAdvice
public class ErrorController extends BaseErrorRestController {
    
    private static Logger logger = LoggerFactory.getLogger(ErrorController.class);
    
    @ExceptionHandler(UsernameAlredyExistsException.class)
    public ResponseEntity  usernameAlredyExists(final UsernameAlredyExistsException exception){
        return createAndSendResponse(HttpStatus.BAD_REQUEST, ApiErrorEnum.BAD_REQUEST, exception.getMessage());
    }
    
    @ExceptionHandler(EmailAlredyExistsException.class)
    public ResponseEntity emailAlredyExists(final EmailAlredyExistsException exception){
        return createAndSendResponse(HttpStatus.BAD_REQUEST, ApiErrorEnum.BAD_REQUEST, exception.getMessage());
    }
    
}
