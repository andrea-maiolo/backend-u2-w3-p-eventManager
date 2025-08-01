package andream.eventManager.services;

import andream.eventManager.entities.Event;
import andream.eventManager.entities.User;
import andream.eventManager.exceptions.NotFoundException;
import andream.eventManager.exceptions.UnAuthorizedException;
import andream.eventManager.payloads.EventDTO;
import andream.eventManager.repositories.EventRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventService {
    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private UserService userService;

    public Event save(EventDTO payload, UUID organizerId) {
        User found = userService.findById(organizerId);
        Event newEvent = new Event(payload.title(), payload.description(), payload.location(),
                payload.date(), payload.availableSeats(), found);
        this.eventRepo.save(newEvent);
        return newEvent;
    }

    public Page<Event> findAll(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        return this.eventRepo.findAll(pageable);
    }

    public Event findById(UUID eventId) {
        return this.eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException("event not found"));
    }


    public Event findByIdAndUpdate(UUID eventId, EventDTO payload, User currentUser) {
        Event found = this.findById(eventId);

        if (!found.getOrganizer().getId().equals(currentUser.getId())) {
            throw new UnAuthorizedException("you cannot modify events that you have not created");
        }

        found.setTitle(payload.title());
        found.setDate(payload.date());
        found.setLocation(payload.location());
        found.setDescription(payload.description());
        found.setAvailableSeats(payload.availableSeats());
        this.eventRepo.save(found);
        return found;
    }

    public void findByIdAndDelete(UUID eventId, User currentUser) {
        Event found = this.findById(eventId);

        if (!found.getOrganizer().getId().equals(currentUser.getId())) {
            throw new UnAuthorizedException("you cannot delete events that you have not created");
        }
        this.eventRepo.delete(found);
    }


    @Transactional
    public void bookEvent(UUID userId, UUID eventId) {
        User found = this.userService.findById(userId);
        Event foundEvent = this.eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException("event not found"));

        if (foundEvent.getAvailableSeats() <= 0) {
            throw new IllegalStateException("No available seats for this event");
        }

        if (found.getBookedEvents().contains(foundEvent)) {
            throw new IllegalStateException("You have already booked this event");
        }

        found.getBookedEvents().add(foundEvent);
        foundEvent.getPartecipants().add(found);
        foundEvent.setAvailableSeats(foundEvent.getAvailableSeats() - 1);

        this.eventRepo.save(foundEvent);
        this.userService.modifyBookedEventList(found);
    }

}
