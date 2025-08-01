package andream.eventManager.controllers;

import andream.eventManager.entities.Event;
import andream.eventManager.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;


    @GetMapping
    public Page<Event> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(defaultValue = "username") String sortBy
    ) {
        return this.eventService.findAll(pageNumber, pageSize, sortBy);
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public EventRespDTO save(@RequestBody @Validated EventDTO payload, BindingResult validationResult) {
//        if (validationResult.hasErrors()) {
//            throw new BadRequestException("da fare con errore guisto");
//        } else {
//            Event newEvent = this.eventService.save(payload);
//            return new EventRespDTO(newEvent.getId());
//        }
//    }
}