package sanchez.sergio.persistence.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import sanchez.sergio.persistence.entities.Device;

/**
 * @author sergio
 */
public interface DeviceRepository extends JpaRepository<Device, Long>{
    List<Device> findByDeviceGroupNotificationKeyName(String groupName);
    Optional<Device> findByRegistrationToken(String registrationToken);
}
