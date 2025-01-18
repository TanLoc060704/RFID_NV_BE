package namviet.rfid_api.service;

import namviet.rfid_api.dto.DonHangSanPhamDTO;
import namviet.rfid_api.entity.SanPham;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;


public interface DonHangSanPhamService {

    List<DonHangSanPhamDTO> dSSanPhamTheoDonHang (String maLenh);
    List<DonHangSanPhamDTO> dSTatCaSanPham();
    DonHangSanPhamDTO capNhatDonHangSanPham(DonHangSanPhamDTO odHangSanPhamDTO);
    List<DonHangSanPhamDTO> themSPVaoDonHangNew(List<DonHangSanPhamDTO> DonHangSanPhamDTO);
    Optional<DonHangSanPhamDTO> timDonHangSanPham(int donHangSanPhamId);
    Optional<List<DonHangSanPhamDTO>> timDonHangTheoDonHangId(int donHangId);
    List<DonHangSanPhamDTO> importFile (List<MultipartFile> dsFileImport, String maLenh, String sku, int viTriEPC);


}
