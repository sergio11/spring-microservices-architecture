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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sanchez.sergio.domain.CreateDeviceGroupRequest;
import sanchez.sergio.persistence.entities.Device;
import sanchez.sergio.persistence.entities.DeviceGroup;
import sanchez.sergio.services.IDeviceGroupsService;
import sanchez.sergio.services.IPushNotificationsService;

/**
 * @author sergio
 */
@Api
@RestController
@RequestMapping("/v1/device-groups")
public class DeviceGroupsController {
    
    @Autowired
    private IDeviceGroupsService deviceGroupsService;
    @Autowired
    private IPushNotificationsService pushNotificationsService;
   
    @GetMapping(path = "/all/{name}")
    @ApiOperation(value = "getDevicesIntoGroup", nickname = "getDevicesIntoGroup", notes="Get all devices registered in the group", response = ResponseEntity.class)
    public ResponseEntity<List<Device>> getDevicesIntoGroup(@ApiParam(value = "name", required = true) @PathVariable String name) {
        return Optional.ofNullable(deviceGroupsService.getDevicesFromGroup(name))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PostMapping(path = "/create")
    @ApiOperation(value = "createDevicesGroup", nickname = "createDevicesGroup", notes="Create Device Group", response = ResponseEntity.class)
    public ResponseEntity<DeviceGroup> createDevicesGroup(@ApiParam(value = "CreateDeviceGroupRequest", required = true) @RequestBody CreateDeviceGroupRequest createDeviceGroupRequest) throws InterruptedException, ExecutionException {
        return pushNotificationsService.createNotificationGroup(createDeviceGroupRequest.getNotificationGroupName())
                .handle((groupKey, ex) -> 
                    Optional.ofNullable(deviceGroupsService.createDeviceGroup(createDeviceGroupRequest.getNotificationGroupName(), groupKey, createDeviceGroupRequest.getUserId()))
                            .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND))).get();
    }
    
}
