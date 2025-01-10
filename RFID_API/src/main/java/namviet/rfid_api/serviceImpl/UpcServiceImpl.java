package namviet.rfid_api.serviceImpl;

import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.UpcDTO;
import namviet.rfid_api.entity.Upc;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.mapper.UpcMapper;
import namviet.rfid_api.repository.UpcRepository;
import namviet.rfid_api.service.UpcService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpcServiceImpl implements UpcService {
    private final UpcRepository upcRepository;
    private final UpcMapper upcMapper;

    @Override
    @Transactional
    public UpcDTO createUpc(UpcDTO upcDTO) {
        Upc entity = upcMapper.toEntity(upcDTO);
        Upc savedEntity = upcRepository.save(entity);
        return upcMapper.toDTO(savedEntity);
    }

    @Override
    public List<UpcDTO> getAllUpc() {
        return upcRepository.findAll().stream()
                .map(upcMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UpcDTO> getUpcById(Integer id) {
        return upcRepository.findById(id).map(upcMapper::toDTO);
    }

    @Override
    @Transactional
    public UpcDTO updateUpc(Integer id, UpcDTO upcDTO) {
        Upc existing = upcRepository.findById(id)
                .orElseThrow(() -> new CustomException("Upc not found",HttpStatus.BAD_REQUEST));
        Upc updateEntity = upcMapper.toEntity(upcDTO);
        if(existing.getSerial() > updateEntity.getSerial()){
            throw new CustomException("Serial must be greater than current Serial", HttpStatus.BAD_REQUEST);
        }
        updateEntity.setUpcId(existing.getUpcId());

        return upcMapper.toDTO(upcRepository.save(updateEntity));
    }

    @Override
    @Transactional
    public void deleteUpc(Integer id) {
        if(!upcRepository.existsById(id)) {
            throw new CustomException("Upc not found", HttpStatus.BAD_REQUEST);
        }
        upcRepository.deleteById(id);
    }
}
