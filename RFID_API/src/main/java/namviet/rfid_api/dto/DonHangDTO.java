package namviet.rfid_api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DonHangDTO {

    Integer donHangId;
    String maLenh;
    String po;
    Integer total;
    String loaiData;

    Integer nhanVienId;
    Integer khachHangId;
}
