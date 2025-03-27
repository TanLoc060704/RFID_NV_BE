package namviet.rfid_api.service;

import namviet.rfid_api.dto.ChipDataDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ChipDataService {
    ChipDataDTO createChipData(ChipDataDTO chipDataDTO);
    Optional<ChipDataDTO>  getChipDataById(Integer id);
    ChipDataDTO updateChipData(Integer id, ChipDataDTO chipDataDTO);
    void deleteChipData(Integer id);
    List<ChipDataDTO> getAllChipData();
    List<ChipDataDTO> findBytidHeader(String chipId);
}
