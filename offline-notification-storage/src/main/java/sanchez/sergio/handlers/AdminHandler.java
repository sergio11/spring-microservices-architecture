package sanchez.sergio.handlers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sanchez.sergio.messages.admin.UsersManadgementMessage;

/**
 * @author sergio
 */
@Component("adminHandler")
public class AdminHandler {
    
    private static Logger logger = LoggerFactory.getLogger(AdminHandler.class);
    
    @RabbitListener(queues="#{rabbitCustomProperties.adminExchange.queues['users'].name}")
    public void handleMessage(@Payload UsersManadgementMessage message) {
        logger.info("Message -> " + message);
    }
}
