package sanchez.sergio.action;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sanchez.sergio.persistence.entities.User;
import sanchez.sergio.persistence.entities.AccountStatus;
import static sanchez.sergio.persistence.entities.AccountStatus.ACCOUNT_CONFIRMED;
import static sanchez.sergio.persistence.entities.AccountStatus.ACCOUNT_PENDING;
import sanchez.sergio.domain.Action;
import sanchez.sergio.persistence.entities.AccountEvent;
import sanchez.sergio.persistence.entities.AccountEventType;
import sanchez.sergio.service.UserModule;
import sanchez.sergio.service.UserService;

/**
 * @author sergio
 */
@Service
@Transactional
public class ConfirmAccount extends Action<User> {

    public User apply(User account) {
        
        Assert.isTrue(account.getStatus() != ACCOUNT_CONFIRMED, "The account has already been confirmed");
        Assert.isTrue(account.getStatus() == ACCOUNT_PENDING, "The account has already been confirmed");

        UserService accountService = account.getModule(UserModule.class).getDefaultService();

        AccountStatus status = account.getStatus();

        // Activate the account
        account.setStatus(AccountStatus.ACCOUNT_CONFIRMED);
        account = accountService.update(account);

        try {
            // Trigger the account confirmed event
            account.sendAsyncEvent(new AccountEvent(AccountEventType.ACCOUNT_CONFIRMED, account));
        } catch (Exception ex) {
            // Rollback the operation
            account.setStatus(status);
            account = accountService.update(account);
        }

        return account;
    }
}
