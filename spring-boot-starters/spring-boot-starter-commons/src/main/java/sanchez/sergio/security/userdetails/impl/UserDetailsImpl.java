package sanchez.sergio.security.userdetails.impl;
import java.util.Collection;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import sanchez.sergio.security.userdetails.CommonUserDetailsAware;

/**
 *
 * @author sergio
 */
public class UserDetailsImpl<T> implements CommonUserDetailsAware<T> {
    
    private T id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private Set<GrantedAuthority> grantedAuthorities;
    
    public UserDetailsImpl(){}

    public UserDetailsImpl(T id, String username, String password, String fullName, String email, Set<GrantedAuthority> grantedAuthorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.grantedAuthorities = grantedAuthorities;
    }

    public void setId(T id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public void setGrantedAuthorities(Set<GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }
    
    @Override
    public T getUserId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
    

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
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
        return true;
    }
}
