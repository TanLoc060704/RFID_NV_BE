package namviet.rfid_api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BanThanhPhamDTO {
    private Integer banThanhPhamId;
    private String code;
    private Integer soPcsTot;
    private Integer soPcsHu;
    private Integer nvlId;
    private String tenNhanVien;
    private int accountId;
    private String maNvl;
    private Date ngayNhap;
}
