package sanchez.sergio.controller;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sanchez.sergio.domain.event.AccountEvent;
import sanchez.sergio.service.EventService;

/**
 *
 * @author sergio
 */
@RestController
@RequestMapping("/v1")
public class EventController {

    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(path = "/events")
    public ResponseEntity handleEvent(@RequestBody AccountEvent event) {
        return Optional.ofNullable(eventService.apply(event))
                .map(e -> new ResponseEntity<>(e, HttpStatus.CREATED))
                .orElseThrow(() -> new RuntimeException("Apply event failed"));
    }
}
