package andream.eventManager.entities;

import andream.eventManager.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String email;
    @Setter(AccessLevel.NONE)
    private String password;
    private String username;
    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.NONE)
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


    public User(String email, String password, String username, Role role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }
}
