package sanchez.sergio.service;

import java.util.UUID;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;
import sanchez.sergio.domain.account.AccountStatus;
import sanchez.sergio.domain.event.AccountEventType;

@Service
public class StateMachineService {

    private final StateMachineFactory<AccountStatus, AccountEventType> factory;

    public StateMachineService(StateMachineFactory<AccountStatus, AccountEventType> factory) {
        this.factory = factory;
    }

    /**
     * Create a new state machine that is initially configured and ready for replicating
     * the state of an {@link Account} from a sequence of {@link AccountEvent}.
     * @return a new instance of {@link StateMachine}
     */
    public StateMachine<AccountStatus, AccountEventType> getStateMachine() {
        // Create a new state machine in its initial state
        StateMachine<AccountStatus, AccountEventType> stateMachine =
                factory.getStateMachine(UUID.randomUUID().toString());

        // Start the new state machine
        stateMachine.start();

        return stateMachine;
    }
}
