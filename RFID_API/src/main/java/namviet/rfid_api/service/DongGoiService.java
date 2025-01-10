package namviet.rfid_api.service;

import namviet.rfid_api.dto.DongGoiDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DongGoiService {
    DongGoiDTO createDongGoi(DongGoiDTO dongGoiDTO);
    List<DongGoiDTO> getAllDongGoi();
    DongGoiDTO getDongGoiById(Integer id);
    DongGoiDTO updateDongGoi(Integer id, DongGoiDTO dongGoiDTO);
    void deleteDongGoi(Integer id);
}
