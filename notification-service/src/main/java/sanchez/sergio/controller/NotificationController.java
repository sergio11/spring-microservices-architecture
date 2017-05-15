package sanchez.sergio.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sanchez.sergio.persistence.repositories.NotificationRepository;
import sanchez.sergio.security.CommonUserDetailsAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
   
    @GetMapping(path = "/notifications/{userId}")
    @ApiOperation(value = "getNotifications", nickname = "getNotifications", notes="Get Notifications", response = ResponseEntity.class)
    public ResponseEntity getNotifications(@ApiParam(value = "userId", required = true) @PathVariable Long userId) {
        return Optional.ofNullable(notificationRepository.findByUserId(userId))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping(path = "/notifications/self")
    @ApiOperation(value = "getSelfNotifications", nickname = "getSelfNotifications", notes="Get Self Notifications", response = ResponseEntity.class)
    public ResponseEntity getNotifications(@AuthenticationPrincipal CommonUserDetailsAware<Long> principal) {
        logger.debug("username -> " + principal.getUsername());
        return Optional.ofNullable(notificationRepository.findByUserId(principal.getUserId()))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
}
