package sanchez.sergio.service;

import java.time.ZonedDateTime;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;
import sanchez.sergio.persistence.entities.User;
import sanchez.sergio.domain.Service;
import java.util.List;
import sanchez.sergio.persistence.entities.AccountStatus;
import sanchez.sergio.persistence.entities.AccountEvent;
import sanchez.sergio.persistence.entities.AccountEventType;
import sanchez.sergio.exceptions.EmailAlredyExistsException;
import sanchez.sergio.exceptions.UsernameAlredyExistsException;
import sanchez.sergio.persistence.repositories.UserRepository;

@org.springframework.stereotype.Service
public class UserService extends Service<User, Long> {

    private final Logger log = Logger.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository accountRepository) {
        this.userRepository = accountRepository;
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Register a new {@link User} and handle a synchronous event flow for account creation
     *
     * @param account is the {@link User} to create
     * @return the created account
     * @throws IllegalStateException if the event flow fails
     */
    public User registerAccount(final User account) throws IllegalStateException {
        // Save the account to the repository
        return (User) userRepository.findByUsername(account.getUsername().toLowerCase())
                .map(user -> { throw new UsernameAlredyExistsException(user); })
                .orElseGet(() -> userRepository.findAccountByEmail(account.getEmail())
                        .map(user -> { throw new EmailAlredyExistsException(user); })
                        .orElseGet(() -> {
                            User accountSaved = create(account);
                            try {
                                // Handle a synchronous event flow
                                //account.sendAsyncEvent(new AccountEvent(AccountEventType.ACCOUNT_CREATED, accountSaved));
                            } catch (Exception ex) {
                                log.error("Account registration failed", ex);
                                // Rollback the account creation
                                delete(accountSaved.getIdentity());
                                throw ex;
                            }

                            return accountSaved;
                        }));
    }
    
    /**
     * Create a new {@link User} entity.
     *
     * @param user is the {@link User} to create
     * @return the newly created {@link User}
     */
    public User create(User user) {
        // Save the account to the repository
        user = userRepository.saveAndFlush(user);

        return user;
    }
    /**
     * Get an {@link User} entity for the supplied identifier.
     *
     * @param id is the unique identifier of a {@link User} entity
     * @return an {@link User} entity
     */
    public User get(Long id) {
        return userRepository.findOne(id);
    }

    /**
     * Update an {@link User} entity with the supplied identifier.
     *
     * @param account is the {@link User} containing updated fields
     * @return the updated {@link User} entity
     */
    @Override
    public User update(User account) {
        Assert.notNull(account.getIdentity(), "Account id must be present in the resource URL");
        Assert.notNull(account, "Account request body cannot be null");

        Assert.state(userRepository.exists(account.getIdentity()),
                "The account with the supplied id does not exist");

        User currentAccount = get(account.getIdentity());
        currentAccount.setEmail(account.getEmail());
        currentAccount.setFirstName(account.getFirstName());
        currentAccount.setLastName(account.getLastName());
        currentAccount.setStatus(account.getStatus());

        return userRepository.save(currentAccount);
    }

    /**
     * Delete the {@link User} with the supplied identifier.
     *
     * @param id is the unique identifier for the {@link User}
     */
    public boolean delete(Long id) {
        Assert.state(userRepository.exists(id),
                "The account with the supplied id does not exist");
        this.userRepository.delete(id);
        return true;
    }
    
    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p> This is scheduled to get fired everyday, at 01:00 (am).</p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        ZonedDateTime now = ZonedDateTime.now();
        List<User> accounts = userRepository.findAllByStatusAndCreatedAtBefore(AccountStatus.ACCOUNT_PENDING, now.minusDays(3));
        for (User account : accounts) {
            log.debug("Deleting not activated account : " + account.getFirstName());
            userRepository.delete(account);
        }
    }
}
