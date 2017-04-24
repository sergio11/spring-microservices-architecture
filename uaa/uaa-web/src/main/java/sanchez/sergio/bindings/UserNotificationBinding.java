package sanchez.sergio.bindings;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;

/**
 *
 * @author sergio
 */
public interface UserNotificationBinding extends Source{
    
    String OUTPUT = "users.direct";
    
    @Output(UserNotificationBinding.OUTPUT)
    MessageChannel notifications();
}
