package sanchez.sergio.domain;

import sanchez.sergio.persistence.constraints.FieldMatch;
import sanchez.sergio.persistence.constraints.FieldNotMatch;
import sanchez.sergio.persistence.constraints.UserCurrentPassword;
import sanchez.sergio.persistence.constraints.UsernameUnique;
import sanchez.sergio.domain.User.UserChangePassword;
import sanchez.sergio.domain.User.UserCreation;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author sergio
 */
@Entity
@Table(name = "USERS")
@FieldMatch(first = "passwordClear", second = "confirmPassword", message = "{user.pass.not.match}", groups = {UserCreation.class, UserChangePassword.class})
@FieldNotMatch(first = "currentClearPassword", second = "passwordClear", message = "{user.current.pass.not.match}", groups = {UserChangePassword.class})
public class User implements Serializable, UserDetails  {

    /* Marker interface for grouping validations to be applied at the time of creating a (new) user. */
    public interface UserCreation {
    }

    /* Marker interface for grouping validations to be applied at the time of updating a (existing) user. */
    public interface UserUpdate {
    }

    /* Marker interface for grouping validations to be applied at the time of change user password. */
    public interface UserChangePassword {
    }

    /* Marker interface for grouping validations to be applied at the time of updating a user status by administrator. */
    public interface UserStatusUpdate {
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{user.username.notnull}", groups = {UserCreation.class, UserUpdate.class})
    @Size(min = 5, max = 15, message = "{user.username.size}", groups = {UserCreation.class, UserUpdate.class})
    @UsernameUnique(message = "{user.username.unique}", groups = {UserCreation.class, UserUpdate.class})
    @Column(nullable = false, length = 30, unique = true)
    private String username;

    @NotBlank(message = "{user.current.pass.notnull}", groups = {UserChangePassword.class})
    @UserCurrentPassword(message = "{user.current.pass.not.match}", groups = {UserChangePassword.class})
    @Transient
    private String currentClearPassword;

    @NotBlank(message = "{user.pass.notnull}", groups = {UserCreation.class, UserChangePassword.class})
    @Size(min = 8, max = 25, message = "{user.pass.size}", groups = {UserCreation.class, UserChangePassword.class})
    @Transient
    private String passwordClear;

    @NotBlank(message = "{user.confirm.pass.notnull}", groups = {UserCreation.class, UserChangePassword.class})
    @Transient
    private String confirmPassword;

    @Column(length = 60)
    private String password;

    @Column(nullable = true)
    private Date lastLoginAccess;

    @NotNull(message = "{user.enabled.notnull}", groups = {UserStatusUpdate.class})
    private Boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "USER_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
    )
    private Set<Authority> authorities = new HashSet();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrentClearPassword() {
        return currentClearPassword;
    }

    public void setCurrentClearPassword(String currentClearPassword) {
        this.currentClearPassword = currentClearPassword;
    }

    public String getPasswordClear() {
        return passwordClear;
    }

    public void setPasswordClear(String passwordClear) {
        this.passwordClear = passwordClear;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastLoginAccess() {
        return lastLoginAccess;
    }

    public void setLastLoginAccess(Date lastLoginAccess) {
        this.lastLoginAccess = lastLoginAccess;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
    
    public void addAuthority(Authority authority){
        if(!this.authorities.contains(authority)){
            this.authorities.add(authority);
            authority.addUser(this);
        }
    }
   
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", currentClearPassword=" + currentClearPassword + ", passwordClear=" + passwordClear + ", confirmPassword=" + confirmPassword + ", password=" + password + ", lastLoginAccess=" + lastLoginAccess + ", enabled=" + enabled + ", authorities=" + authorities + '}';
    }
}
