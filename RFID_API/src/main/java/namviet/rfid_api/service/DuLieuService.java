package namviet.rfid_api.service;

import namviet.rfid_api.entity.SanPham;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DuLieuService {

    boolean taoFileData (int donHangSanPhamID);
    SanPham decoderEpc(String epc);

}
