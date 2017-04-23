package sanchez.sergio.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author sergio
 */
@Component
@ConfigurationProperties(prefix="rabbitmq")
public class RabbitMQProperties {
    
    private String host;
    private String username;
    private String password;
    private final AlternateExchange alternateExchange = new AlternateExchange();
    private final DeadlettersExchange deadlettersExchange = new DeadlettersExchange();

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AlternateExchange getAlternateExchange() {
        return alternateExchange;
    }

    public DeadlettersExchange getDeadlettersExchange() {
        return deadlettersExchange;
    }
    
    public static class AlternateExchange {
    
        private String name;
        private String queue;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getQueue() {
            return queue;
        }

        public void setQueue(String queue) {
            this.queue = queue;
        }
    
    }
    
    public static class DeadlettersExchange {

        private String name;
        private String queue;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getQueue() {
            return queue;
        }

        public void setQueue(String queue) {
            this.queue = queue;
        }
    }
    
}
