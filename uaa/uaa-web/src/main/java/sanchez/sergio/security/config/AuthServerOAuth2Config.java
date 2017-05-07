package sanchez.sergio.security.config;

import java.security.KeyPair;
import java.util.Arrays;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import sanchez.sergio.security.CustomTokenEnhancer;
import sanchez.sergio.service.DomainUserDetailsService;

/**
 *
 * @author sergio
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerOAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    /**
     * An AuthenticationManager instance is required to enable OAuth2 grant type 'password'
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private DomainUserDetailsService userDetailsService;
    
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }
    
    /**
     * Apply the token converter (and enhander) for token store.
     */
    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
    
    /**
     * Supplies an AccessTokenConverter implementation to be used by this endpoint
     * @return an access token converter configured with the authorization server's public/private keys
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(
             new ClassPathResource("keystore.jks"), "password".toCharArray())
             .getKeyPair("selfsigned");
        converter.setKeyPair(keyPair);
        return converter;
    }
    
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()").passwordEncoder(passwordEncoder());
    }
    
    /**
     * Sets up this authorization endpoint
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));

        endpoints.tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource)
                .passwordEncoder(passwordEncoder())
                .withClient("client")
                .authorizedGrantTypes("authorization_code", "client_credentials",
                        "refresh_token", "password", "implicit")
                .authorities("ROLE_CLIENT")
                .resourceIds("apis")
                .scopes("read")
                .secret("secret")
                .accessTokenValiditySeconds(300);
    }
}
