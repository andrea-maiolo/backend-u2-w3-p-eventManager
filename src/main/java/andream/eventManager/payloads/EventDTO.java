package andream.eventManager.payloads;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EventDTO(
        @NotEmpty(message = "title is required")
        @Size(min = 3, max = 20, message = "title must be between 3 and 20 characters long")
        String title,
        @NotEmpty(message = "description required")
        @Size(min = 3, max = 20, message = "description must be between 3 and 20 characters long")
        String description,
        @NotEmpty(message = "location is requireed")
        @Size(min = 3, max = 20, message = "location must be between 3 and 20 characters long")
        String location,
        @FutureOrPresent(message = "The start date must be today or later")
        LocalDate date,
        @NotNull
        @Min(value = 5, message = "less than 5 is not an event")
        @Positive
        int availableSeats
) {
}
