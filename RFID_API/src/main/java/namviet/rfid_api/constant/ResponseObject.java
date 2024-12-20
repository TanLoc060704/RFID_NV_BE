package namviet.rfid_api.constant;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseObject<T> {
    private HttpStatus status;
    private String message;
    private T data;
}
