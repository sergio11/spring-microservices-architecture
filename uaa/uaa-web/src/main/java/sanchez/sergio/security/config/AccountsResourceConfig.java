package sanchez.sergio.security.config;

import javax.annotation.PostConstruct;
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
import sanchez.sergio.security.AuthoritiesConstants;

/**
 * @author sergio
 */
@Configuration
@EnableResourceServer
public class AccountsResourceConfig extends ResourceServerConfigurerAdapter {
    
    private static Logger logger = LoggerFactory.getLogger(AccountsResourceConfig.class);
    
    @Autowired
    private TokenStore tokenStore;
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers()
                    .antMatchers("/api/v1/accounts/**")
                .and()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.GET, "/api/v1/accounts").hasAuthority(AuthoritiesConstants.ADMIN)
                        .antMatchers("/api/v1/accounts/**").hasAuthority(AuthoritiesConstants.ADMIN)
                        .anyRequest().access("#oauth2.hasScope('accounts')");

    }
   
  
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("account").tokenStore(tokenStore);
    }
    
    @PostConstruct
    public void init(){
        logger.info("Init Accounts Resource Configuration  ...");
    }
}
