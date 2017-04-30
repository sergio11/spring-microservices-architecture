package sanchez.sergio.config.properties;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author sergio
 */
@Component
@ConfigurationProperties(prefix="rabbitmq")
public class RabbitCustomProperties {
    
    private String host;
    private String username;
    private String password;
    private final AlternateExchange alternateExchange = new AlternateExchange();
    private final DeadlettersExchange deadlettersExchange = new DeadlettersExchange();
    private final AdminExchange adminExchange = new AdminExchange();

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

    public AdminExchange getAdminExchange() {
        return adminExchange;
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
    
    public static class AdminExchange {
        
        private String name;
        private Map<String, AdminQueue> queues = new HashMap<String, AdminQueue>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, AdminQueue> getQueues() {
            return queues;
        }

        public void setQueues(Map<String, AdminQueue> queues) {
            this.queues = queues;
        }
        
        public static class AdminQueue {
            
            private String name;
            private String routingKey;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRoutingKey() {
                return routingKey;
            }

            public void setRoutingKey(String routingKey) {
                this.routingKey = routingKey;
            }
        }
    
    
    }
    
}
