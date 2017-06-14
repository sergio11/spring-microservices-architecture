package sanchez.sergio.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sanchez.sergio.persistence.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sanchez.sergio.exceptions.UserNotFoundException;
import sanchez.sergio.persistence.entities.Notification;
import sanchez.sergio.rest.ApiHelper;
import sanchez.sergio.rest.response.APIResponse;
import sanchez.sergio.security.userdetails.CommonUserDetailsAware;
import sanchez.sergio.service.IAuthenticationService;
import sanchez.sergio.util.NotificationResponseCode;

/**
 * @author sergio
 */
@Api
@RestController
@RequestMapping("/v1/api/")
public class NotificationController {
    
    private static Logger logger = LoggerFactory.getLogger(NotificationController.class);
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private IAuthenticationService authenticationService;
   
    @GetMapping(path = "/notifications/{username}")
    @ApiOperation(value = "getNotifications", nickname = "getNotifications", notes="Get Notifications", response = ResponseEntity.class)
    public ResponseEntity<APIResponse<List<Notification>>> getNotifications(@ApiParam(value = "userId", required = true) @PathVariable String username) throws Throwable {
        return Optional.ofNullable(notificationRepository.findByUsername(username))
                .map(notifications -> ApiHelper.<List<Notification>>createAndSendResponse(NotificationResponseCode.ALL_NOTIFICATIONS, notifications, HttpStatus.OK))
                .orElseThrow(() -> {throw new UserNotFoundException(); });
    }
    
    @GetMapping(path = "/notifications/self")
    @ApiOperation(value = "getSelfNotifications", nickname = "getSelfNotifications", notes="Get Self Notifications", response = ResponseEntity.class)
    public ResponseEntity<APIResponse<List<Notification>>> getNotifications() throws Throwable {
        CommonUserDetailsAware principal = authenticationService.getPrincipal();
        logger.debug("username -> " + principal.getUsername());
        return Optional.ofNullable(notificationRepository.findByUsername(principal.getUsername()))
                .map(notifications -> ApiHelper.<List<Notification>>createAndSendResponse(NotificationResponseCode.SELF_NOTIFICATIONS, notifications, HttpStatus.OK))
                .orElseThrow(() -> {throw new UserNotFoundException(); });
    }
    
}
