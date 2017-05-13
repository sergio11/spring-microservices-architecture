package sanchez.sergio.security.config;

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
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    
    private static Logger logger = LoggerFactory.getLogger(ResourceServerConfig.class);
    
    @Autowired
    private TokenStore tokenStore;
   
   @Override
    public void configure(HttpSecurity http) throws Exception {
        
        logger.info("Configure ResourceServerConfig ....");
        
        http
                .antMatcher("/api/v1/accounts/**")
                .authorizeRequests()
                    .anyRequest().access("#oauth2.hasScope('accounts')")
                    .antMatchers(HttpMethod.GET, "/api/v1/accounts").hasAuthority("ROLE_ADMIN")
                    .antMatchers("/api/v1/accounts/**").hasAuthority("ROLE_ADMIN")
                .and()
                .csrf().disable();
    }
    
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("account").tokenStore(tokenStore);
    }
}
