package namviet.rfid_api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SanPhamDTO {

    String sku;

    Integer partition;
    Integer filter;
//    Long serial;
//    String upc;
//    int upcId;
    UpcDTO upcDTO;
    String head;
    String content;

    String kichThuoc;
    String inlay;
    String nccInlay;
}
