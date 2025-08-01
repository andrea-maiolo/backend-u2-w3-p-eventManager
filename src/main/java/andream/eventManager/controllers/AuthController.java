package andream.eventManager.controllers;

import andream.eventManager.entities.User;
import andream.eventManager.exceptions.ValidationException;
import andream.eventManager.payloads.LoginDTO;
import andream.eventManager.payloads.LoginRespDTO;
import andream.eventManager.payloads.NewUserDTO;
import andream.eventManager.payloads.UserRespDTO;
import andream.eventManager.services.AuthService;
import andream.eventManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody @Validated LoginDTO payload) {
        String token = authService.checkAndCreateToken(payload);
        return new LoginRespDTO(token);
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRespDTO save(@RequestBody @Validated NewUserDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getFieldErrors().forEach(fieldError -> System.out.println(fieldError.getDefaultMessage()));
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList());
        } else {
            User newUser = this.userService.save(payload);
            return new UserRespDTO(newUser.getId());
        }

    }

}
