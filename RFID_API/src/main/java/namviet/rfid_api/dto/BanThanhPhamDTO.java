package namviet.rfid_api.dto;

import lombok.Data;

@Data
public class BanThanhPhamDTO {
    private Integer banThanhPhamId;
    private String code;
    private Integer soPcsTot;
    private Integer soPcsHu;
    private Integer nvlId;
}
