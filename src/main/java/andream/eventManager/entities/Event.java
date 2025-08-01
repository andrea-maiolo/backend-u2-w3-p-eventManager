package andream.eventManager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
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
    @JsonIgnore
    private User organizer;

    @ManyToMany(mappedBy = "bookedEvents")
    @JsonIgnore
    private Set<User> partecipants = new HashSet<>();

    public Event(String title, String description, String location, LocalDate date, int availableSeats, User organizer) {
        this.availableSeats = availableSeats;
        this.description = description;
        this.title = title;
        this.date = date;
        this.location = location;
        this.organizer = organizer;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", availableSeats=" + availableSeats +
                '}';
    }
}
