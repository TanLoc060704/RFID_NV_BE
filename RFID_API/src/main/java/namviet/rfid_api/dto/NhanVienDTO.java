package namviet.rfid_api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NhanVienDTO {

    Integer nhanVienId;
    String hoTen;
    String chucVu;
    Integer accountId;
    String username;
    String password;
    int roleId;
    boolean active;
    String roleName;
}
