package andream.eventManager.controllers;

import andream.eventManager.entities.Event;
import andream.eventManager.entities.User;
import andream.eventManager.exceptions.ValidationException;
import andream.eventManager.payloads.EventDTO;
import andream.eventManager.payloads.EventRespDTO;
import andream.eventManager.services.EventService;
import andream.eventManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    //serve a tutti gli utenti
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Page<Event> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(defaultValue = "id") String sortBy
    ) {
        return this.eventService.findAll(pageNumber, pageSize, sortBy);
    }

    //serve a gli organizer
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public EventRespDTO save(@RequestBody @Validated EventDTO payload, @AuthenticationPrincipal User currentUser,
                             BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList());
        } else {
            Event newEvent = this.eventService.save(payload, currentUser.getId());
            return new EventRespDTO(newEvent.getId());
        }
    }

    //put su evento sempre organizer se e proprietario
    @PutMapping("/{eventId}")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public Event updateEvent(@PathVariable UUID eventId,
                             @Validated @RequestBody EventDTO payload,
                             @AuthenticationPrincipal User currentUser
    ) {
        System.out.println(currentUser);
        return this.eventService.findByIdAndUpdate(eventId, payload, currentUser);
    }

    //delete su evento di organizer
    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findAndDelete(@PathVariable UUID eventId, @AuthenticationPrincipal User currentUser) {
        this.eventService.findByIdAndDelete(eventId, currentUser);
    }

    //prenotazione da parte di user
}