package namviet.rfid_api.serviceImpl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.dto.KhachHangDTO;
import namviet.rfid_api.entity.KhachHang;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.mapper.KhachHangMapper;
import namviet.rfid_api.repository.KhachHangRepository;
import namviet.rfid_api.service.KhachHangService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class KhachHangServiceImpl implements KhachHangService {

    KhachHangRepository repository;
    KhachHangMapper mapper;

    @Override
    @Transactional
    public KhachHangDTO createKhachHang(KhachHangDTO khachHangDTO) {
        KhachHang entity = mapper.toEntity(khachHangDTO);
        KhachHang savedEntity = repository.save(entity);
        return mapper.toDTO(savedEntity);
    }

    @Override
    public List<KhachHangDTO> getAllKhachHang() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<KhachHangDTO> getKhachHangById(Integer id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    @Transactional
    public KhachHangDTO updateKhahHang(Integer id, KhachHangDTO khachHangDTO) {
        KhachHang khachHang = repository.findById(id)
                .orElseThrow( () -> new CustomException("Khach Hang not found", HttpStatus.BAD_REQUEST));
        KhachHang updateEntity = mapper.toEntity(khachHangDTO);
        updateEntity.setKhachHangId(khachHang.getKhachHangId());
        return mapper.toDTO(repository.save(updateEntity));
    }

    @Override
    @Transactional
    public void deleteKhachHang(Integer id) {
        if(!repository.existsById(id)) {
            throw new CustomException("Khach Hang not found", HttpStatus.BAD_REQUEST);
        }
        repository.deleteById(id);
    }
}
