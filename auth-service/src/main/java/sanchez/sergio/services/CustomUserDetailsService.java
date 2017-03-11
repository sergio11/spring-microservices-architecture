package sanchez.sergio.services;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sanchez.sergio.domain.User;
import sanchez.sergio.persistence.repositories.UserRepository;

/**
 *
 * @author sergio
 */
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService, Serializable {
    
    private final UserRepository userRepository;
    
    private static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        }
        logger.info("Usuario obtenido: " + user.toString());
        return user;
    }
}
