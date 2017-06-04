package sanchez.sergio.persistence.entities;


import java.io.Serializable;
import javax.naming.Name;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;

/**
 * @author sergio
 */
@Entry(objectClasses = { "account", "inetOrgPerson", "posixAccount", "shadowAccount" }, base="ou=users")
public class User implements Serializable  {

    @Id
    private Name id;
    
    @Attribute(name="uid")
    private String username;

    @Attribute(name="displayName")
    private String displayName;

    @Transient
    private String currentClearPassword;

    @Transient
    private String passwordClear;

    @Transient
    private String confirmPassword;

    @Attribute(name="userPassword")
    private String password;
    
    @Attribute(name="gecos")
    private String fullName;

    @Attribute(name="mail")
    private String email;
    
    @Attribute(name="title")
    private String title;

    @Attribute(name="initials")
    private String initials;

    public Name getId() {
        return id;
    }

    public void setId(Name id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
