package sanchez.sergio.config.security;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import sanchez.sergio.security.AuthoritiesConstants;

/**
 * @author sergio
 */
@Configuration
@EnableWebSecurity
@EnableResourceServer
public class NotificationResourceConfig extends ResourceServerConfigurerAdapter {
    
    private static Logger logger = LoggerFactory.getLogger(NotificationResourceConfig.class);
    
    @Autowired
    private TokenStore tokenStore;
  
    @Override
    public void configure(HttpSecurity http) throws Exception {
       
        http
                .requestMatchers()
                    .antMatchers("/v1/api/notifications/**")
                .and()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.GET, "/v1/api/notifications").hasAuthority(AuthoritiesConstants.ADMIN)
                        .anyRequest().access("#oauth2.hasScope('notifications')");
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("notification").tokenStore(tokenStore);
    }
    
    @PostConstruct
    protected void init() {
        logger.info("NotificationResourceConfig init ...");
    }
}
