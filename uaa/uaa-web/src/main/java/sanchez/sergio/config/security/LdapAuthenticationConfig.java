package sanchez.sergio.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
import sanchez.sergio.config.properties.LdapCustomProperties;
import sanchez.sergio.service.LdapUserDetailsService;

/**
 *
 * @author sergio
 */
@Configuration
@Profile("prod")
public class LdapAuthenticationConfig {
    
    @Autowired
    private LdapCustomProperties ldapCustomProperties;
    
    @Autowired
    private LdapUserDetailsService userDetailsService;
    
    @Bean
    public LdapContextSource contextSource(){
        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(ldapCustomProperties.getUri());
        contextSource.setUserDn(ldapCustomProperties.getAdminDN());
        contextSource.setPassword(ldapCustomProperties.getAdminPassword());
        return contextSource;
    }
    
    /**
     * LdapUserSearch implementation which uses an Ldap filter to locate the user.
     * @return 
     */
    @Bean
    public LdapUserSearch ldapUserSearch(LdapContextSource contextSource) {
        return new FilterBasedLdapUserSearch(ldapCustomProperties.getSearchBase(), ldapCustomProperties.getSearchFilter(), contextSource );
    }
    
    /**
     * This interface is responsible for performing the user authentication and retrieving the 
     * user's information from the directory
     */
    @Bean
    public LdapAuthenticator ldapAuthenticator(LdapContextSource contextSource, LdapUserSearch ldapUserSearch){
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
        LdapAuthenticationProvider authenticationProvider =  new LdapAuthenticationProvider(ldapAuthenticator, ldapAuthoritiesPopulator);
        authenticationProvider.setUserDetailsContextMapper(userDetailsService);
        return authenticationProvider;
    }
}
