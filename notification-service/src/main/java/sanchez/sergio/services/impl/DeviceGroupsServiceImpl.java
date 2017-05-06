package sanchez.sergio.services.impl;

import java.util.List;
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
    public DeviceGroup createDeviceGroup(String name, String key, Long userId) {
        return deviceGroupRepository.saveAndFlush(new DeviceGroup(name, key, userId));
    }

    @Override
    public DeviceGroup createDeviceGroup(String name, String key, Long userId, Set<Device> devices) {
       return deviceGroupRepository.saveAndFlush(new DeviceGroup(name, key, userId, devices));
    }

    @Override
    public List<Device> getDevicesFromGroup(String groupName) {
        return deviceRepository.findByNotificationKeyName(groupName);
    }
}
