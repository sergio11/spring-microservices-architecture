package sanchez.sergio.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import java.util.EnumSet;
import sanchez.sergio.domain.account.AccountStatus;
import sanchez.sergio.domain.event.AccountEvent;
import sanchez.sergio.domain.event.AccountEventType;
import sanchez.sergio.function.AccountFunction;
import sanchez.sergio.function.ActivateAccountFunction;
import sanchez.sergio.function.ArchiveAccountFunction;
import sanchez.sergio.function.ConfirmAccountFunction;
import sanchez.sergio.function.CreateAccountFunction;
import sanchez.sergio.function.SuspendAccountFunction;
import sanchez.sergio.function.UnSuspendAccountFunction;
import sanchez.sergio.function.UnarchiveAccountFunction;

/**
 * A configuration adapter for describing a {@link StateMachine} factory that maps actions to functional
 * expressions. Actions are executed during transitions between a source state and a target state.
 * A state machine provides a robust declarative language for describing the state of an {@link Account}
 * resource given a sequence of ordered {@link AccountEvents}. When an event is received
 * in {@link AccountEventStream}, an in-memory state machine is fully replicated given the
 * {@link AccountEvents} attached to an {@link Account} resource.
 *
 * @author sergio
 */
@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<AccountStatus, AccountEventType> {

    final private Logger log = Logger.getLogger(StateMachineConfig.class);

    /**
     * Configures the initial conditions of a new in-memory {@link StateMachine} for {@link Account}.
     *
     * @param states is the {@link StateMachineStateConfigurer} used to describe the initial condition
     */
    @Override
    public void configure(StateMachineStateConfigurer<AccountStatus, AccountEventType> states) {
        try {
            // Describe the initial condition of the account state machine
            states.withStates()
                    .initial(AccountStatus.ACCOUNT_CREATED)
                    .states(EnumSet.allOf(AccountStatus.class));
        } catch (Exception e) {
            throw new RuntimeException("State machine configuration failed", e);
        }
    }

    /**
     * Configures the {@link StateMachine} that describes how {@link AccountEventType} drives the state
     * of an {@link Account}. Events are applied as transitions from a source {@link AccountStatus} to
     * a target {@link AccountStatus}. An {@link Action} is attached to each transition, which maps to a
     * function that is executed in the context of an {@link AccountEvent}.
     *
     * @param transitions is the {@link StateMachineTransitionConfigurer} used to describe state transitions
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<AccountStatus, AccountEventType> transitions) {
        try {
            // Describe state machine transitions for accounts
            transitions.withExternal()
                    .source(AccountStatus.ACCOUNT_CREATED)
                    .target(AccountStatus.ACCOUNT_PENDING)
                    .event(AccountEventType.ACCOUNT_CREATED)
                    .action(createAccount())
                    .and()
                    .withExternal()
                    .source(AccountStatus.ACCOUNT_PENDING)
                    .target(AccountStatus.ACCOUNT_CONFIRMED)
                    .event(AccountEventType.ACCOUNT_CONFIRMED)
                    .action(confirmAccount())
                    .and()
                    .withExternal()
                    .source(AccountStatus.ACCOUNT_CONFIRMED)
                    .target(AccountStatus.ACCOUNT_ACTIVE)
                    .event(AccountEventType.ACCOUNT_ACTIVATED)
                    .action(activateAccount())
                    .and()
                    .withExternal()
                    .source(AccountStatus.ACCOUNT_ACTIVE)
                    .target(AccountStatus.ACCOUNT_ARCHIVED)
                    .event(AccountEventType.ACCOUNT_ARCHIVED)
                    .action(archiveAccount())
                    .and()
                    .withExternal()
                    .source(AccountStatus.ACCOUNT_ACTIVE)
                    .target(AccountStatus.ACCOUNT_SUSPENDED)
                    .event(AccountEventType.ACCOUNT_SUSPENDED)
                    .action(suspendAccount())
                    .and()
                    .withExternal()
                    .source(AccountStatus.ACCOUNT_ARCHIVED)
                    .target(AccountStatus.ACCOUNT_ACTIVE)
                    .event(AccountEventType.ACCOUNT_ACTIVATED)
                    .action(unarchiveAccount())
                    .and()
                    .withExternal()
                    .source(AccountStatus.ACCOUNT_SUSPENDED)
                    .target(AccountStatus.ACCOUNT_ACTIVE)
                    .event(AccountEventType.ACCOUNT_ACTIVATED)
                    .action(unsuspendAccount());
        } catch (Exception e) {
            throw new RuntimeException("Could not configure state machine transitions", e);
        }
    }

    /**
     * The {@link AccountFunction} argument is only applied if an {@link AccountEvent} is provided as a
     * message header in the {@link StateContext}.
     *
     * @param context         is the state machine context that may include an {@link AccountEvent}
     * @param accountFunction is the account function to apply after the state machine has completed replication
     * @return an {@link AccountEvent} only if this event has not yet been processed, otherwise returns null
     */
    private AccountEvent applyEvent(StateContext<AccountStatus, AccountEventType> context, AccountFunction accountFunction) {
        AccountEvent accountEvent = null;

        // Log out the progress of the state machine replication
        log.info("Replicate event: " + context.getMessage().getPayload());

        // The machine is finished replicating when an AccountEvent is found in the message header
        if (context.getMessageHeader("event") != null) {
            accountEvent = (AccountEvent) context.getMessageHeader("event");
            log.info("State machine replicated: " + accountEvent.getType());

            // Apply the provided function to the AccountEvent
            accountFunction.apply(accountEvent);
        }

        return accountEvent;
    }

    /**
     * The action that is triggered in response to an account transitioning from ACCOUNT_CREATED
     * to ACCOUNT_PENDING.
     * @return an implementation of {@link Action} that includes a function to execute
     */
    @Bean
    public Action<AccountStatus, AccountEventType> createAccount() {
        return context -> applyEvent(context, new CreateAccountFunction(context));
    }

    /**
     * The action that is triggered in response to an account transitioning from ACCOUNT_PENDING
     * to ACCOUNT_CONFIRMED.
     * @return an implementation of {@link Action} that includes a function to execute
     */
    @Bean
    public Action<AccountStatus, AccountEventType> confirmAccount() {
        return context -> applyEvent(context, new ConfirmAccountFunction(context));
    }

    /**
     * The action that is triggered in response to an account transitioning from ACCOUNT_CONFIRMED
     * to ACCOUNT_ACTIVE.
     *
     * @return an implementation of {@link Action} that includes a function to execute
     */
    @Bean
    public Action<AccountStatus, AccountEventType> activateAccount() {
        return context -> applyEvent(context, new ActivateAccountFunction(context));
    }

    /**
     * The action that is triggered in response to an account transitioning from ACCOUNT_ACTIVE
     * to ACCOUNT_ARCHIVED.
     *
     * @return an implementation of {@link Action} that includes a function to execute
     */
    @Bean
    public Action<AccountStatus, AccountEventType> archiveAccount() {
        return context -> applyEvent(context, new ArchiveAccountFunction(context));
    }

    /**
     * The action that is triggered in response to an account transitioning from ACCOUNT_ACTIVE
     * to ACCOUNT_SUSPENDED.
     *
     * @return an implementation of {@link Action} that includes a function to execute
     */
    @Bean
    public Action<AccountStatus, AccountEventType> suspendAccount() {
        return context -> applyEvent(context, new SuspendAccountFunction(context));
    }

    /**
     * The action that is triggered in response to an account transitioning from ACCOUNT_ARCHIVED
     * to ACCOUNT_ACTIVE.
     *
     * @return an implementation of {@link Action} that includes a function to execute
     */
    @Bean
    public Action<AccountStatus, AccountEventType> unarchiveAccount() {
        return context -> applyEvent(context,  new UnarchiveAccountFunction(context));
    }

    /**
     * The action that is triggered in response to an account transitioning from ACCOUNT_SUSPENDED
     * to ACCOUNT_ACTIVE.
     *
     * @return an implementation of {@link Action} that includes a function to execute
     */
    @Bean
    public Action<AccountStatus, AccountEventType> unsuspendAccount() {
        return context -> applyEvent(context, new UnSuspendAccountFunction(context));
    }
}
