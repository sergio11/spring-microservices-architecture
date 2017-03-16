package sanchez.sergio.action;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sanchez.sergio.domain.Account;
import java.util.Arrays;
import org.springframework.util.Assert;
import sanchez.sergio.domain.AccountStatus;
import static sanchez.sergio.domain.AccountStatus.*;
import sanchez.sergio.domain.Action;
import sanchez.sergio.events.AccountEvent;
import sanchez.sergio.events.AccountEventType;
import sanchez.sergio.service.AccountModule;
import sanchez.sergio.service.AccountService;

/**
 * @author sergio
 */
@Service
@Transactional
public class ActivateAccount extends Action<Account> {

    public Account apply(Account account) {
        
        Assert.isTrue(account.getStatus() != ACCOUNT_ACTIVE, "The account is already active");
        Assert.isTrue(Arrays.asList(ACCOUNT_CONFIRMED, ACCOUNT_SUSPENDED, ACCOUNT_ARCHIVED)
                .contains(account.getStatus()), "The account cannot be activated");

        AccountService accountService = account.getModule(AccountModule.class).getDefaultService();
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
