package namviet.rfid_api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DonHangDTO {

    Integer donHangId;
    String maLenh;
    String po;
    Integer total;
    String loaiData;
    String username;
    Integer nhanVienId;
    Integer khachHangId;
    String tenNhanVien;
    String tenKhachHang;
    Date ngayLap;
    Date ngayCapNhat;
}
