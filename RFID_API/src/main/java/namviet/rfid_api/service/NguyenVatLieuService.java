package namviet.rfid_api.service;

import namviet.rfid_api.dto.NguyenVatLieuDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface NguyenVatLieuService {

    NguyenVatLieuDTO createNguyenVatLieu(NguyenVatLieuDTO nguyenVatLieuDTO);

    List<NguyenVatLieuDTO> getAllNguyenVatLieu();

    Optional<NguyenVatLieuDTO> getNguyenVatLieuById(Integer nvlId);

    NguyenVatLieuDTO updateNguyenVatLieu(Integer nvlId, NguyenVatLieuDTO nguyenVatLieuDTO);

    void deleteNguyenVatLieu(Integer nvlId);

    Page<NguyenVatLieuDTO> getNguyenVatLieuPagination(Pageable pageable);
}
