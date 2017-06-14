package sanchez.sergio.services.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sanchez.sergio.persistence.entities.Device;
import sanchez.sergio.persistence.entities.DeviceGroup;
import sanchez.sergio.persistence.repositories.DeviceGroupRepository;
import sanchez.sergio.services.IDeviceGroupsService;
import java.util.Set;
import sanchez.sergio.persistence.repositories.DeviceRepository;

/**
 * @author sergio
 */
@Service
public class DeviceGroupsServiceImpl implements IDeviceGroupsService {
    
    @Autowired
    private DeviceGroupRepository deviceGroupRepository;
    
    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public DeviceGroup createDeviceGroup(String name, String key, String username) {
        return deviceGroupRepository.saveAndFlush(new DeviceGroup(name, key, username));
    }

    @Override
    public DeviceGroup createDeviceGroup(String name, String key, String username, Set<Device> devices) {
       return deviceGroupRepository.saveAndFlush(new DeviceGroup(name, key, username, devices));
    }

    @Override
    public List<Device> getDevicesFromGroup(String groupName) {
        return deviceRepository.findByDeviceGroupNotificationKeyName(groupName);
    }

    @Override
    public String getNotificationKey(String groupName) {
        return deviceGroupRepository.getNotificationKey(groupName);
    }

    @Override
    public Optional<DeviceGroup> getDeviceGroupByName(String name) {
        return deviceGroupRepository.findByNotificationKeyName(name);
    }

    @Override
    public Device addDeviceToGroup(String registrationToken, DeviceGroup deviceGroup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean removeDeviceFromGroup(String registrationToken, DeviceGroup deviceGroup) {
        boolean result = deviceGroup.getDevices().removeIf(device -> device.getRegistrationToken().equals(registrationToken));
        if(result)
            deviceGroupRepository.saveAndFlush(deviceGroup);
        return result;
    }
    
    
}
