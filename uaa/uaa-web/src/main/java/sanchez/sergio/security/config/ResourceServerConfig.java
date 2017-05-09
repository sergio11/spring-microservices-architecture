package sanchez.sergio.security.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author sergio
 */
@Configuration
@EnableWebSecurity
@EnableResourceServer
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    
    @Autowired
    private UserDetailsService userService;
    
    @Autowired
    private TokenStore tokenStore;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }
    
    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userService).passwordEncoder(passwordEncoder());
    }
   
   @Override
    public void configure(HttpSecurity http) throws Exception {
        
        http
                .authorizeRequests()
                    .anyRequest().authenticated()
                    .antMatchers(HttpMethod.GET, "/uaa/api/v1/accounts").hasAuthority("ROLE_ADMIN")
                    .antMatchers("/uaa/api/v1/accounts/{id}/**").hasAuthority("ROLE_ADMIN")
                    .antMatchers("/uaa/oauth/token").permitAll()
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable();
    }
    
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("account").tokenStore(tokenStore);
    }
}
