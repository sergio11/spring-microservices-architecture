package sanchez.sergio.persistence.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sanchez.sergio.domain.Notification;

/**
 * @author sergio
 */
public interface NotificationRepository extends JpaRepository<Notification, Long>{
    List<Notification> findByUserId(Long userId);
}
