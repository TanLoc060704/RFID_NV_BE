package namviet.rfid_api.serviceImpl;

import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.ChipDataDTO;
import namviet.rfid_api.entity.ChipData;
import namviet.rfid_api.mapper.ChipDataMapper;
import namviet.rfid_api.repository.ChipDataRepository;
import namviet.rfid_api.service.ChipDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ChipDataServiceImpl implements ChipDataService {
    private final ChipDataRepository chipDataRepository;
    private final ChipDataMapper chipDataMapper;

    @Override
    @Transactional
    public ChipDataDTO createChipData(ChipDataDTO chipDataDTO) {
        ChipData entity = chipDataMapper.toEntity(chipDataDTO);
        ChipData savedEntity = chipDataRepository.save(entity);
        return chipDataMapper.toDTO(savedEntity);
    }

    @Override
    public Optional<ChipDataDTO> getChipDataById(Integer id) {
        return chipDataRepository.findById(id).map(chipDataMapper::toDTO);
    }

    @Override
    public ChipDataDTO updateChipData(Integer id, ChipDataDTO chipDataDTO) {
        ChipData existing = chipDataRepository.findById(id).orElseThrow(() -> new RuntimeException("ChipData not found"));
        ChipData updatedEntity = chipDataMapper.toEntity(chipDataDTO);
        updatedEntity.setChipId(existing.getChipId());
        return chipDataMapper.toDTO(chipDataRepository.save(updatedEntity));
    }

    @Override
    public void deleteChipData(Integer id) {
        if (!chipDataRepository.existsById(id)) {
            throw new RuntimeException("ChipData not found");
        }
        chipDataRepository.deleteById(id);
    }

    @Override
    public List<ChipDataDTO> getAllChipData() {
        return chipDataRepository.findAll().stream()
                .map(chipDataMapper::toDTO)
                .toList();
    }

    @Override
    public List<ChipDataDTO> findBytidHeader(String chipId) {
        return chipDataRepository.findByTidHeaderContaining(chipId).stream()
                .map(chipDataMapper::toDTO)
                .toList();
    }
}
