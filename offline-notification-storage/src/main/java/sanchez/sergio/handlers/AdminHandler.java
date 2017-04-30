package sanchez.sergio.handlers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sanchez.sergio.messages.admin.UsersManadgementMessage;
import sanchez.sergio.messages.admin.UsersManadgementMessageType.UsersManadgementVisitor;

/**
 * @author sergio
 */
@Component("adminHandler")
public class AdminHandler implements UsersManadgementVisitor {
    
    private static Logger logger = LoggerFactory.getLogger(AdminHandler.class);
    
    @RabbitListener(queues="#{rabbitCustomProperties.adminExchange.queues['users'].name}")
    public void handleMessage(@Payload UsersManadgementMessage message) {
        logger.info("Message -> " + message);
        message.getType().accept(this, message.getId());
    }

    @Override
    public void visitUserConnected(Long idUser) {
        logger.info("Declare new queue for user: " + idUser );
    }

    @Override
    public void visitUserDisconnected(Long idUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
