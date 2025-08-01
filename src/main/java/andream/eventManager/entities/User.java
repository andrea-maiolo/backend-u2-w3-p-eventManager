package andream.eventManager.entities;

import andream.eventManager.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String email;
    private String password;
    private String username;
    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.NONE)
    private Role role;

    @OneToMany(mappedBy = "organizer")
    @JsonIgnore
    private List<Event> createdEvents = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "reservations",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @JsonIgnore
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}
