package sanchez.sergio.action;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sanchez.sergio.domain.Account;
import sanchez.sergio.domain.AccountStatus;
import static sanchez.sergio.domain.AccountStatus.ACCOUNT_ACTIVE;
import static sanchez.sergio.domain.AccountStatus.ACCOUNT_SUSPENDED;
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
public class SuspendAccount extends Action<Account> {

    public Account apply(Account account) {
        
        Assert.isTrue(account.getStatus() != ACCOUNT_SUSPENDED, "The account is already suspended");
        Assert.isTrue(account.getStatus() == ACCOUNT_ACTIVE, "An inactive account cannot be suspended");

        AccountService accountService = account.getModule(AccountModule.class)
                .getDefaultService();
        // Save PreState
        AccountStatus status = account.getStatus();

        // Suspend the account
        account.setStatus(AccountStatus.ACCOUNT_SUSPENDED);
        account = accountService.update(account);

        try {
            // Trigger the account suspended event
            account.sendAsyncEvent(new AccountEvent(AccountEventType.ACCOUNT_SUSPENDED, account));
        } catch (Exception ex) {
            // Rollback the operation
            account.setStatus(status);
            account = accountService.update(account);
        }

        return account;
    }
}
