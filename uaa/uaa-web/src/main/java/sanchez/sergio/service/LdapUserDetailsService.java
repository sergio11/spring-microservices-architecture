package sanchez.sergio.service;

import java.util.Collection;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Component;

/**
 * Implementation of UserDetailsContextMapper strategy, which is responsible for mapping user objects to and from LDAP context data: 
 * @author sergio
 */
@Component
@Profile("prod")
public class LdapUserDetailsService implements UserDetailsContextMapper {

    @Override
    public UserDetails mapUserFromContext(DirContextOperations dco, String username, Collection<? extends GrantedAuthority> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mapUserToContext(UserDetails ud, DirContextAdapter dca) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
