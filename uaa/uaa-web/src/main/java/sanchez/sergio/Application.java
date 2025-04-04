package sanchez.sergio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.apache.log4j.Logger;

@SpringBootApplication
@EnableDiscoveryClient
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class Application {
    
    private final Logger logger = Logger.getLogger(Application.class);
    
    /*@Bean
    @InboundChannelAdapter(value = UserNotificationBinding.CHANNEL_NAME, poller = @Poller(fixedDelay = "10000", maxMessagesPerPoll = "1"))
    public MessageSource<Notification> timeMessageSource() {
        return () -> {
            logger.info("Notification at : " + new Date().getTime());
            return MessageBuilder.withPayload(new Notification(1L, "Notification at: " + new Date().getTime(), 1111l, new Date())).build();
        };
    }*/

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
