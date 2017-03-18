package sanchez.sergio.function;

import java.util.function.Function;
import org.apache.log4j.Logger;
import org.springframework.statemachine.StateContext;
import sanchez.sergio.domain.account.Account;
import sanchez.sergio.domain.account.AccountStatus;
import sanchez.sergio.domain.event.AccountEvent;
import sanchez.sergio.domain.event.AccountEventType;

/**
 * The {@link AccountFunction} is an abstraction used to map actions that are triggered by
 * state transitions on a {@link Account} resource on to a function.
 */
public abstract class AccountFunction {

    final private Logger log = Logger.getLogger(AccountFunction.class);
    final protected StateContext<AccountStatus, AccountEventType> context;
    final protected Function<AccountEvent, Account> lambda;

    /**
     * Create a new instance of a class that extends {@link AccountFunction}, supplying
     * a state context and a lambda function used to apply {@link AccountEvent} to a provided
     * action.
     *
     * @param context is the {@link StateContext} for a replicated state machine
     * @param lambda  is the lambda function describing an action that consumes an {@link AccountEvent}
     */
    public AccountFunction(StateContext<AccountStatus, AccountEventType> context,
                           Function<AccountEvent, Account> lambda) {
        this.context = context;
        this.lambda = lambda;
    }

    /**
     * Apply an {@link AccountEvent} to the lambda function that was provided through the
     * constructor of this {@link AccountFunction}.
     *
     * @param event is the {@link AccountEvent} to apply to the lambda function
     */
    public Account apply(AccountEvent event) {
        // Execute the lambda function
        Account result = lambda.apply(event);
        context.getExtendedState().getVariables().put("account", result);
        return result;
    }
}