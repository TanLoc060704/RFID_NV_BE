package namviet.rfid_api.service;

import namviet.rfid_api.dto.BanThanhPhamDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BanThanhPhamService {
    BanThanhPhamDTO createBanThanhPham(BanThanhPhamDTO banThanhPhamDTO);
    List<BanThanhPhamDTO> getAllBanThanhPham();
    Optional<BanThanhPhamDTO> getBanThanhPhamById(Integer id);
    BanThanhPhamDTO updateBanThanhPham(Integer id, BanThanhPhamDTO banThanhPhamDTO);
    void deleteBanThanhPham(Integer id);
    Page<BanThanhPhamDTO> getBanThanhPhamPagination(Pageable pageable);
    Page<BanThanhPhamDTO> searchWithFTSService(String searchText, Pageable pageable);
}
