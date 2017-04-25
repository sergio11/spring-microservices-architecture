package sanchez.sergio.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import sanchez.sergio.config.properties.RabbitMQProperties;

/**
 * RabbitMQ Configuration
 * @author sergio
 */
@Configuration
@EnableConfigurationProperties(RabbitMQProperties.class)
public class RabbitMQConfig implements RabbitListenerConfigurer {
    
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
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        return rabbitTemplate;
    }
    
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        return factory;
    }
    
    @Bean
    public DefaultMessageHandlerMethodFactory myHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(new MappingJackson2MessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rler) {
        rler.setMessageHandlerMethodFactory(myHandlerMethodFactory());
    }
}
