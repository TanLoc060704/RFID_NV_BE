package namviet.rfid_api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThietBiDTO {
    Integer thietBiId;
    String tenMay;
    String chucNang;
}
