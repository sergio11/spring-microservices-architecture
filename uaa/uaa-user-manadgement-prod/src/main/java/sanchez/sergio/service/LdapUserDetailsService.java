package sanchez.sergio.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.naming.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Component;
import sanchez.sergio.security.userdetails.impl.UserDetailsImpl;

/**
 * Implementation of UserDetailsContextMapper strategy, which is responsible for mapping user objects to and from LDAP context data: 
 * @author sergio
 */
@Component
public class LdapUserDetailsService implements UserDetailsContextMapper {
    
    private final Logger logger = LoggerFactory.getLogger(LdapUserDetailsService.class);

    @Override
    public UserDetails mapUserFromContext(DirContextOperations dco, String username, Collection<? extends GrantedAuthority> clctn) {
        
        logger.info("mapUserFromContext: " + username);
        
        Set<GrantedAuthority> grantedAuthorities = clctn.stream().map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toSet());
        
        return new UserDetailsImpl<Name>(
                dco.getDn(),
                dco.getStringAttribute("uid"),
                dco.getStringAttribute("userPassword"),
                dco.getStringAttribute("cn"),
                dco.getStringAttribute("sn"),
                dco.getStringAttribute("mail"),
                grantedAuthorities
        );
    }

    @Override
    public void mapUserToContext(UserDetails ud, DirContextAdapter dca) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
