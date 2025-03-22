package namviet.rfid_api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DonHangSanPhamDTO {

    Integer donHangSanPhamId;
    SanPhamDTO sanPham;
    DonHangDTO donHang;
    String  tenFile;
    Integer soLuong;
    Integer soLanTao;
}
