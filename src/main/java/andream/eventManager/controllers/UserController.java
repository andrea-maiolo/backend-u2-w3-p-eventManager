package andream.eventManager.controllers;

import andream.eventManager.entities.User;
import andream.eventManager.exceptions.BadRequestException;
import andream.eventManager.payloads.NewUserDTO;
import andream.eventManager.payloads.UserRespDTO;
import andream.eventManager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping
    public Page<User> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                              @RequestParam(defaultValue = "10") int pageSize,
                              @RequestParam(defaultValue = "username") String sortBy
    ) {
        return this.userService.findAll(pageNumber, pageSize, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserRespDTO save(@RequestBody @Validated NewUserDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException("da fare con errore guisto");
        } else {
            User newU = this.userService.save(payload);
            return new UserRespDTO(newU.getId());
        }
    }
}
