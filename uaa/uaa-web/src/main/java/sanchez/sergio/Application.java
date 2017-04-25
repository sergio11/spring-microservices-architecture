package sanchez.sergio;

import java.util.Date;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.support.MessageBuilder;
import org.apache.log4j.Logger;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.core.MessageSource;
import sanchez.sergio.bindings.UserNotificationBinding;
import sanchez.sergio.domain.Notification;

@SpringBootApplication
@EnableJpaAuditing
@EnableDiscoveryClient
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
@EnableBinding({ UserNotificationBinding.class, Source.class })
public class Application {
    
    private final Logger logger = Logger.getLogger(Application.class);
    
    @Bean
    @InboundChannelAdapter(value = UserNotificationBinding.CHANNEL_NAME, poller = @Poller(fixedDelay = "10000", maxMessagesPerPoll = "1"))
    public MessageSource<Notification> timeMessageSource() {
        return () -> {
            logger.info("Notification at : " + new Date().getTime());
            return MessageBuilder.withPayload(new Notification("Notification at: " + new Date().getTime(), 1111l)).build();
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
