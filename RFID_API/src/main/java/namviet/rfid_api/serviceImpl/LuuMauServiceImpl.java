package namviet.rfid_api.serviceImpl;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.dto.LuuMauDTO;
import namviet.rfid_api.entity.LuuMau;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.mapper.LuuMauMapper;
import namviet.rfid_api.repository.LuuMauRepository;
import namviet.rfid_api.service.LuuMauService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LuuMauServiceImpl implements LuuMauService {
    LuuMauMapper luuMauMapper;
    LuuMauRepository luuMauRepository;

    @Override
    @Transactional
    public LuuMauDTO createLuuMau(LuuMauDTO luuMauDTO) {
        LuuMau luuMau = luuMauMapper.toEntity(luuMauDTO);
        LuuMau savedEntity = luuMauRepository.save(luuMau);
        return luuMauMapper.toDto(savedEntity);
    }

    @Override
    public List<LuuMauDTO> getAllLuuMau() {
        return luuMauRepository.findAll().stream()
                .map(luuMauMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LuuMauDTO> getLuuMauByLuMauId(Integer id) {
        return luuMauRepository.findById(id).map(luuMauMapper::toDto);
    }

    @Override
    public LuuMauDTO updateLuuMau(Integer id, LuuMauDTO luuMauDTO) {
        LuuMau luuMau = luuMauRepository.findById(id)
                .orElseThrow( () -> new CustomException("Luu Mau Not Found", HttpStatus.BAD_REQUEST));
        LuuMau updateEntity = luuMauMapper.toEntity(luuMauDTO);
        updateEntity.setLuuMauId(luuMau.getLuuMauId());
        return luuMauMapper.toDto(luuMauRepository.save(updateEntity));
    }

    @Override
    public void deleteLuuMau(Integer id) {
        if(!luuMauRepository.existsById(id)) {
            throw new CustomException("Luu mau not found", HttpStatus.BAD_REQUEST);
        }
        luuMauRepository.deleteById(id);
    }

    @Override
    public Page<LuuMauDTO> getLuuMauPagination(Pageable pageable) {
        return luuMauRepository.findAll(pageable).map(luuMauMapper::toDto);
    }

    @Override
    public Page<LuuMauDTO> searchWithFTSService(String searchText, Pageable pageable) {
        Page<LuuMau> luuMauPage = luuMauRepository.searchWithFTS(searchText, pageable);
        return luuMauPage.map(luuMauMapper::toDto);
    }
}
