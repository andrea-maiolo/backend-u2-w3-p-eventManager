package andream.eventManager.entities;

import andream.eventManager.enums.Role;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    private String email;
    private String password;
    private String username;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "organizer")
    private List<Event> createdEvents = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "reservations",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<Event> bookedEvents = new HashSet<>();

}
