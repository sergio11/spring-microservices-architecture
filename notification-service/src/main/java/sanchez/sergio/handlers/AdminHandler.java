package sanchez.sergio.handlers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.Header;
import sanchez.sergio.domain.UserHolder;
import sanchez.sergio.notifications.messages.admin.UsersManadgementMessage;
import sanchez.sergio.notifications.messages.admin.UsersManadgementMessageType.UsersManadgementVisitor;

/**
 * @author sergio
 */
@Component("adminHandler")
public class AdminHandler implements UsersManadgementVisitor {
    
    @Autowired
    private ApplicationContext appCtx;
    
    private Map<String, UsersHandler> handlers = new HashMap<String, UsersHandler>();

    private static Logger logger = LoggerFactory.getLogger(AdminHandler.class);
    
    @RabbitListener(queues="#{rabbitCustomProperties.adminExchange.queues['users'].name}")
    public void handleMessage(@Payload UsersManadgementMessage message,  @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String rk) {
        logger.info("Message -> " + message);
        logger.info("Routing Key -> " + rk);
        message.getType().accept(this, message.getUsername());
    }

    @Override
    public void visitUserConnected(String username) {
        if (!handlers.containsKey(username)) {
            logger.info("Declare new queue for user: " + username);
            UserHolder.setUser(username);
            UsersHandler userHandler = appCtx.getBean(UsersHandler.class);
            UserHolder.clearUser();
            handlers.put(username, userHandler);
        }
    }

    @Override
    public void visitUserDisconnected(String username) {
        logger.info("Remove queue for user: " + username );
        handlers.remove(username);
    }
}
