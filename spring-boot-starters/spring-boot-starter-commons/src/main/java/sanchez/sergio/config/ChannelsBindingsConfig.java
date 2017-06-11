package sanchez.sergio.config;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Configuration;
import sanchez.sergio.notifications.bindings.UserNotificationBinding;
import sanchez.sergio.notifications.bindings.UsersManadgementBinding;

/**
 * @author sergio
 */
@Configuration
@EnableBinding({ UserNotificationBinding.class, UsersManadgementBinding.class, Source.class })
public class ChannelsBindingsConfig implements Serializable {
    
    private Logger logger = LoggerFactory.getLogger(ChannelsBindingsConfig.class);
    
    @PostConstruct
    protected void init() {
        logger.info("init Channels Bindings Config ...");
    }
}
