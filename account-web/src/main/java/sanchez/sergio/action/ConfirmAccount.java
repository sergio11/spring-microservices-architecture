package sanchez.sergio.action;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sanchez.sergio.domain.Account;
import sanchez.sergio.domain.AccountStatus;
import static sanchez.sergio.domain.AccountStatus.ACCOUNT_CONFIRMED;
import static sanchez.sergio.domain.AccountStatus.ACCOUNT_PENDING;
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
public class ConfirmAccount extends Action<Account> {

    public Account apply(Account account) {
        
        Assert.isTrue(account.getStatus() != ACCOUNT_CONFIRMED, "The account has already been confirmed");
        Assert.isTrue(account.getStatus() == ACCOUNT_PENDING, "The account has already been confirmed");

        AccountService accountService = account.getModule(AccountModule.class).getDefaultService();

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
