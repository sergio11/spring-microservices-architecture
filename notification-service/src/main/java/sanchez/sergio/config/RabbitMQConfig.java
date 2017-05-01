package sanchez.sergio.config;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import sanchez.sergio.config.properties.RabbitCustomProperties;

/**
 * RabbitMQ Configuration
 * @author sergio
 */
@Configuration
@EnableConfigurationProperties(RabbitCustomProperties.class)
@EnableRabbit
public class RabbitMQConfig implements RabbitListenerConfigurer {
    
    @Autowired
    private RabbitCustomProperties rabbitCustomProperties;
    
    /**
     * Exchanges
     * =====================
     */
    
    @Bean
    FanoutExchange alternateExchange() {
        FanoutExchange fanoutExchange = new FanoutExchange(rabbitCustomProperties.getAlternateExchange().getName());
        fanoutExchange.setInternal(true);
        return fanoutExchange;
    }
    
    @Bean
    FanoutExchange deadLettersExchange() {
        FanoutExchange fanoutExchange = new FanoutExchange(rabbitCustomProperties.getDeadlettersExchange().getName());
        fanoutExchange.setInternal(true);
        return fanoutExchange;
    }
    
    @Bean
    TopicExchange adminExchange() {
        TopicExchange adminExchange = new TopicExchange(rabbitCustomProperties.getAdminExchange().getName());
        return adminExchange;
    }
    
    @Bean
    DirectExchange directExchange() {
        DirectExchange directExchange = new DirectExchange(
                rabbitCustomProperties.getDirectExchange().getName(),
                rabbitCustomProperties.getDirectExchange().getDurable(),
                rabbitCustomProperties.getDirectExchange().getAutoDelete(),
                rabbitCustomProperties.getDirectExchange().getArguments());
        return directExchange;
    }
    
    /**
     * Queues
     * ========================
     */

    @Bean
    Queue alternateQueue() {
        return new Queue(rabbitCustomProperties.getAlternateExchange().getQueue(), true);
    }
    
    @Bean
    Queue deadLettersQueue() {
        return new Queue(rabbitCustomProperties.getDeadlettersExchange().getQueue(), true);
    }
    
    @Bean
    public List<Queue> adminQueues() {
        return rabbitCustomProperties.getAdminExchange().getQueues().entrySet().stream()
                .map(e -> e.getValue())
                .map(queueAdmin -> new Queue(queueAdmin.getName(), true))
                .collect(Collectors.toList());
    }

    /**
     * Bindings
     * ========================
     */
    
    @Bean
    Binding alternateBinding() {
        return BindingBuilder.bind(alternateQueue()).to(alternateExchange());
    }
    
    @Bean
    Binding deadLettersBinding() {
        return BindingBuilder.bind(deadLettersQueue()).to(deadLettersExchange());
    }
    
    @Bean
    List<Binding> adminBinding() {
        return rabbitCustomProperties.getAdminExchange().getQueues().entrySet().stream()
                .map(e -> e.getValue())
                .map(queueAdmin -> BindingBuilder
                .bind(new Queue(queueAdmin.getName(), true))
                .to(adminExchange())
                .with(queueAdmin.getRoutingKey())
                )
                .collect(Collectors.toList());
    }
    
   
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitCustomProperties.getHost());
        connectionFactory.setUsername(rabbitCustomProperties.getUsername());
        connectionFactory.setPassword(rabbitCustomProperties.getPassword());
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
    public MappingJackson2MessageConverter jackson2Converter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        return converter;
    }

    @Bean
    public DefaultMessageHandlerMethodFactory myHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(jackson2Converter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(myHandlerMethodFactory());
    }
}
