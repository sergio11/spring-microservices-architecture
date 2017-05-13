package sanchez.sergio.security;
import java.util.Set;
import sanchez.sergio.persistence.entities.User;
import sanchez.sergio.persistence.entities.Authority;

/**
 *
 * @author sergio
 */
public class UserDetailsImpl extends User implements CommonUserDetailsAware<Long> {

    public UserDetailsImpl(String username, String firstName, String lastName, Long identity, Set<Authority> grantedAuthorities) {
        super(username, firstName, lastName, identity, grantedAuthorities);
    }

    @Override
    public Long getUserId() {
        return this.identity;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
