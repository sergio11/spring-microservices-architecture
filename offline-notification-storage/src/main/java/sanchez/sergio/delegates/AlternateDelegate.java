package sanchez.sergio.delegates;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import sanchez.sergio.domain.Notification;
import sanchez.sergio.persistence.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sergio
 */
@Component("alternateDelegate")
public class AlternateDelegate implements INotificationDelegate {
    
    private static Logger logger = LoggerFactory.getLogger(AlternateDelegate.class);
    
    @Autowired
    private NotificationRepository notificationRepository;
   
    @Override
    @RabbitListener(queues="#{rabbitCustomProperties.alternateExchange.queue}")
    public void handleMessage(
            @Payload Notification notification, 
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String receivedRoutingKey) {
        logger.info("RECEIVED_ROUTING_KEY -> " + receivedRoutingKey);
        notificationRepository.save(notification);
    }
    
}
