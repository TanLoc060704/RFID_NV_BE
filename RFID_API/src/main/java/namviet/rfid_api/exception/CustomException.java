package namviet.rfid_api.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;
@Getter
public class CustomException extends RuntimeException{
    private HttpStatus status = HttpStatus.MULTI_STATUS;
    private String message;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
