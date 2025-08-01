package andream.eventManager.services;

import andream.eventManager.entities.User;
import andream.eventManager.enums.Role;
import andream.eventManager.exceptions.BadRequestException;
import andream.eventManager.exceptions.NotFoundException;
import andream.eventManager.payloads.NewUserDTO;
import andream.eventManager.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder bcrypt;

    public User save(NewUserDTO payload) {
        this.userRepo.findByEmail(payload.email()).ifPresent(user -> {
            throw new BadRequestException("email already in use");
        });

        User newUser = new User(payload.email(), bcrypt.encode(payload.password()), payload.username(), Role.USER);
        this.userRepo.save(newUser);
        return newUser;
    }

    public Page<User> findAll(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        return this.userRepo.findAll(pageable);
    }

    public User findById(UUID userId) {
        return this.userRepo.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
    }


    public User findByIdAndUpdate(UUID userId, NewUserDTO payload) {
        User found = this.findById(userId);
        if (!found.getEmail().equals(payload.email()))
            this.userRepo.findByEmail(payload.email()).ifPresent(user -> {
                throw new BadRequestException("email already in use");
            });
        found.setUsername(payload.username());
        found.setEmail(payload.email());
        this.userRepo.save(found);
        return found;
    }

    public void findByIdAndDelete(UUID userId) {
        User found = this.findById(userId);
        this.userRepo.delete(found);
    }

    public User findByEmail(String email) {
        return this.userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("user not found"));
    }
}