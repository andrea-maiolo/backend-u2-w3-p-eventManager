package andream.eventManager.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Event {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String title;
    private String description;
    private String location;
    private LocalDate date;
    private int availableSeats;

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @ManyToMany(mappedBy = "bookedEvents")
    private Set<User> partecipants = new HashSet<>();

    public Event(String title, String description, String location, LocalDate date, int availableSeats) {
        this.availableSeats = availableSeats;
        this.description = description;
        this.title = title;
        this.date = date;
        this.location = location;
    }
}
