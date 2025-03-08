package namviet.rfid_api.service;

import namviet.rfid_api.dto.ThietBiDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ThietBiService {
    ThietBiDTO createThietBi(ThietBiDTO thietBiDTO);
    List<ThietBiDTO> getAllThietBi();
    Optional<ThietBiDTO> getThietBiById(Integer id);
    ThietBiDTO updateThietBi(Integer id, ThietBiDTO thietBiDTO);
    void deleteThietBi(Integer id);
    Page<ThietBiDTO> getThietBiPagination(Pageable pageable);
    Page<ThietBiDTO> searchWithFTSService(String searchText, Pageable pageable);
}
