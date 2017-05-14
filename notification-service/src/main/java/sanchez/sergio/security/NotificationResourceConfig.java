package sanchez.sergio.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author sergio
 */
@Configuration
@EnableResourceServer
public class NotificationResourceConfig extends ResourceServerConfigurerAdapter {
    
    private static Logger logger = LoggerFactory.getLogger(NotificationResourceConfig.class);
    
    @Autowired
    private TokenStore tokenStore;
  
    @Override
    public void configure(HttpSecurity http) throws Exception {
        
        http
                .requestMatchers()
                    .antMatchers("/api/v1/notifications/**")
                .and()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.GET, "/api/v1/notifications").hasAuthority(AuthoritiesConstants.ADMIN)
                        .anyRequest().access("#oauth2.hasScope('notifications')");
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("notification").tokenStore(tokenStore);
    }
}
