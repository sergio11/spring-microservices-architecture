package sanchez.sergio.delegates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sanchez.sergio.domain.Notification;
import sanchez.sergio.persistence.repositories.NotificationRepository;

/**
 * @author sergio
 */
@Component("alternateDelegate")
public class AlternateDelegate implements INotificationDelegate {
    
    @Autowired
    private NotificationRepository notificationRepository;
    

    @Override
    public void handleMessage(Notification notification) {
        notificationRepository.save(notification);
    }
    
}
