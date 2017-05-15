package sanchez.sergio.config.security;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import sanchez.sergio.security.CustomAuthenticationEntryPoint;
import sanchez.sergio.security.CustomLogoutSuccessHandler;

/**
 *
 * @author sergio
 */
@Configuration
@EnableResourceServer
public class CommonResourceServerConfig  extends ResourceServerConfigurerAdapter {
    
    private Logger logger = LoggerFactory.getLogger(CommonResourceServerConfig.class);
    
    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    
    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;
    
    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                    .logout()
                        .logoutUrl("/oauth/logout")
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                .and()
                    .csrf()
                        .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
                        .disable()
                    .headers()
                        .frameOptions().disable()
                .and()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }
    
    @PostConstruct
    protected void init() {
        logger.info("init CommonResourceServerConfig ...");
    }
    
}
