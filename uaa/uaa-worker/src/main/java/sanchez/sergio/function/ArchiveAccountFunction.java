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
 *
 * @author sergio
 */
public class ArchiveAccountFunction extends AccountFunction {
    
    final private Logger log = Logger.getLogger(ArchiveAccountFunction.class);
    
    public ArchiveAccountFunction(StateContext<AccountStatus, AccountEventType> context){
        this(context, null);
    }

    public ArchiveAccountFunction(StateContext<AccountStatus, AccountEventType> context, Function<AccountEvent, Account> lambda) {
        super(context, lambda);
    }
    
    @Override
    public Account apply(AccountEvent event) {
        log.info("Executing workflow for an archive account...");
        log.info(event.getType() + ": " + event.getLink("account").getHref());
        // Get the account resource for the event
        Traverson traverson = new Traverson(
                URI.create(event.getLink("account").getHref()),
                MediaTypes.HAL_JSON
        );

        return traverson.follow("self")
                .toEntity(Account.class)
                .getBody();
    }
}
