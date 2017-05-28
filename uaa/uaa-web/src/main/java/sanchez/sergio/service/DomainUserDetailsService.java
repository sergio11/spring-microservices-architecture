package sanchez.sergio.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import sanchez.sergio.persistence.entities.User;
import sanchez.sergio.exceptions.UserNotActivatedException;
import sanchez.sergio.persistence.repositories.UserRepository;
import sanchez.sergio.security.UserDetailsImpl;

/**
 * Authenticate a user from the database.
 */
@Component
@Profile("dev")
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
            Set<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toSet());
            
            return new UserDetailsImpl(user.getIdentity(), user.getUsername(),
                    user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), grantedAuthorities);

        }).orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " +
        "database"));
    }
}
