package namviet.rfid_api.service;

import namviet.rfid_api.dto.SanPhamDTO;

import java.util.List;
import java.util.Optional;

public interface SanPhamService {
    Optional<List<SanPhamDTO>> timTatCaSanPham();

    Optional<List<SanPhamDTO>> timSanPhamTheoSKU(String SKU);

    Optional<SanPhamDTO> capNhatSanPham(SanPhamDTO sanPhamDTO);

    Optional<List<SanPhamDTO>> themSKUHoanLoat(List<SanPhamDTO> dsSanPhamDTO);
}
