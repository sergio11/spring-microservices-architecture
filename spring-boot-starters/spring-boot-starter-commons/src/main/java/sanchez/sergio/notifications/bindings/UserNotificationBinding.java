package sanchez.sergio.notifications.bindings;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 *
 * @author sergio
 */
public interface UserNotificationBinding {
    
    String CHANNEL_NAME = "userNotifications";
  
    @Output(UserNotificationBinding.CHANNEL_NAME)
    MessageChannel userNotifications();
}
