package sanchez.sergio.handlers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import sanchez.sergio.persistence.entities.Notification;
import sanchez.sergio.persistence.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sergio
 */
@Component("alternateHandler")
public class AlternateHandler {
    
    private static Logger logger = LoggerFactory.getLogger(AlternateHandler.class);
    
    @Autowired
    private NotificationRepository notificationRepository;
   
    @RabbitListener(queues="#{rabbitCustomProperties.alternateExchange.queue}")
    public void handleMessage(
            @Payload Notification notification, 
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String receivedRoutingKey) {
        logger.info("RECEIVED_ROUTING_KEY -> " + receivedRoutingKey);
        notificationRepository.save(notification);
    }
    
}
