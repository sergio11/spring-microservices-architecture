package sanchez.sergio.handlers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import sanchez.sergio.domain.UserHolder;
import sanchez.sergio.messages.admin.UsersManadgementMessage;
import sanchez.sergio.messages.admin.UsersManadgementMessageType.UsersManadgementVisitor;

/**
 * @author sergio
 */
@Component("adminHandler")
public class AdminHandler implements UsersManadgementVisitor {
    
    @Autowired
    private ApplicationContext appCtx;
    
    private Map<Long, UsersHandler> handlers = new HashMap<Long, UsersHandler>();

    private static Logger logger = LoggerFactory.getLogger(AdminHandler.class);
    
    @RabbitListener(queues="#{rabbitCustomProperties.adminExchange.queues['users'].name}")
    public void handleMessage(@Payload UsersManadgementMessage message) {
        logger.info("Message -> " + message);
        message.getType().accept(this, message.getId());
    }

    @Override
    public void visitUserConnected(Long idUser) {
        if (!handlers.containsKey(idUser)) {
            logger.info("Declare new queue for user: " + idUser);
            UserHolder.setUser(idUser);
            UsersHandler userHandler = appCtx.getBean(UsersHandler.class);
            UserHolder.clearUser();
            handlers.put(idUser, userHandler);
        }
    }

    @Override
    public void visitUserDisconnected(Long idUser) {
        logger.info("Remove queue for user: " + idUser );
        handlers.remove(idUser);
    }
}
