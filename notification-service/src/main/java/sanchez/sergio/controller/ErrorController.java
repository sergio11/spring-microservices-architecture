package sanchez.sergio.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sanchez.sergio.exception.DeviceAddToGroupFailedException;
import sanchez.sergio.exception.DeviceGroupCreateFailedException;
import sanchez.sergio.exception.DeviceGroupNotFoundException;
import sanchez.sergio.rest.BaseErrorRestController;
import sanchez.sergio.rest.response.APIResponse;
import sanchez.sergio.util.NotificationResponseCode;

/**
 *
 * @author sergio
 */
@ControllerAdvice
public class ErrorController extends BaseErrorRestController {
    
    private static Logger logger = LoggerFactory.getLogger(ErrorController.class);
    
    @ExceptionHandler(DeviceGroupNotFoundException.class)
    public ResponseEntity<APIResponse<String>>  deviceGroupNotFound(final DeviceGroupNotFoundException exception){
        return createAndSendResponse(HttpStatus.BAD_REQUEST, NotificationResponseCode.DEVICE_GROUP_NOT_FOUND, exception.getMessage());
    }
    
    @ExceptionHandler(DeviceGroupCreateFailedException.class)
    public ResponseEntity<APIResponse<String>>  deviceGroupCreateFailed(final DeviceGroupCreateFailedException exception){
        return createAndSendResponse(HttpStatus.INTERNAL_SERVER_ERROR, NotificationResponseCode.DEVICES_GROUP_CREATE_FAILED, exception.getMessage());
    }
    
    @ExceptionHandler(DeviceAddToGroupFailedException.class)
    public ResponseEntity<APIResponse<String>>  deviceAddToGroupFailed(final DeviceAddToGroupFailedException exception){
        return createAndSendResponse(HttpStatus.INTERNAL_SERVER_ERROR, NotificationResponseCode.DEVICE_ADD_TO_GROUP_FAILED, exception.getMessage());
    }
}
