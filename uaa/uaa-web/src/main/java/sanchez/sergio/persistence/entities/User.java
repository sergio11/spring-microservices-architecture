package sanchez.sergio.persistence.entities;

import sanchez.sergio.persistence.entities.AbstractEntity;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import sanchez.sergio.action.ActivateAccount;
import sanchez.sergio.action.ArchiveAccount;
import sanchez.sergio.action.ConfirmAccount;
import sanchez.sergio.action.SuspendAccount;
import sanchez.sergio.controller.UserController;
import sanchez.sergio.domain.Aggregate;
import sanchez.sergio.domain.Command;
import sanchez.sergio.domain.Module;
import sanchez.sergio.persistence.entities.User.UserChangePassword;
import sanchez.sergio.persistence.entities.User.UserCreation;
import sanchez.sergio.persistence.constraints.FieldMatch;
import sanchez.sergio.persistence.constraints.FieldNotMatch;
import sanchez.sergio.persistence.constraints.UserCurrentPassword;
import sanchez.sergio.persistence.constraints.UsernameUnique;
import sanchez.sergio.service.UserModule;

/**
 * @author sergio
 */
@Entity
@Table(name = "USERS")
@FieldMatch(first = "passwordClear", second = "confirmPassword", message = "{user.pass.not.match}", groups = {UserCreation.class, UserChangePassword.class})
@FieldNotMatch(first = "currentClearPassword", second = "passwordClear", message = "{user.current.pass.not.match}", groups = {UserChangePassword.class})
public class User extends AbstractEntity<AccountEvent, Long>  {


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

    @Column(name="last_login_access", nullable = true)
    private Date lastLoginAccess;

    @NotNull(message = "{user.enabled.notnull}", groups = {UserStatusUpdate.class})
    protected Boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "USER_AUTHORITIES",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "identity"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "ID")
    )
    private Set<Authority> authorities = new HashSet();
    
    @Column(name="first_name", unique = false, nullable = false, length = 30)
    private String firstName;
    @Column(name="last_name", unique = false, nullable = false, length = 30)
    private String lastName;
    @Column(unique = true, nullable = false, length = 90)
    private String email;
    @Enumerated(value = EnumType.STRING)
    @Column(unique = false, nullable = false)
    private AccountStatus status;

    public User() {
        status = AccountStatus.ACCOUNT_CREATED;
    }

    public User(String firstName, String lastName, String email) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(String username, String password, String firstName, String lastName, Long identity, Set<Authority> grantedAuthorities) {
        super(identity);
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.authorities = grantedAuthorities;
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

    public Set<Authority> getAuthorities() {
        return authorities;
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
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    @Command(method = "activate", controller = UserController.class)
    public User activate() {
        return getAction(ActivateAccount.class).apply(this);
    }

    @Command(method = "archive", controller = UserController.class)
    public User archive() {
        return getAction(ArchiveAccount.class).apply(this);
    }

    @Command(method = "confirm", controller = UserController.class)
    public User confirm() {
        return getAction(ConfirmAccount.class).apply(this);
    }

    @Command(method = "suspend", controller = UserController.class)
    public User suspend() {
        return getAction(SuspendAccount.class).apply(this);
    }

    /**
     * Retrieves an instance of the {@link Module} for this instance
     *
     * @return the provider for this instance
     * @throws IllegalArgumentException if the application context is
     * unavailable or the provider does not exist
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Module<A>, A extends Aggregate<AccountEvent, Long>> T getModule() throws IllegalArgumentException {
        UserModule accountProvider = getModule(UserModule.class);
        return (T) accountProvider;
    }

    /**
     * Returns the {@link Link} with a rel of {@link Link#REL_SELF}.
     */
    @Override
    public Link getId() {
        return linkTo(UserController.class)
                .slash("accounts")
                .slash(getIdentity())
                .withSelfRel();
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", status=" + status + '}';
    }
}
