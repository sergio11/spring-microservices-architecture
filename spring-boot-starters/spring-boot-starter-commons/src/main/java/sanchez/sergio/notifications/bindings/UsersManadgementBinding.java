package sanchez.sergio.notifications.bindings;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 *
 * @author sergio
 */
public interface UsersManadgementBinding {
    
    String CHANNEL_NAME = "usersManadgement";
  
    @Output(UsersManadgementBinding.CHANNEL_NAME)
    MessageChannel usersManadgement();
}
