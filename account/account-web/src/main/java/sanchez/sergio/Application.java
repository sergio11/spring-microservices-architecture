package sanchez.sergio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@EnableJpaAuditing
@EnableDiscoveryClient
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
