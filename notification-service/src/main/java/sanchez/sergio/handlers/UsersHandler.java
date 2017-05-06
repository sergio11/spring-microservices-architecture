package sanchez.sergio.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import sanchez.sergio.persistence.entities.Notification;

/**
 *
 * @author sergio
 */
@Component("usersHandler")
@Scope(value = "prototype")
public class UsersHandler {
    
    private static Logger logger = LoggerFactory.getLogger(UsersHandler.class);
    
    @RabbitListener(bindings
            = @QueueBinding(
                    value = @Queue(
                            value = "#{'queue_' + @userHolder.user}",
                            durable = "false",
                            autoDelete = "true",
                            arguments = {
                                @Argument(
                                        name = "x-message-ttl",
                                        value = "#{rabbitCustomProperties.directExchange.queueArguments['x-message-ttl']}",
                                        type = "java.lang.Integer"
                                )
                                ,
                                @Argument(
                                        name = "x-expires",
                                        value = "#{rabbitCustomProperties.directExchange.queueArguments['x-expires']}",
                                        type = "java.lang.Integer"
                                )
                                ,
                                @Argument(
                                        name = "x-dead-letter-exchange",
                                        value = "#{rabbitCustomProperties.directExchange.queueArguments['x-dead-letter-exchange']}",
                                        type = "java.lang.String"
                                )
                            }
                    ),
                    exchange = @Exchange(
                            value = "#{rabbitCustomProperties.directExchange.name}",
                            type = ExchangeTypes.DIRECT,
                            durable = "#{rabbitCustomProperties.directExchange.durable}",
                            autoDelete = "#{rabbitCustomProperties.directExchange.autoDelete}",
                            arguments = {
                                @Argument(
                                        name = "alternate-exchange",
                                        value = "#{rabbitCustomProperties.directExchange.arguments['alternate-exchange']}",
                                        type = "java.lang.String"
                                )
                            }
                    ),
                    key = "#{'user_' + @userHolder.user}")
    )
    public void handleMessage(@Payload Notification notification) {
        logger.info("Notification Received : " + notification);
    }
    
}
