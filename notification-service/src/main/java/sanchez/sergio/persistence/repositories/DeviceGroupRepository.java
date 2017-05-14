package sanchez.sergio.persistence.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sanchez.sergio.persistence.entities.DeviceGroup;

/**
 * @author sergio
 */
public interface DeviceGroupRepository extends JpaRepository<DeviceGroup, Long>{
    @Query("SELECT dg.notificationKey  FROM DeviceGroup dg WHERE dg.notificationKeyName=:notificationKeyName")
    String getNotificationKey(@Param("notificationKeyName") String notificationKeyName);
    Optional<DeviceGroup> findByNotificationKeyName(String notificationKeyName);
}
