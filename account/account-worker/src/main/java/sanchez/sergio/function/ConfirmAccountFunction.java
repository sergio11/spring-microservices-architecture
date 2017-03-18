package sanchez.sergio.function;

import java.net.URI;
import java.util.function.Function;
import org.apache.log4j.Logger;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.statemachine.StateContext;
import sanchez.sergio.domain.account.Account;
import sanchez.sergio.domain.account.AccountStatus;
import sanchez.sergio.domain.event.AccountEvent;
import sanchez.sergio.domain.event.AccountEventType;

/**
 * @author sergio
 */
public class ConfirmAccountFunction extends AccountFunction {

    final private Logger log = Logger.getLogger(ConfirmAccountFunction.class);

    public ConfirmAccountFunction(StateContext<AccountStatus, AccountEventType> context) {
        this(context, null);
    }
    
    public ConfirmAccountFunction(StateContext<AccountStatus, AccountEventType> context, Function<AccountEvent, Account> lambda) {
        super(context, lambda);
    }

    /**
     * @param event is the {@link AccountEvent} to apply to the lambda function
     */
    @Override
    public Account apply(AccountEvent event) {
        log.info("Executing workflow for a confirmed account...");
        // Get the account resource for the event
        Traverson traverson = new Traverson(
                URI.create(event.getLink("account").getHref()),
                MediaTypes.HAL_JSON
        );

        // Follow the command resource to activate the account
        Account account = traverson.follow("commands")
                .follow("activate")
                .toEntity(Account.class)
                .getBody();

        log.info(event.getType() + ": " + event.getLink("account").getHref());

        return account;
    }
}
