package namviet.rfid_api.serviceImpl;

import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.BanThanhPhamDTO;
import namviet.rfid_api.entity.BanThanhPham;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.mapper.BanThanhPhamMapper;
import namviet.rfid_api.repository.BanThanhPhamRepository;
import namviet.rfid_api.service.BanThanhPhamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BanThanhPhamServiceImpl implements BanThanhPhamService {

    private final BanThanhPhamRepository repository;
    private final BanThanhPhamMapper mapper;

    @Override
    public BanThanhPhamDTO createBanThanhPham(BanThanhPhamDTO banThanhPhamDTO) {
        BanThanhPham entity = mapper.toEntity(banThanhPhamDTO);
        BanThanhPham savedEntity = repository.save(entity);
        return mapper.toDTO(savedEntity);
    }

    @Override
    public List<BanThanhPhamDTO> getAllBanThanhPham() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BanThanhPhamDTO> getBanThanhPhamById(Integer id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    public BanThanhPhamDTO updateBanThanhPham(Integer id, BanThanhPhamDTO banThanhPhamDTO) {
        BanThanhPham existing = repository.findById(id)
                .orElseThrow(() -> new CustomException("BanThanhPham not found", HttpStatus.NOT_FOUND));
        BanThanhPham updatedEntity = mapper.toEntity(banThanhPhamDTO);
        updatedEntity.setBanThanhPhamId(existing.getBanThanhPhamId());
        System.out.println(updatedEntity.getNvl().getNvlId());
        return mapper.toDTO(repository.save(updatedEntity));
    }

    @Override
    public void deleteBanThanhPham(Integer id) {
        if (!repository.existsById(id)) {
            throw new CustomException("BanThanhPham not found", HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

    @Override
    public Page<BanThanhPhamDTO> getBanThanhPhamPagination(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDTO);
    }

    @Override
    public Page<BanThanhPhamDTO> searchWithFTSService(String searchText, Pageable pageable) {
        Page<BanThanhPham> entities = repository.searchWithFTS(searchText,pageable);
        return entities.map(mapper::toDTO);
    }
}
