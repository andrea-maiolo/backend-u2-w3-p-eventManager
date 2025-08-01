package andream.eventManager.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
    private List<String> messagesLog;

    public ValidationException(List<String> messages) {
        super("Error in validation");
        this.messagesLog = messages;
    }
}
