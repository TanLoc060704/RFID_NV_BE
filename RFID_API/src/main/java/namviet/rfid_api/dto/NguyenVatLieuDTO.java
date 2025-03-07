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
public class NguyenVatLieuDTO {

    Integer nvlId;
    String code;
    String lot;
    Integer soPcsTot;
    Integer soPcsHu;
    Date ngayNhap;
    String tenNhanVien;
//    int nhanVienId;
    int accountId;
}
