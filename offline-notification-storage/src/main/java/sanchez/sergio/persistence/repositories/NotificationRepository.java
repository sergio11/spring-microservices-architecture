package sanchez.sergio.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sanchez.sergio.domain.Notification;

/**
 * @author sergio
 */
public interface NotificationRepository extends JpaRepository<Notification, Long>{}
