package namviet.rfid_api.service;

import namviet.rfid_api.dto.UpcDTO;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UpcService {
    UpcDTO createUpc(UpcDTO upcDTO);
    List<UpcDTO> getAllUpc();
    Optional<UpcDTO> getUpcById(Integer id);
    UpcDTO updateUpc(Integer id, UpcDTO upcDTO);
    void deleteUpc(Integer id);
    Page<UpcDTO> getUpcPagination(Pageable pageable);
    Page<UpcDTO> searchUpcWithFTSService(String searchText, Pageable pageable);

    Resource template();
    void uploadFile(MultipartFile file);
}
