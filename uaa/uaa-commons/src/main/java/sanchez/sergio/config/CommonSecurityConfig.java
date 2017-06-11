package sanchez.sergio.config;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sergio
 */
@Configuration
public class CommonSecurityConfig implements Serializable {
    
    private Logger logger = LoggerFactory.getLogger(CommonSecurityConfig.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @PostConstruct
    protected void init() {
        logger.info("init Common Security Config ...");
    }
}
