package namviet.rfid_api.service;

import namviet.rfid_api.dto.FileResourceDTO;
import namviet.rfid_api.entity.SanPham;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DuLieuService {

    boolean taoFileData (int donHangSanPhamID);
    SanPham decoderEpc(String epc);
    List<FileResourceDTO> downloadFile(int donHangSanPhamID);
    FileResourceDTO downloadFileByDonHangSanPhamId(int donHangSanPhamID);

    List<FileResourceDTO> downloadListFileSerialByDonHangSanPhamId(int donHangID);
    FileResourceDTO downloadFileSerialByDonHangSanPhamId(int donHangSanPhamID);

    List<FileResourceDTO> downloadFileListHexByDonHangSanPhamId(int donHangSanPhamID);
    FileResourceDTO downloadFileHexByDonHangSanPhamId(int donHangSanPhamID);
}
