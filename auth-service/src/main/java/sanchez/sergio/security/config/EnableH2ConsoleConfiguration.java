package sanchez.sergio.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author sergio
 */
@Configuration
@Profile("default")
@Order(1)
public class EnableH2ConsoleConfiguration extends WebSecurityConfigurerAdapter  {
    
    private static Logger logger = LoggerFactory.getLogger(EnableH2ConsoleConfiguration.class);
  
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        logger.info("Apply Security H2 configuration ");
        httpSecurity.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }
}
