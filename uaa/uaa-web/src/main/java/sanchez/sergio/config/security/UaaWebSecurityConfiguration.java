package sanchez.sergio.config.security;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import sanchez.sergio.security.filter.OAuth2ServerAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class UaaWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    private static Logger logger = LoggerFactory.getLogger(UaaWebSecurityConfiguration.class);
    
    @Autowired
    private AuthenticationProvider authenticationProvider;
    
    @Autowired
    private OAuth2ServerAuthenticationFilter authenticationFilter;

    @Override
    @Bean
    public AuthenticationManager  authenticationManagerBean() throws Exception {
        List<AuthenticationProvider> list = new ArrayList<AuthenticationProvider>();
        list.add(authenticationProvider);
        return new ProviderManager(list);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http
                .addFilterBefore(authenticationFilter, BasicAuthenticationFilter.class)
                .exceptionHandling()
                    .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                    .csrf()
                        .disable()
                    .headers()
                        .frameOptions()
                            .disable()
                .and()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
    }
    
    @PostConstruct
    public void init(){
        logger.info("Init UAA Web Security Configuration ...");
    }
}
