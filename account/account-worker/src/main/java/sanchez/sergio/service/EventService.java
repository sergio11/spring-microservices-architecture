package sanchez.sergio.service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import sanchez.sergio.domain.account.Account;
import sanchez.sergio.domain.account.AccountStatus;
import sanchez.sergio.domain.event.AccountEvent;
import sanchez.sergio.domain.event.AccountEventType;
import sanchez.sergio.domain.event.AccountEvents;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

/**
 *
 * @author sergio
 */
@Service
public class EventService {

    final private StateMachineService stateMachineService;
    
    public EventService(StateMachineService stateMachineService) {
        this.stateMachineService = stateMachineService;
    }

    public Account apply(AccountEvent accountEvent) {
        Account result;
        // Generate a state machine for computing the state of the account resource
        StateMachine<AccountStatus, AccountEventType> stateMachine
                = stateMachineService.getStateMachine();

        // Follow the hypermedia link to fetch the attached account
        Traverson traverson = new Traverson(
                URI.create(accountEvent.getLink("account").getHref()),
                MediaTypes.HAL_JSON
        );

        // Get the event log for the attached account resource
        AccountEvents events = traverson.follow("events")
                .toEntity(AccountEvents.class)
                .getBody();

        // Prepare account event message headers
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("event", accountEvent);

        // Replicate the current state of the account resource
        events.getContent()
                .stream()
                .sorted((a1, a2) -> a1.getCreatedAt().compareTo(a2.getCreatedAt()))
                .forEach(e -> {
                    MessageHeaders headers = new MessageHeaders(null);
                    // Check to see if this is the current event
                    if (e.getLink("self").equals(accountEvent.getLink("self"))) {
                        headers = new MessageHeaders(headerMap);
                    }
                    // Send the event to the state machine
                    stateMachine.sendEvent(MessageBuilder.createMessage(e.getType(), headers));
                });

        // Get result
        Map<Object, Object> context = stateMachine.getExtendedState().getVariables();

        // Get the account result
        result = (Account) context.getOrDefault("account", null);

        // Destroy the state machine
        stateMachine.stop();

        return result;
    }

}
