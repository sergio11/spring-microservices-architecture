package sanchez.sergio.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.UriTemplate;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(path = "/accounts")
    public ResponseEntity getAccounts(@RequestBody(required = false) PageRequest pageRequest) {
        return new ResponseEntity<>(getAccountsResource(pageRequest), HttpStatus.OK);
    }
}
