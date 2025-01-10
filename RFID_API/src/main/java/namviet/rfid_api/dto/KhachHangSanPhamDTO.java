package namviet.rfid_api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KhachHangSanPhamDTO {

    Integer khachHangId;
    String sku;
}
