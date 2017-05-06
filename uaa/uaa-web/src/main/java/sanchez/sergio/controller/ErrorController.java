package sanchez.sergio.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sanchez.sergio.exceptions.UsernameAlredyExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import sanchez.sergio.exceptions.EmailAlredyExistsException;

/**
 * @author sergio
 */
@ControllerAdvice
public class ErrorController {
    
    private static Logger logger = LoggerFactory.getLogger(ErrorController.class);
    
    @ExceptionHandler(UsernameAlredyExistsException.class)
    public ResponseEntity  usernameAlredyExists(final UsernameAlredyExistsException exception){
        HttpHeaders textPlainHeaders = new HttpHeaders();
        textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(textPlainHeaders)
                .body(exception.getUser().getUsername() + " alredy exists");
                
    }
    
    @ExceptionHandler(EmailAlredyExistsException.class)
    public ResponseEntity emailAlredyExists(final EmailAlredyExistsException exception){
        HttpHeaders textPlainHeaders = new HttpHeaders();
        textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .headers(textPlainHeaders)
                .body("User with email " + exception.getUser().getEmail() + " alredy exists");
                
    }
    
}
