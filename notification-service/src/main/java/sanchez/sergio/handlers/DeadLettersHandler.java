package sanchez.sergio.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sanchez.sergio.persistence.entities.Notification;
import sanchez.sergio.persistence.repositories.NotificationRepository;

/**
 * @author sergio
 */
@Component("deadLettersHandler")
public class DeadLettersHandler {
    
    @Autowired
    private NotificationRepository notificationRepository;

    public void handleMessage(Notification notification, String receivedRoutingKey) {
        notificationRepository.save(notification);
    }
    
}
