package sanchez.sergio.action;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sanchez.sergio.domain.User;
import java.util.Arrays;
import org.springframework.util.Assert;
import sanchez.sergio.domain.AccountStatus;
import static sanchez.sergio.domain.AccountStatus.*;
import sanchez.sergio.domain.Action;
import sanchez.sergio.events.AccountEvent;
import sanchez.sergio.events.AccountEventType;
import sanchez.sergio.service.UserModule;
import sanchez.sergio.service.UserService;

/**
 * @author sergio
 */
@Service
@Transactional
public class ActivateAccount extends Action<User> {

    public User apply(User account) {
        
        Assert.isTrue(account.getStatus() != ACCOUNT_ACTIVE, "The account is already active");
        Assert.isTrue(Arrays.asList(ACCOUNT_CONFIRMED, ACCOUNT_SUSPENDED, ACCOUNT_ARCHIVED)
                .contains(account.getStatus()), "The account cannot be activated");

        UserService accountService = account.getModule(UserModule.class).getDefaultService();
        // Get prev account status.
        AccountStatus status = account.getStatus();
        // Activate the account
        account.setStatus(AccountStatus.ACCOUNT_ACTIVE);
        account = accountService.update(account);
        try {
            // Trigger the account activated event
            account.sendAsyncEvent(new AccountEvent(AccountEventType.ACCOUNT_ACTIVATED, account));
        } catch (Exception ex) {
            // Rollback the operation
            account.setStatus(status);
            account = accountService.update(account);
        }
        return account;
    }
}
