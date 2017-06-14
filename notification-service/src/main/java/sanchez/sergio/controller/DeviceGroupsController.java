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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sanchez.sergio.persistence.entities.Device;
import sanchez.sergio.persistence.entities.DeviceGroup;
import sanchez.sergio.security.userdetails.CommonUserDetailsAware;
import sanchez.sergio.services.IDeviceGroupsService;
import sanchez.sergio.services.IPushNotificationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import sanchez.sergio.exception.DeviceAddToGroupFailedException;
import sanchez.sergio.exception.DeviceGroupCreateFailedException;
import sanchez.sergio.exception.DeviceGroupNotFoundException;
import sanchez.sergio.rest.ApiHelper;
import sanchez.sergio.rest.response.APIResponse;
import sanchez.sergio.service.IAuthenticationService;
import sanchez.sergio.util.NotificationResponseCode;
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
    
    @Autowired
    private IAuthenticationService authenticationService;
   
    @GetMapping(path = "/{name}/all")
    @ApiOperation(value = "getDevicesIntoGroup", nickname = "getDevicesIntoGroup", notes="Get all devices registered in the group", response = ResponseEntity.class)
    public ResponseEntity<APIResponse<List<Device>>> getDevicesIntoGroup(@ApiParam(value = "name", required = true) @PathVariable String name) throws Throwable {
        return Optional.ofNullable(deviceGroupsService.getDevicesFromGroup(name))
                .map(devices -> ApiHelper.<List<Device>>createAndSendResponse(NotificationResponseCode.ALL_DEVICES_INTO_GROUP, devices, HttpStatus.OK))
                .<DeviceGroupNotFoundException>orElseThrow(() -> {throw new DeviceGroupNotFoundException(); });
    }
    
    @PutMapping(path = "/{name}/create")
    @ApiOperation(value = "createDevicesGroup", nickname = "createDevicesGroup", notes="Create Device Group", response = ResponseEntity.class)
    public ResponseEntity<APIResponse<DeviceGroup>> createDevicesGroup(
            @ApiParam(value = "name", required = true) @PathVariable String name
    ) throws InterruptedException, ExecutionException, Throwable {
        CommonUserDetailsAware principal = authenticationService.getPrincipal();
        return pushNotificationsService.createNotificationGroup(name)
                .handle((groupKey, ex) -> 
                    Optional.ofNullable(deviceGroupsService.createDeviceGroup(name, groupKey, principal.getUsername()))
                            .map(deviceGroup -> ApiHelper.<DeviceGroup>createAndSendResponse(NotificationResponseCode.DEVICES_GROUP_CREATED, deviceGroup, HttpStatus.OK))
                            .<DeviceGroupCreateFailedException>orElseThrow(() -> { throw new DeviceGroupCreateFailedException(); })).get();
    }
    
    
    @PutMapping(path = "/{group}/devices/{token}/add")
    @ApiOperation(value = "addDeviceToGroup", nickname = "addDeviceToGroup", notes="Add Device To Group", response = ResponseEntity.class)
    public ResponseEntity<APIResponse<Device>> addDeviceToGroup(
            @ApiParam(value = "group", required = true) @PathVariable String group,
            @ApiParam(value = "token", required = true) @PathVariable String token
    ){
        return deviceGroupsService.getDeviceGroupByName(group)
                .map(deviceGroup -> pushNotificationsService.addDeviceToGroup(deviceGroup.getNotificationKeyName(), deviceGroup.getNotificationKey(), token)
                    .handle((groupKey, ex) -> 
                            Optional.ofNullable(deviceGroupsService.addDeviceToGroup(token, deviceGroup))
                            .map(device -> ApiHelper.<Device>createAndSendResponse(NotificationResponseCode.DEVICE_ADDED_TO_GROUP, device, HttpStatus.OK))
                            .<DeviceAddToGroupFailedException>orElseThrow(() -> { throw new DeviceAddToGroupFailedException(); })))
                .map(completableFuture -> Unthrow.wrap(() -> completableFuture.get()))
                .<DeviceGroupNotFoundException>orElseThrow(() -> {throw new DeviceGroupNotFoundException(); });
    }
    
    @DeleteMapping(path = "/{group}/devices/{token}/delete")
    @ApiOperation(value = "deleteDeviceFromGroup", nickname = "deleteDeviceFromGroup", notes="Delete Device From Group", response = ResponseEntity.class)
    public ResponseEntity<Device> deleteDeviceFromGroup(
            @ApiParam(value = "group", required = true) @PathVariable String group,
            @ApiParam(value = "token", required = true) @PathVariable String token
    ){
        return deviceGroupsService.getDeviceGroupByName(group)
                .map(deviceGroup -> pushNotificationsService.removeDeviceFromGroup(
                        deviceGroup.getNotificationKeyName(), deviceGroup.getNotificationKey(), token)
                    .handle((groupKey, ex) -> 
                            Optional.ofNullable(deviceGroupsService.addDeviceToGroup(token, deviceGroup))
                            .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND))))
                .map(completableFuture -> Unthrow.wrap(() -> completableFuture.get()))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
}
