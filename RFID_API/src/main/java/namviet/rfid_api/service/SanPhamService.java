package namviet.rfid_api.service;

import namviet.rfid_api.dto.SanPhamDTO;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface SanPhamService {
    SanPhamDTO createSanPham(SanPhamDTO sanPhamDTO);

    Optional<List<SanPhamDTO>> timTatCaSanPham();

    Optional<List<SanPhamDTO>> timSanPhamTheoSKU(String SKU);

    Optional<SanPhamDTO> capNhatSanPham(SanPhamDTO sanPhamDTO);

    Optional<List<SanPhamDTO>> themSKUHoanLoat(List<SanPhamDTO> dsSanPhamDTO);

    Resource template();

    void uploadFile(MultipartFile file);

    Page <SanPhamDTO> getSanPhamPagination(Pageable pageable);
    Page<SanPhamDTO> searchSPWithFTSService(String searchText, Pageable pageable);
}
