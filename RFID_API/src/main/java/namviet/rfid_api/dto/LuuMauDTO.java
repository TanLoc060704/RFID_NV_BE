package namviet.rfid_api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LuuMauDTO {

    Integer luuMauId;
    String code;
    String vitri;
    String nhaCungCap;
    String tenSanPham;
    String thanhDangPham;
    Float chieuDaiNhan;
    Float chieuRongNhan;
    Float chieuDaiChip;
    Float chieuRongChip;
    String chatLieu;
    String maGiay;
    String maInlay;
    String chip;
    String maKeo;
    String thietDo;
    String dinhLuong;
    String ghiChu;
    Integer soLuong;
    Date ngayTao;
    Integer nhanVienId;
}
