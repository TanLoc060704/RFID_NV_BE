package namviet.rfid_api.service;

import namviet.rfid_api.dto.DongGoiDTO;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface DongGoiService {
    @Transactional
    DongGoiDTO createDongGoi(DongGoiDTO dongGoiDTO);

    List<DongGoiDTO> getAllDongGoi();

    DongGoiDTO getDongGoiById(Integer id);

    @Transactional
    DongGoiDTO updateDongGoi(Integer id, DongGoiDTO dongGoiDTO);

    @Transactional
    void deleteDongGoi(Integer id);

    Page<DongGoiDTO> getDongGoiPagination(Pageable pageable);

    Page<DongGoiDTO> searchWithFTSService(String searchText, Pageable pageable);

    List<DongGoiDTO> findDongGoiByMaLenh(String MaLenh);

    Resource exportPackingList(int soCuonTrongThung,int soPcsTrenCuon, String maLenh);

    void createAllDongGoi(String maLenh,int soPcsTrenCuon);
}
