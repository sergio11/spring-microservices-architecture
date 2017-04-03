package sanchez.sergio.function;

import java.net.URI;
import java.util.function.Function;
import org.apache.log4j.Logger;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.RequestEntity;
import org.springframework.statemachine.StateContext;
import org.springframework.web.client.RestTemplate;
import sanchez.sergio.domain.account.Account;
import sanchez.sergio.domain.account.AccountStatus;
import sanchez.sergio.domain.event.AccountEvent;
import sanchez.sergio.domain.event.AccountEventType;

/**
 * @author sergio
 */
public class CreateAccountFunction extends AccountFunction {

    final private Logger log = Logger.getLogger(CreateAccountFunction.class);

    public CreateAccountFunction(StateContext<AccountStatus, AccountEventType> context) {
        this(context, null);
    }

    public CreateAccountFunction(StateContext<AccountStatus, AccountEventType> context,
                         Function<AccountEvent, Account> function) {
        super(context, function);
    }

    /**
     * Applies the {@link AccountEvent} to the {@link Account} aggregate.
     * @param event is the {@link AccountEvent} for this context
     */
    @Override
    public Account apply(AccountEvent event) {

        Account account;

        log.info("Executing workflow for a created account...");

        // Create a traverson for the root account
        Traverson traverson = new Traverson(
                URI.create(event.getLink("account").getHref()),
                MediaTypes.HAL_JSON
        );

        // Get the account resource attached to the event
        account = traverson.follow("self")
                .toEntity(Account.class)
                .getBody();

        // Set the account to a pending state
        account = setAccountPendingStatus(event, account);

        // The account can only be confirmed if it is in a pending state
        if (account.getStatus() == AccountStatus.ACCOUNT_PENDING) {
            // Traverse to the confirm account command
            account = traverson.follow("commands")
                    .follow("confirm")
                    .toEntity(Account.class)
                    .getBody();

            log.info(event.getType() + ": " +
                    event.getLink("account").getHref());
        }

        context.getExtendedState().getVariables().put("account", account);

        return account;
    }

    /**
     * Set the {@link Account} resource to a pending state.
     *
     * @param event   is the {@link AccountEvent} for this context
     * @param account is the {@link Account} attached to the {@link AccountEvent} resource
     * @return an {@link Account} with its updated state set to pending
     */
    private Account setAccountPendingStatus(AccountEvent event, Account account) {
        // Set the account status to pending
        account.setStatus(AccountStatus.ACCOUNT_PENDING);
        RestTemplate restTemplate = new RestTemplate();

        // Create a new request entity
        RequestEntity<Account> requestEntity = RequestEntity.put(
                URI.create(event.getLink("account").getHref()))
                .contentType(MediaTypes.HAL_JSON)
                .body(account);

        // Update the account entity's status
        account = restTemplate.exchange(requestEntity, Account.class).getBody();

        return account;
    }
}
