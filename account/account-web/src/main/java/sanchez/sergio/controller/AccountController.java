package sanchez.sergio.controller;

import java.lang.reflect.Method;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.UriTemplate;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sanchez.sergio.domain.Account;
import sanchez.sergio.domain.Accounts;
import sanchez.sergio.event.EventService;
import sanchez.sergio.events.AccountEvent;
import sanchez.sergio.service.AccountService;

/**
 *
 * @author sergio
 */
@RestController
@RequestMapping("/v1")
public class AccountController {

    private final AccountService accountService;
    private final EventService<AccountEvent, Long> eventService;

    public AccountController(AccountService accountService, EventService<AccountEvent, Long> eventService) {
        this.accountService = accountService;
        this.eventService = eventService;
    }
    
    @RequestMapping(path = "/accounts")
    public ResponseEntity getAccounts(@RequestBody(required = false) PageRequest pageRequest) {
        return new ResponseEntity<>(getAccountsResource(pageRequest), HttpStatus.OK);
    }
    
    @PostMapping(path = "/accounts")
    public ResponseEntity createAccount(@RequestBody Account account) {
        return Optional.ofNullable(createAccountResource(account))
                .map(e -> new ResponseEntity<>(e, HttpStatus.CREATED))
                .orElseThrow(() -> new RuntimeException("Account creation failed"));
    }

    @PutMapping(path = "/accounts/{id}")
    public ResponseEntity updateAccount(@RequestBody Account account, @PathVariable Long id) {
        return Optional.ofNullable(updateAccountResource(id, account))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Account update failed"));
    }
    
    @RequestMapping(path = "/accounts/{id}")
    public ResponseEntity getAccount(@PathVariable Long id) {
        return Optional.ofNullable(accountService.get(id))
                .map(this::getAccountResource)
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/accounts/{id}")
    public ResponseEntity deleteAccount(@PathVariable Long id) {
        return Optional.ofNullable(accountService.delete(id))
                .map(e -> new ResponseEntity<>(HttpStatus.NO_CONTENT))
                .orElseThrow(() -> new RuntimeException("Account deletion failed"));
    }
    
    @RequestMapping(path = "/accounts/{id}/events")
    public ResponseEntity getAccountEvents(@PathVariable Long id) {
        return Optional.of(eventService.find(id))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Could not get account events"));
    }

    @RequestMapping(path = "/accounts/{id}/events/{eventId}")
    public ResponseEntity getAccountEvent(@PathVariable Long id, @PathVariable Long eventId) {
        return Optional.of(eventService.findOne(eventId))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Could not get order events"));
    }

    @PostMapping(path = "/accounts/{id}/events")
    public ResponseEntity appendAccountEvent(@PathVariable Long id, @RequestBody AccountEvent event) {
        return Optional.ofNullable(appendEventResource(id, event))
                .map(e -> new ResponseEntity<>(e, HttpStatus.CREATED))
                .orElseThrow(() -> new RuntimeException("Append account event failed"));
    }
    
    @RequestMapping(path = "/accounts/{id}/commands")
    public ResponseEntity getCommands(@PathVariable Long id) {
        return Optional.ofNullable(getCommandsResource(id))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The account could not be found"));
    }

    @RequestMapping(path = "/accounts/{id}/commands/confirm")
    public ResponseEntity confirm(@PathVariable Long id) {
        return Optional.ofNullable(accountService.get(id))
                .map(Account::confirm)
                .map(this::getAccountResource)
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @RequestMapping(path = "/accounts/{id}/commands/activate")
    public ResponseEntity activate(@PathVariable Long id) {
        return Optional.ofNullable(accountService.get(id))
                .map(Account::activate)
                .map(this::getAccountResource)
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @RequestMapping(path = "/accounts/{id}/commands/suspend")
    public ResponseEntity suspend(@PathVariable Long id) {
        return Optional.ofNullable(accountService.get(id))
                .map(Account::suspend)
                .map(this::getAccountResource)
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @RequestMapping(path = "/accounts/{id}/commands/archive")
    public ResponseEntity archive(@PathVariable Long id) {
        return Optional.ofNullable(accountService.get(id))
                .map(Account::archive)
                .map(this::getAccountResource)
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }
    
    
    /**
     * Appends an {@link AccountEvent} domain event to the event log of the
     * {@link Account} aggregate with the specified accountId.
     *
     * @param accountId is the unique identifier for the {@link Account}
     * @param event is the {@link AccountEvent} that attempts to alter the state
     * of the {@link Account}
     * @return a hypermedia resource for the newly appended {@link AccountEvent}
     */
    private Resource<AccountEvent> appendEventResource(Long accountId, AccountEvent event) {
        Assert.notNull(event, "Event body must be provided");

        Account account = accountService.get(accountId);
        Assert.notNull(account, "Account could not be found");

        event.setEntity(account);
        account.sendAsyncEvent(event);

        return new Resource<>(event,
                linkTo(AccountController.class)
                        .slash("accounts")
                        .slash(accountId)
                        .slash("events")
                        .slash(event.getEventId())
                        .withSelfRel(),
                linkTo(AccountController.class)
                        .slash("accounts")
                        .slash(accountId)
                        .withRel("account")
        );
    }
   
    private LinkBuilder linkBuilder(String name, Long id) {
        Method method;
        try {
            method = AccountController.class.getMethod(name, Long.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return linkTo(AccountController.class, method, id);
    }
    
    private Accounts getAccountsResource(PageRequest pageRequest) {
        if (pageRequest == null) {
            pageRequest = new PageRequest(1, 20);
        }
        Page<Account> accountPage = accountService.findAll(pageRequest.first());
        Accounts accounts = new Accounts(accountPage);
        accounts.add(new Link(new UriTemplate(linkTo(AccountController.class).slash("accounts").toUri().toString())
                .with("page", TemplateVariable.VariableType.REQUEST_PARAM)
                .with("size", TemplateVariable.VariableType.REQUEST_PARAM), "self"));
        return accounts;
    }
    
    /**
     * Creates a new {@link Account} entity and persists the result to the
     * repository.
     *
     * @param account is the {@link Account} model used to create a new account
     * @return a hypermedia resource for the newly created {@link Account}
     */
    private Resource<Account> createAccountResource(Account account) {
        Assert.notNull(account, "Account body must not be null");
        Assert.notNull(account.getEmail(), "Email is required");
        Assert.notNull(account.getFirstName(), "First name is required");
        Assert.notNull(account.getLastName(), "Last name is required");
        // Create the new account
        account = accountService.registerAccount(account);

        return getAccountResource(account);
    }
    
    /**
     * Get a hypermedia enriched {@link Account} entity.
     * @param account is the {@link Account} to enrich with hypermedia links
     * @return is a hypermedia enriched resource for the supplied {@link Account} entity
     */
    private Resource<Account> getAccountResource(Account account) {
        Assert.notNull(account, "Account must not be null");

        if (!account.hasLink("commands")) {
            // Add command link
            account.add(linkBuilder("getCommands", account.getIdentity()).withRel("commands"));
        }

        if (!account.hasLink("events")) {
            // Add get events link
            account.add(linkBuilder("getAccountEvents", account.getIdentity()).withRel("events"));
        }

        if (!account.hasLink("orders")) {
            // Add orders link
            account.add(linkBuilder("getAccountOrders", account.getIdentity()).withRel("orders"));
        }

        return new Resource<>(account);
    }
    
    /**
     * Update a {@link Account} entity for the provided identifier.
     *
     * @param id is the unique identifier for the {@link Account} update
     * @param account is the entity representation containing any updated
     * {@link Account} fields
     * @return a hypermedia resource for the updated {@link Account}
     */
    private Resource<Account> updateAccountResource(Long id, Account account) {
        account.setIdentity(id);
        return getAccountResource(accountService.update(account));
    }
    
    private ResourceSupport getCommandsResource(Long id) {
        Account account = new Account();
        account.setIdentity(id);
        return new Resource<>(account.getCommands());
    }
}