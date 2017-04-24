package sanchez.sergio.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sanchez.sergio.config.properties.RabbitMQProperties;
import sanchez.sergio.delegates.INotificationDelegate;

/**
 * RabbitMQ Configuration
 * @author sergio
 */
@Configuration
@EnableConfigurationProperties(RabbitMQProperties.class)
public class RabbitMQConfig {
    
    @Autowired
    private RabbitMQProperties rabbitMQProperties;
            
            
    @Bean
    @Qualifier("alternateQueue")
    Queue alternateQueue() {
        return new Queue(rabbitMQProperties.getAlternateExchange().getQueue(), true);
    }
    
    @Bean
    @Qualifier("deadLettersQueue")
    Queue deadLettersQueue() {
        return new Queue(rabbitMQProperties.getDeadlettersExchange().getQueue(), true);
    }
    
    @Bean
    @Qualifier("alternateExchange")
    FanoutExchange alternateExchange() {
        FanoutExchange fanoutExchange = new FanoutExchange(rabbitMQProperties.getAlternateExchange().getName());
        fanoutExchange.setInternal(true);
        return fanoutExchange;
    }
    
    @Bean
    @Qualifier("deadLettersExchange")
    FanoutExchange deadLettersExchange() {
        FanoutExchange fanoutExchange = new FanoutExchange(rabbitMQProperties.getDeadlettersExchange().getName());
        fanoutExchange.setInternal(true);
        return fanoutExchange;
    }
    
    @Bean
    Binding alternateBinding(@Qualifier("alternateQueue") Queue queue, @Qualifier("alternateExchange") FanoutExchange alternateExchange) {
        return BindingBuilder.bind(queue).to(alternateExchange);
    }
    
    @Bean
    Binding deadLettersBinding(@Qualifier("deadLettersQueue") Queue queue, @Qualifier("deadLettersExchange") FanoutExchange deadLettersExchange) {
        return BindingBuilder.bind(queue).to(deadLettersExchange);
    }
    
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMQProperties.getHost());
        connectionFactory.setUsername(rabbitMQProperties.getUsername());
        connectionFactory.setPassword(rabbitMQProperties.getPassword());
        return connectionFactory;
    }
    
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }
    
    // Messages Listener Adapter
    
    /*
        Message listener adapter that delegates the handling of messages to target 
        listener methods via reflection.
    */
    
    @Bean(name="alternateListenerAdapter")
    MessageListenerAdapter alternateListenerAdapter(@Qualifier("alternateDelegate") INotificationDelegate alternateDelegate) {
        return new MessageListenerAdapter(alternateDelegate, "receiveMessage");
    }
    
    @Bean(name="deadLettersListenerAdapter")
    MessageListenerAdapter deadLettersListenerAdapter(@Qualifier("deadLettersDelegate") INotificationDelegate deadLettersDelegate) {
        return new MessageListenerAdapter(deadLettersDelegate, "receiveMessage");
    }
    
    @Bean
    SimpleMessageListenerContainer alternateListenerContainer(
            ConnectionFactory connectionFactory,
            @Qualifier("alternateListenerAdapter") MessageListenerAdapter alternateListenerAdapter,
            @Qualifier("alternateQueue") Queue alternateQueue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(alternateQueue);
        container.setMessageListener(alternateListenerAdapter);
        return container;
    }
    
    @Bean
    SimpleMessageListenerContainer deadLettersListenerContainer(
            ConnectionFactory connectionFactory,
            @Qualifier("deadLettersListenerAdapter") MessageListenerAdapter deadLettersListenerAdapter,
            @Qualifier("deadLettersQueue") Queue deadLettersQueue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(deadLettersQueue);
        container.setMessageListener(deadLettersListenerAdapter);
        return container;
    }
}
