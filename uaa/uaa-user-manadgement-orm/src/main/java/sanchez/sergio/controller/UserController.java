package sanchez.sergio.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.lang.reflect.Method;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sanchez.sergio.persistence.entities.User;
import sanchez.sergio.domain.Users;
import sanchez.sergio.event.EventService;
import sanchez.sergio.persistence.entities.AccountEvent;
import sanchez.sergio.security.userdetails.CommonUserDetailsAware;
import sanchez.sergio.service.UserService;

/**
 * @author sergio
 */
@Api
@RestController
@RequestMapping("/api/v1/")
public class UserController {
    
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService accountService;
    private final EventService<AccountEvent, Long> eventService;

    public UserController(UserService accountService, EventService<AccountEvent, Long> eventService) {
        this.accountService = accountService;
        this.eventService = eventService;
    }
    
    @GetMapping(path = "/accounts")
    @ApiOperation(value = "getAccounts", nickname = "getAccounts", notes="Get accounts resources", response = ResponseEntity.class)
    public ResponseEntity getAccounts(
            @ApiParam(value = "pageRequest", required = false) @RequestBody(required = false) PageRequest pageRequest,
            @AuthenticationPrincipal CommonUserDetailsAware principal) {
        if(principal != null)
            logger.info("Principal FirstName: " + principal.getFirstName());
        return new ResponseEntity<>(getAccountsResource(pageRequest), HttpStatus.OK);
    }
    

    @PostMapping(path = "/accounts", produces={ MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
    @ApiOperation(value = "createAccount", nickname = "createAccount", notes="Create a new account", response = ResponseEntity.class)
    public ResponseEntity createAccount(@ApiParam(value = "account", required = true) @Valid @RequestBody User account) {
       
        return Optional.ofNullable(createAccountResource(account))
                .map(e -> new ResponseEntity<>(e, HttpStatus.CREATED))
                .orElseThrow(() -> new RuntimeException("Account creation failed"));
    }

    @PutMapping(path = "/accounts/{id}")
    @ApiOperation(value = "updateAccount", nickname = "updateAccount", notes="Update account", response = ResponseEntity.class)
    public ResponseEntity updateAccount(
            @ApiParam(value = "id", required = true) @PathVariable Long id,
            @ApiParam(value = "account", required = true) @RequestBody User account) {
        return Optional.ofNullable(updateAccountResource(id, account))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Account update failed"));
    }
    
    @GetMapping(path = "/accounts/{id}")
    @ApiOperation(value = "getAccount", nickname = "getAccount", notes="Get a account", response = ResponseEntity.class)
    public ResponseEntity getAccount(@ApiParam(value = "id", required = true) @PathVariable Long id) {
        return Optional.ofNullable(accountService.get(id))
                .map(this::getAccountResource)
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/accounts/{id}")
    @ApiOperation(value = "deleteAccount", nickname = "deleteAccount", notes="Delete a account", response = ResponseEntity.class)
    public ResponseEntity deleteAccount(@ApiParam(value = "id", required = true) @PathVariable Long id) {
        return Optional.ofNullable(accountService.delete(id))
                .map(e -> new ResponseEntity<>(HttpStatus.NO_CONTENT))
                .orElseThrow(() -> new RuntimeException("Account deletion failed"));
    }
    
    @GetMapping(path = "/accounts/{id}/events")
    @ApiOperation(value = "getAccountEvents", nickname = "getAccountEvents", notes="Get account's events", response = ResponseEntity.class)
    public ResponseEntity getAccountEvents(@ApiParam(value = "id", required = true) @PathVariable Long id) {
        return Optional.of(eventService.find(id))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Could not get account events"));
    }

    @GetMapping(path = "/accounts/{id}/events/{eventId}")
    @ApiOperation(value = "getAccountEvent", nickname = "getAccountEvent", notes="Get a event for any account", response = ResponseEntity.class)
    public ResponseEntity getAccountEvent(
            @ApiParam(value = "id", required = true) @PathVariable Long id, 
            @ApiParam(value = "eventId", required = true) @PathVariable Long eventId) {
        return Optional.of(eventService.findOne(eventId))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("Could not get order events"));
    }

    @PostMapping(path = "/accounts/{id}/events")
    @ApiOperation(value = "appendAccountEvent", nickname = "appendAccountEvent", notes="Append account event", response = ResponseEntity.class)
    public ResponseEntity appendAccountEvent(
            @ApiParam(value = "id", required = true) @PathVariable Long id, 
            @ApiParam(value = "event", required = true) @RequestBody AccountEvent event) {
        return Optional.ofNullable(appendEventResource(id, event))
                .map(e -> new ResponseEntity<>(e, HttpStatus.CREATED))
                .orElseThrow(() -> new RuntimeException("Append account event failed"));
    }
    
    @GetMapping(path = "/accounts/{id}/commands")
    @ApiOperation(value = "getCommands", nickname = "getCommands", notes="Get account's commands", response = ResponseEntity.class)
    public ResponseEntity getCommands(@ApiParam(value = "id", required = true) @PathVariable Long id) {
        return Optional.ofNullable(getCommandsResource(id))
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The account could not be found"));
    }

    @PutMapping(path = "/accounts/{id}/commands/confirm")
    @ApiOperation(value = "confirm", nickname = "confirm", notes="Confirm account", response = ResponseEntity.class)
    public ResponseEntity confirm(@ApiParam(value = "id", required = true) @PathVariable Long id) {
        return Optional.ofNullable(accountService.get(id))
                .map(User::confirm)
                .map(this::getAccountResource)
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @PutMapping(path = "/accounts/{id}/commands/activate")
    @ApiOperation(value = "activate", nickname = "activate", notes="Activate account", response = ResponseEntity.class)
    public ResponseEntity activate(@ApiParam(value = "id", required = true) @PathVariable Long id) {
        return Optional.ofNullable(accountService.get(id))
                .map(User::activate)
                .map(this::getAccountResource)
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @PutMapping(path = "/accounts/{id}/commands/suspend")
    @ApiOperation(value = "suspend", nickname = "suspend", notes="Suspend account", response = ResponseEntity.class)
    public ResponseEntity suspend(@ApiParam(value = "id", required = true) @PathVariable Long id) {
        return Optional.ofNullable(accountService.get(id))
                .map(User::suspend)
                .map(this::getAccountResource)
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }

    @PutMapping(path = "/accounts/{id}/commands/archive")
    @ApiOperation(value = "archive", nickname = "archive", notes="Archive account", response = ResponseEntity.class)
    public ResponseEntity archive(@ApiParam(value = "id", required = true) @PathVariable Long id) {
        return Optional.ofNullable(accountService.get(id))
                .map(User::archive)
                .map(this::getAccountResource)
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseThrow(() -> new RuntimeException("The command could not be applied"));
    }
    
    
    /**
     * Appends an {@link AccountEvent} domain event to the event log of the
     * {@link User} aggregate with the specified accountId.
     *
     * @param accountId is the unique identifier for the {@link User}
     * @param event is the {@link AccountEvent} that attempts to alter the state
     * of the {@link User}
     * @return a hypermedia resource for the newly appended {@link AccountEvent}
     */
    private Resource<AccountEvent> appendEventResource(Long accountId, AccountEvent event) {
        Assert.notNull(event, "Event body must be provided");

        User account = accountService.get(accountId);
        Assert.notNull(account, "Account could not be found");

        event.setEntity(account);
        account.sendAsyncEvent(event);

        return new Resource<>(event,
                linkTo(UserController.class)
                        .slash("accounts")
                        .slash(accountId)
                        .slash("events")
                        .slash(event.getId())
                        .withSelfRel(),
                linkTo(UserController.class)
                        .slash("accounts")
                        .slash(accountId)
                        .withRel("account")
        );
    }
   
    private LinkBuilder linkBuilder(String name, Long id) {
        Method method;
        try {
            method = UserController.class.getMethod(name, Long.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return linkTo(UserController.class, method, id);
    }
    
    private Users getAccountsResource(PageRequest pageRequest) {
        if (pageRequest == null) {
            pageRequest = new PageRequest(1, 20);
        }
        Page<User> accountPage = accountService.findAll(pageRequest.first());
        Users accounts = new Users(accountPage);
        accounts.add(new Link(new UriTemplate(linkTo(UserController.class).slash("accounts").toUri().toString())
                .with("page", TemplateVariable.VariableType.REQUEST_PARAM)
                .with("size", TemplateVariable.VariableType.REQUEST_PARAM), "self"));
        return accounts;
    }
    
    /**
     * Creates a new {@link User} entity and persists the result to the
     * repository.
     *
     * @param account is the {@link User} model used to create a new account
     * @return a hypermedia resource for the newly created {@link User}
     */
    private Resource<User> createAccountResource(User account) {
        Assert.notNull(account, "Account body must not be null");
        Assert.notNull(account.getEmail(), "Email is required");
        Assert.notNull(account.getFirstName(), "First name is required");
        // Create the new account
        account = accountService.registerAccount(account);

        return getAccountResource(account);
    }
    
    /**
     * Get a hypermedia enriched {@link User} entity.
     * @param account is the {@link User} to enrich with hypermedia links
     * @return is a hypermedia enriched resource for the supplied {@link User} entity
     */
    private Resource<User> getAccountResource(User account) {
        Assert.notNull(account, "Account must not be null");

        if (!account.hasLink("commands")) {
            // Add command link
            account.add(linkBuilder("getCommands", account.getIdentity()).withRel("commands"));
        }

        if (!account.hasLink("events")) {
            // Add get events link
            account.add(linkBuilder("getAccountEvents", account.getIdentity()).withRel("events"));
        }
        
        return new Resource<>(account);
    }
    
    /**
     * Update a {@link User} entity for the provided identifier.
     *
     * @param id is the unique identifier for the {@link User} update
     * @param account is the entity representation containing any updated
     * {@link User} fields
     * @return a hypermedia resource for the updated {@link User}
     */
    private Resource<User> updateAccountResource(Long id, User account) {
        account.setIdentity(id);
        return getAccountResource(accountService.update(account));
    }
    
    private ResourceSupport getCommandsResource(Long id) {
        User account = new User();
        account.setIdentity(id);
        return new Resource<>(account.getCommands());
    }
}
