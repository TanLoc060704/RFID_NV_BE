package namviet.rfid_api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DongGoiDTO {
    Integer dongGoiId;
    Integer soPcsTot;
    Integer soPcsHu;
    Integer donHangSanPhamId;
    Integer nhanVienId;
    Integer thietBiId;
    Integer banThanhPhamId;
    String tenNhanVien;
    String tenThietBi;
    String codeBanThanhPham;
    String maLenh;
    String sku;
    String code;
    Date ngayLap;
    int indexCuon;
}
