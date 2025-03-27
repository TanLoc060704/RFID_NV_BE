package namviet.rfid_api.service;


import namviet.rfid_api.dto.KhachHangDTO;
import namviet.rfid_api.dto.LuuMauDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LuuMauService {

    LuuMauDTO createLuuMau( LuuMauDTO luuMauDTO);

    List<LuuMauDTO> getAllLuuMau();

    Optional<LuuMauDTO> getLuuMauByLuMauId(Integer id);

    LuuMauDTO updateLuuMau(Integer id, LuuMauDTO luuMauDTO);

    void deleteLuuMau(Integer id);

    Page<LuuMauDTO> getLuuMauPagination(Pageable pageable);
    Page<LuuMauDTO> searchWithFTSService(String searchText, Pageable pageable);
}
