package namviet.rfid_api.serviceImpl;

import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.DongGoiDTO;
import namviet.rfid_api.entity.DongGoi;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.mapper.DongGoiMapper;
import namviet.rfid_api.repository.DongGoiRepository;
import namviet.rfid_api.service.DongGoiService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DongGoiServiceImpl implements DongGoiService {
    private final DongGoiRepository dongGoiRepository;
    private final DongGoiMapper dongGoiMapper;

    @Override
    public DongGoiDTO createDongGoi(DongGoiDTO dongGoiDTO) {
        DongGoi dongGoi = dongGoiMapper.toEntity(dongGoiDTO);
        return dongGoiMapper.toDTO(dongGoiRepository.save(dongGoi));
    }

    @Override
    public List<DongGoiDTO> getAllDongGoi() {
        return dongGoiRepository.findAll()
                .stream()
                .map(dongGoiMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DongGoiDTO getDongGoiById(Integer id) {
        DongGoi dongGoi = dongGoiRepository.findById(id)
                .orElseThrow(() -> new CustomException("DongGoi not found", HttpStatus.NOT_FOUND));
        return dongGoiMapper.toDTO(dongGoi);
    }

    @Override
    public DongGoiDTO updateDongGoi(Integer id, DongGoiDTO dongGoiDTO) {
        DongGoi existingDongGoi = dongGoiRepository.findById(id)
                .orElseThrow(() -> new CustomException("DongGoi not found", HttpStatus.NOT_FOUND));
        DongGoi updatedDongGoi = dongGoiMapper.toEntity(dongGoiDTO);
        updatedDongGoi.setDongGoiId(existingDongGoi.getDongGoiId());
        return dongGoiMapper.toDTO(dongGoiRepository.save(updatedDongGoi));
    }

    @Override
    public void deleteDongGoi(Integer id) {
        if (!dongGoiRepository.existsById(id)) {
            throw new CustomException("DongGoi not found", HttpStatus.NOT_FOUND);
        }
        dongGoiRepository.deleteById(id);
    }
}
