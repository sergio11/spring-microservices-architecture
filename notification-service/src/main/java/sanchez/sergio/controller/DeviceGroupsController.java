package sanchez.sergio.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sanchez.sergio.persistence.entities.Device;
import sanchez.sergio.persistence.entities.DeviceGroup;
import sanchez.sergio.security.CommonUserDetailsAware;
import sanchez.sergio.services.IDeviceGroupsService;
import sanchez.sergio.services.IPushNotificationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sanchez.sergio.utils.Unthrow;

/**
 * @author sergio
 */
@Api
@RestController
@RequestMapping("/v1/device-groups")
public class DeviceGroupsController {
    
     private static Logger logger = LoggerFactory.getLogger(DeviceGroupsController.class);
    
    @Autowired
    private IDeviceGroupsService deviceGroupsService;
    
    @Autowired
    private IPushNotificationsService pushNotificationsService;
   
    @GetMapping(path = "/{name}/all")
    @ApiOperation(value = "getDevicesIntoGroup", nickname = "getDevicesIntoGroup", notes="Get all devices registered in the group", response = ResponseEntity.class)
    public ResponseEntity<List<Device>> getDevicesIntoGroup(@ApiParam(value = "name", required = true) @PathVariable String name) {
        return Optional.ofNullable(deviceGroupsService.getDevicesFromGroup(name))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping(path = "/{name}/create")
    @ApiOperation(value = "createDevicesGroup", nickname = "createDevicesGroup", notes="Create Device Group", response = ResponseEntity.class)
    public ResponseEntity<DeviceGroup> createDevicesGroup(
            @ApiParam(value = "name", required = true) @PathVariable String name,
            @AuthenticationPrincipal CommonUserDetailsAware<Long> principal
    ) throws InterruptedException, ExecutionException {
        return pushNotificationsService.createNotificationGroup(name)
                .handle((groupKey, ex) -> 
                    Optional.ofNullable(deviceGroupsService.createDeviceGroup(name, groupKey, principal.getUserId()))
                            .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND))).get();
    }
    
    
    @PutMapping(path = "/{group}/devices/{token}/add")
    @ApiOperation(value = "addDeviceToGroup", nickname = "addDeviceToGroup", notes="Add Device To Group", response = ResponseEntity.class)
    public ResponseEntity<Device> addDeviceToGroup(
            @ApiParam(value = "group", required = true) @PathVariable String group,
            @ApiParam(value = "token", required = true) @PathVariable String token,
            @AuthenticationPrincipal CommonUserDetailsAware<Long> principal
    ){
        return deviceGroupsService.getDeviceGroupByName(group)
                .map(deviceGroup -> pushNotificationsService.addDeviceToGroup(deviceGroup.getNotificationKeyName(), deviceGroup.getNotificationKey(), token)
                    .handle((groupKey, ex) -> 
                            Optional.ofNullable(deviceGroupsService.addDeviceToGroup(token, deviceGroup))
                            .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND))))
                .map(completableFuture -> Unthrow.wrap(() -> completableFuture.get()))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                
    }
    
}
