package sanchez.sergio.persistence.entities;

import sanchez.sergio.persistence.entities.AbstractEntity;
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
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import sanchez.sergio.action.ActivateAccount;
import sanchez.sergio.action.ArchiveAccount;
import sanchez.sergio.action.ConfirmAccount;
import sanchez.sergio.action.SuspendAccount;
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
import sanchez.sergio.controller.UserController;

/**
 * @author sergio
 * @param <T>
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
    
    @NotBlank(message = "{user.username.notnull}", groups = {UserCreation.class, UserUpdate.class})
    @Size(min = 5, max = 15, message = "{user.username.size}", groups = {UserCreation.class, UserUpdate.class})
    @UsernameUnique(message = "{user.username.unique}", groups = {UserCreation.class, UserUpdate.class})
    @Column(name="display_name", nullable = false, length = 30, unique = true)
    private String displayName;

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
    
    @Column(name="full_name", unique = false, nullable = false, length = 60)
    private String fullName;
    
    @Column(unique = true, nullable = false, length = 90)
    private String email;
    
    @Column(name="title", unique = false, nullable = false, length = 30)
    private String title;
    
    @Column(name="initials", unique = false, nullable = false, length = 2)
    private String initials;


    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "USER_AUTHORITIES",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "identity"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "ID")
    )
    private Set<Authority> authorities = new HashSet();
    
    
    @Enumerated(value = EnumType.STRING)
    @Column(unique = false, nullable = false)
    private AccountStatus status;

    public User() {
        status = AccountStatus.ACCOUNT_CREATED;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
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
}
