package sanchez.sergio.services;

import java.util.List;
import java.util.Optional;
import sanchez.sergio.persistence.entities.Device;
import java.util.Set;
import sanchez.sergio.persistence.entities.DeviceGroup;

/**
 * @author sergio
 */
public interface IDeviceGroupsService {

    Optional<DeviceGroup> getDeviceGroupByName(String name);
    DeviceGroup createDeviceGroup(String name, String key, String username);
    DeviceGroup createDeviceGroup(String name, String key, String username, Set<Device> devices);
    Device addDeviceToGroup(String registrationToken, DeviceGroup deviceGroup);
    Boolean removeDeviceFromGroup(String registrationToken, DeviceGroup deviceGroup);
    List<Device> getDevicesFromGroup(String groupName);
    String getNotificationKey(String groupName);
    
}
