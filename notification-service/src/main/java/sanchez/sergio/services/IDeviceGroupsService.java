package sanchez.sergio.services;

import java.util.List;
import sanchez.sergio.persistence.entities.Device;
import java.util.Set;
import sanchez.sergio.persistence.entities.DeviceGroup;

/**
 * @author sergio
 */
public interface IDeviceGroupsService {

    DeviceGroup createDeviceGroup(String name, String key, Long userId);
    DeviceGroup createDeviceGroup(String name, String key, Long userId, Set<Device> devices);
    List<Device> getDevicesFromGroup(String groupName);
    
}
