package andream.eventManager.payloads;

import java.util.UUID;

public record ReservationDTO(UUID eventId, UUID userId) {
}
