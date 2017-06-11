package sanchez.sergio.config.security;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.DirContextAuthenticationStrategy;
import org.springframework.ldap.core.support.ExternalTlsDirContextAuthenticationStrategy;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.util.Assert;
import sanchez.sergio.config.properties.LdapCustomProperties;
import sanchez.sergio.service.LdapUserDetailsService;

/**
 * Config Authentication Provider for Prod environment
 * @author sergio
 */
@Configuration
public class LdapAuthenticationConfig {
    
    private final Logger logger = LoggerFactory.getLogger(LdapAuthenticationConfig.class);
    
    static {
        System.setProperty("javax.net.debug", "ssl,keymanager");
        System.setProperty("javax.net.ssl.keyStore", "classpath:keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "bisite00");
        System.setProperty("javax.net.ssl.keyStoreType", "JKS");
        System.setProperty("javax.net.ssl.trustStore", "classpath:truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "bisite00");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
    }
    
    @Autowired
    private LdapCustomProperties ldapCustomProperties;
    
    @Autowired
    private LdapUserDetailsService userDetailsService;
    
    @Bean
    public DirContextAuthenticationStrategy authenticationStrategy(){
        ExternalTlsDirContextAuthenticationStrategy authStra = new ExternalTlsDirContextAuthenticationStrategy();
        authStra.setShutdownTlsGracefully(true);
        return authStra;
    }
    
    @Bean
    public LdapContextSource contextSource(DirContextAuthenticationStrategy authenticationStrategy){
        Assert.state(authenticationStrategy != null, "DirContextAuthenticationStrategy must be provided");
        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(ldapCustomProperties.getUri());
        contextSource.setUserDn(ldapCustomProperties.getAdminDn());
        logger.debug("Admin DN for DefaultSpringSecurityContextSource " + ldapCustomProperties.getAdminDn());
        contextSource.setPassword(ldapCustomProperties.getAdminPassword());
        logger.debug("Admin Password DN for DefaultSpringSecurityContextSource " + ldapCustomProperties.getAdminPassword());
        //contextSource.setAuthenticationStrategy(authenticationStrategy);
        contextSource.afterPropertiesSet();
        return contextSource;
    }
    
    /**
     * LdapUserSearch implementation which uses an Ldap filter to locate the user.
     * @return 
     */
    @Bean
    public LdapUserSearch ldapUserSearch(LdapContextSource contextSource) {
         Assert.state(contextSource != null, "LdapContextSource must be provided");
        return new FilterBasedLdapUserSearch(ldapCustomProperties.getSearchBase(), ldapCustomProperties.getSearchFilter(), contextSource );
    }
    
    /**
     * This interface is responsible for performing the user authentication and retrieving the 
     * user's information from the directory
     */
    @Bean
    public LdapAuthenticator ldapAuthenticator(LdapContextSource contextSource, LdapUserSearch ldapUserSearch){
        Assert.state(contextSource != null, "LdapContextSource must be provided");
        Assert.state(ldapUserSearch != null, "LdapUserSearch must be provided");
        BindAuthenticator ldapAuthenticator = new BindAuthenticator(contextSource);
        ldapAuthenticator.setUserSearch(ldapUserSearch);
        return ldapAuthenticator;
    };
    
    /**
     * Once the user has been authenticated, 
     * this interface is called to obtain the set of granted authorities for the user
     * @param contextSource
     * @return 
     */
    @Bean
    public LdapAuthoritiesPopulator LdapAuthoritiesPopulator(LdapContextSource contextSource){
        Assert.state(contextSource != null, "LdapContextSource must be provided");
        DefaultLdapAuthoritiesPopulator  ldapAuthoritiesPopulator = new DefaultLdapAuthoritiesPopulator(contextSource, ldapCustomProperties.getGroupSearchBase());
        ldapAuthoritiesPopulator.setGroupSearchFilter(ldapCustomProperties.getGroupSearchFilter());
        ldapAuthoritiesPopulator.setGroupRoleAttribute(ldapCustomProperties.getGroupRoleAttribute());
        ldapAuthoritiesPopulator.setSearchSubtree(true);
        ldapAuthoritiesPopulator.setConvertToUpperCase(true);
        return ldapAuthoritiesPopulator;
    }
    
    /**
     * An AuthenticationProvider implementation that authenticates against an LDAP server. 
     */
    @Bean
    public AuthenticationProvider authenticationProvider(LdapAuthenticator ldapAuthenticator, LdapAuthoritiesPopulator ldapAuthoritiesPopulator){
        Assert.state(ldapAuthenticator != null, "LdapAuthenticator must be provided");
        Assert.state(ldapAuthoritiesPopulator != null, "LdapAuthoritiesPopulator must be provided");
        LdapAuthenticationProvider authenticationProvider =  new LdapAuthenticationProvider(ldapAuthenticator, ldapAuthoritiesPopulator);
        authenticationProvider.setUserDetailsContextMapper(userDetailsService);
        return authenticationProvider;
    }
    
    @PostConstruct
    public void init(){
        logger.info("Init Ldap Authentication Configuration ...");
        Assert.state(ldapCustomProperties != null, "LDAP Custom Properties must be provided");
        Assert.state(userDetailsService != null, "A LDAP User Details Service must be provided");
    }
}
