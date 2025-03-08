package namviet.rfid_api.serviceImpl;

import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.ThietBiDTO;
import namviet.rfid_api.entity.ThietBi;
import namviet.rfid_api.mapper.ThietBiMapper;
import namviet.rfid_api.repository.ThietBiRepository;
import namviet.rfid_api.service.ThietBiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThietBiServiceImpl implements ThietBiService {
    private final ThietBiRepository thietBiRepository;
    private final ThietBiMapper thietBiMapper;

    @Override
    public ThietBiDTO createThietBi(ThietBiDTO thietBiDTO) {
        ThietBi thietBi = thietBiMapper.toEntity(thietBiDTO);
        thietBiRepository.save(thietBi);
        return thietBiMapper.toDto(thietBi);
    }

    @Override
    public List<ThietBiDTO> getAllThietBi() {
        return thietBiRepository.findAll().stream()
                .map(thietBiMapper::toDto).toList();
    }

    @Override
    public Optional<ThietBiDTO> getThietBiById(Integer id) {
        return thietBiRepository.findById(id).map(thietBiMapper::toDto);
    }

    @Override
    public ThietBiDTO updateThietBi(Integer id, ThietBiDTO thietBiDTO) {
        ThietBi thietBi = thietBiRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        thietBi.setTenMay(thietBiDTO.getTenMay());
        thietBi.setChucNang(thietBiDTO.getChucNang());
        thietBiRepository.save(thietBi);
        return thietBiMapper.toDto(thietBi);
    }

    @Override
    public void deleteThietBi(Integer id) {
        thietBiRepository.deleteById(id);
    }

    @Override
    public Page<ThietBiDTO> getThietBiPagination(Pageable pageable) {
        return thietBiRepository.findAll(pageable).map(thietBiMapper::toDto);
    }

    @Override
    public Page<ThietBiDTO> searchWithFTSService(String searchText, Pageable pageable) {
        return thietBiRepository.searchWithFTSService(searchText, pageable).map(thietBiMapper::toDto);
    }
}
