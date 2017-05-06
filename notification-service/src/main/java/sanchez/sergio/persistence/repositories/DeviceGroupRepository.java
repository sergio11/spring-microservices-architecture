package sanchez.sergio.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sanchez.sergio.persistence.entities.DeviceGroup;

/**
 * @author sergio
 */
public interface DeviceGroupRepository extends JpaRepository<DeviceGroup, Long>{
    
}
