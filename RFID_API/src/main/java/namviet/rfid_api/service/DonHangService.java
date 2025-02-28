package namviet.rfid_api.service;

import namviet.rfid_api.dto.DonHangDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DonHangService {

    List<DonHangDTO> findAll();

    @Transactional
    DonHangDTO taoDonHang(DonHangDTO donHangDTO);

    List<DonHangDTO> timDonHangTheoMaLenh(String maLenh);

    Optional<DonHangDTO> getDonHangById(Integer id);

    @Transactional
    DonHangDTO updateDonHang(Integer id, DonHangDTO donHangDTO);

    @Transactional
    void deleteDonHang(Integer id);

    Page<DonHangDTO> getDonHangPagination(Pageable pageable);

    Page<DonHangDTO> searchWithFTSService(String searchText, Pageable pageable);
}
