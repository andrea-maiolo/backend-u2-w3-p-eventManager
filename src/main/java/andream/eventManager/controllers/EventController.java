package andream.eventManager.controllers;

import andream.eventManager.entities.Event;
import andream.eventManager.entities.User;
import andream.eventManager.exceptions.BadRequestException;
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

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    //serve a tutti gli utenti
    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public Page<Event> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(defaultValue = "username") String sortBy
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
            throw new BadRequestException("da fare con errore guisto");
        } else {
            Event newEvent = this.eventService.save(payload, currentUser.getId());
            return new EventRespDTO(newEvent.getId());
        }
    }

    //put su evento sempre organizer
    //delete su evento di organizer
    //get by id sempre organizer
    //all the above solo se si e il creatore dell evento

    //prenotazione da parte di user
}