package namviet.rfid_api.service;

import namviet.rfid_api.dto.KhachHangDTO;

import java.util.List;
import java.util.Optional;

public interface KhachHangService {
    KhachHangDTO createKhachHang( KhachHangDTO khachHangDTO);

    List<KhachHangDTO> getAllKhachHang();
    Optional<KhachHangDTO> getKhachHangById(Integer id);
    KhachHangDTO updateKhahHang(Integer id, KhachHangDTO khachHangDTO);
    void deleteKhachHang(Integer id);
}
