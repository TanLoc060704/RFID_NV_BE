package namviet.rfid_api.dto;

import lombok.Data;

@Data
public class DuLieuDTO {

    private Integer dataId;
    private String epc;
    private String sku;
    private String tid;
    private String dataGoc;
    private String noiDung;
    private Integer donHangId;

}

