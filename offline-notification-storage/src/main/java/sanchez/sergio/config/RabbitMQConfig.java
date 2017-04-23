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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sanchez.sergio.config.properties.RabbitMQProperties;

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
        return new Queue(rabbitMQProperties.getAlternateExchange().getQueue(), false);
    }
    
    @Bean
    @Qualifier("deadLettersQueue")
    Queue deadLettersQueue() {
        return new Queue(rabbitMQProperties.getDeadlettersExchange().getQueue(), false);
    }
    
    @Bean
    @Qualifier("alternateExchange")
    FanoutExchange alternateExchange() {
        return new FanoutExchange(rabbitMQProperties.getAlternateExchange().getName());
    }
    
    @Bean
    @Qualifier("deadLettersExchange")
    FanoutExchange deadLettersExchange() {
        return new FanoutExchange(rabbitMQProperties.getDeadlettersExchange().getName());
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
    
}
