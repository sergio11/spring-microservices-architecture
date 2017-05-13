package sanchez.sergio.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import sanchez.sergio.persistence.entities.User;
import sanchez.sergio.exceptions.UserNotActivatedException;
import sanchez.sergio.persistence.repositories.UserRepository;
import sanchez.sergio.security.UserDetailsImpl;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        log.debug("Authenticating {}", username);
        String lowercaseLogin = username.toLowerCase(Locale.ENGLISH);
        Optional<User> userFromDatabase = userRepository.findOneWithAuthoritiesByUsername(lowercaseLogin);
        return userFromDatabase.map(user -> {
            if (!user.getEnabled()) {
                throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
            }
            return new UserDetailsImpl(lowercaseLogin, user.getFirstName(), user.getLastName(), user.getIdentity(),
                user.getAuthorities());
        }).orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " +
        "database"));
    }
}
