package sanchez.sergio.persistence.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sanchez.sergio.persistence.entities.Notification;

/**
 * @author sergio
 */
public interface NotificationRepository extends JpaRepository<Notification, Long>{
    List<Notification> findByUsername(String username);
}
