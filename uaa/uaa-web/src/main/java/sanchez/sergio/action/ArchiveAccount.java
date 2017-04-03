package sanchez.sergio.action;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sanchez.sergio.domain.User;
import sanchez.sergio.domain.AccountStatus;
import static sanchez.sergio.domain.AccountStatus.ACCOUNT_ACTIVE;
import static sanchez.sergio.domain.AccountStatus.ACCOUNT_ARCHIVED;
import sanchez.sergio.domain.Action;
import sanchez.sergio.events.AccountEvent;
import sanchez.sergio.events.AccountEventType;
import sanchez.sergio.service.UserModule;
import sanchez.sergio.service.UserService;

/**
 *
 * @author sergio
 */
@Service
@Transactional
public class ArchiveAccount extends Action<User> {

    public User apply(User account) {
        
        Assert.isTrue(account.getStatus() != ACCOUNT_ARCHIVED, "The account is already archived");
        Assert.isTrue(account.getStatus() == ACCOUNT_ACTIVE, "An inactive account cannot be archived");

        UserService accountService = account.getModule(UserModule.class).getDefaultService();

        AccountStatus status = account.getStatus();

        // Archive the account
        account.setStatus(AccountStatus.ACCOUNT_ARCHIVED);
        account = accountService.update(account);

        try {
            // Trigger the account archived event
            account.sendAsyncEvent(new AccountEvent(AccountEventType.ACCOUNT_ARCHIVED, account));
        } catch (Exception ex) {
            // Rollback the operation
            account.setStatus(status);
            account = accountService.update(account);
        }

        return account;
    }
}
