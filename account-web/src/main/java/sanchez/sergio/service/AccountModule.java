package sanchez.sergio.service;

import sanchez.sergio.domain.Account;
import sanchez.sergio.domain.Module;
import sanchez.sergio.event.EventService;
import sanchez.sergio.events.AccountEvent;

/**
 * @author sergio
 */
@org.springframework.stereotype.Service
public class AccountModule extends Module<Account> {

    private final AccountService accountService;
    private final EventService<AccountEvent, Long> eventService;

    public AccountModule(AccountService accountService, EventService<AccountEvent, Long> eventService) {
        this.accountService = accountService;
        this.eventService = eventService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public EventService<AccountEvent, Long> getEventService() {
        return eventService;
    }

    @Override
    public AccountService getDefaultService() {
        return accountService;
    }

    @Override
    public EventService<AccountEvent, Long> getDefaultEventService() {
        return eventService;
    }
}
