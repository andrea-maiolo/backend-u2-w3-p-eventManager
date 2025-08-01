package andream.eventManager.controllers;

import andream.eventManager.entities.User;
import andream.eventManager.payloads.NewUserDTO;
import andream.eventManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping("/me")
    public User updateOwnProfile(@AuthenticationPrincipal User currentAuthenticatedUser,
                                 @RequestBody @Validated NewUserDTO payload) {
        return this.userService.findByIdAndUpdate(currentAuthenticatedUser.getId(), payload);
    }

    @DeleteMapping("/me")
    public void deleteOwnProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        this.userService.findByIdAndDelete(currentAuthenticatedUser.getId());
    }

}