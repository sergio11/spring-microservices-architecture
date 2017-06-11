package sanchez.sergio.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import sanchez.sergio.notifications.messages.admin.UsersManadgementMessage;
import sanchez.sergio.notifications.messages.admin.UsersManadgementMessageType;
import sanchez.sergio.service.IUserNotificationService;

/**
 * @author sergio
 */
@Service
public class UserNotificationService implements IUserNotificationService {
    
    @Autowired
    @Qualifier("usersManadgement") 
    private MessageChannel usersManadgement;
    
    @Autowired
    @Qualifier("userNotifications") 
    private MessageChannel userNotifications;
    
    private <T> Message<T> buildMessage(T payload){
        return MessageBuilder.withPayload(payload).build();
    }
    

    @Override
    public boolean notifyUserConnected(String username) {
        Message<UsersManadgementMessage> message = buildMessage(new UsersManadgementMessage(username, UsersManadgementMessageType.USER_CONNECTED));
        return usersManadgement.send(message);
    }

    @Override
    public boolean notifyUserDisconnected(String username) {
        Message<UsersManadgementMessage> message = buildMessage(new UsersManadgementMessage(username, UsersManadgementMessageType.USER_DISCONNECTED));
        return usersManadgement.send(message);
    }
    
}
