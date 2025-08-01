package andream.eventManager.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @NotEmpty(message = "The email address is required.")
        @Email(message = "The entered email address is not in the correct format.")
        String email,
        @NotEmpty(message = "The password is required!")
        @Size(min = 4)
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{4,}$", message = "The password must be at least 4 characters long " +
                "and include at least one uppercase letter, " +
                "one lowercase letter, and one number")
        String password) {
}
