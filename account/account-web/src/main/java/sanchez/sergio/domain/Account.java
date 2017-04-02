package sanchez.sergio.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import sanchez.sergio.action.ActivateAccount;
import sanchez.sergio.action.ArchiveAccount;
import sanchez.sergio.action.ConfirmAccount;
import sanchez.sergio.action.SuspendAccount;
import sanchez.sergio.controller.AccountController;
import sanchez.sergio.events.AccountEvent;
import sanchez.sergio.service.AccountModule;

/**
 * @author sergio
 */
@Entity
@Table(name = "ACCOUNTS")
public class Account extends AbstractEntity<AccountEvent, Long> {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = false, nullable = false, length = 30)
    private String firstName;
    @Column(unique = false, nullable = false, length = 30)
    private String lastName;
    @Column(unique = true, nullable = false, length = 90)
    private String email;
    @Enumerated(value = EnumType.STRING)
    @Column(unique = false, nullable = false)
    private AccountStatus status;

    public Account() {
        status = AccountStatus.ACCOUNT_CREATED;
    }

    public Account(String firstName, String lastName, String email) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @JsonProperty("accountId")
    @Override
    public Long getIdentity() {
        return this.id;
    }

    @Override
    public void setIdentity(Long id) {
        this.id = id;
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

    @Command(method = "activate", controller = AccountController.class)
    public Account activate() {
        return getAction(ActivateAccount.class).apply(this);
    }

    @Command(method = "archive", controller = AccountController.class)
    public Account archive() {
        return getAction(ArchiveAccount.class).apply(this);
    }

    @Command(method = "confirm", controller = AccountController.class)
    public Account confirm() {
        return getAction(ConfirmAccount.class).apply(this);
    }

    @Command(method = "suspend", controller = AccountController.class)
    public Account suspend() {
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
        AccountModule accountProvider = getModule(AccountModule.class);
        return (T) accountProvider;
    }

    /**
     * Returns the {@link Link} with a rel of {@link Link#REL_SELF}.
     */
    @Override
    public Link getId() {
        return linkTo(AccountController.class)
                .slash("accounts")
                .slash(getIdentity())
                .withSelfRel();
    }

    @Override
    public String toString() {
        return "Account{"
                + "id=" + id
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", email='" + email + '\''
                + ", status=" + status
                + "} " + super.toString();
    }

}
