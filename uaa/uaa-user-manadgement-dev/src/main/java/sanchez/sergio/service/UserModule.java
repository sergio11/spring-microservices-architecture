package sanchez.sergio.service;

import sanchez.sergio.persistence.entities.User;
import sanchez.sergio.domain.Module;
import sanchez.sergio.event.EventService;
import sanchez.sergio.persistence.entities.AccountEvent;

/**
 * @author sergio
 */
@org.springframework.stereotype.Service
public class UserModule extends Module<User> {

    private final UserService accountService;
    private final EventService<AccountEvent, Long> eventService;

    public UserModule(UserService accountService, EventService<AccountEvent, Long> eventService) {
        this.accountService = accountService;
        this.eventService = eventService;
    }

    public UserService getAccountService() {
        return accountService;
    }

    public EventService<AccountEvent, Long> getEventService() {
        return eventService;
    }

    @Override
    public UserService getDefaultService() {
        return accountService;
    }

    @Override
    public EventService<AccountEvent, Long> getDefaultEventService() {
        return eventService;
    }
}
