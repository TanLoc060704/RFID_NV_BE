package namviet.rfid_api.service;

import namviet.rfid_api.dto.DonHangSanPhamDTO;

import java.util.List;
import java.util.Optional;


public interface DonHangSanPhamService {

    List<DonHangSanPhamDTO> dSSanPhamTheoDonHang (String maLenh);
    List<DonHangSanPhamDTO> dSTatCaSanPham();
    DonHangSanPhamDTO capNhatDonHangSanPham(DonHangSanPhamDTO odHangSanPhamDTO);
    List<DonHangSanPhamDTO> themSPVaoDonHangNew(List<DonHangSanPhamDTO> DonHangSanPhamDTO);
    Optional<DonHangSanPhamDTO> timDonHangSanPham(int donHangSanPhamId);
    Optional<List<DonHangSanPhamDTO>> timDonHangTheoDonHangId(int donHangId);

}
