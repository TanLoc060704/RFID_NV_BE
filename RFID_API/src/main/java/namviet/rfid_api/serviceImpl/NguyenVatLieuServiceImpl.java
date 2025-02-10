package namviet.rfid_api.serviceImpl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.NguyenVatLieuDTO;
import namviet.rfid_api.entity.Account;
import namviet.rfid_api.entity.NguyenVatLieu;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.mapper.NguyenVatLieuMapper;
import namviet.rfid_api.repository.NguyenVatLieuRepository;
import namviet.rfid_api.repository.UserRepository;
import namviet.rfid_api.service.NguyenVatLieuService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class NguyenVatLieuServiceImpl implements NguyenVatLieuService {

    private final NguyenVatLieuRepository nguyenVatLieuRepository;
    private final NguyenVatLieuMapper nguyenVatLieuMapper;
    private final UserRepository userRepository;
    @Override
    public NguyenVatLieuDTO createNguyenVatLieu(NguyenVatLieuDTO nguyenVatLieuDTO) {
        // Kiểm tra validation và thêm dữ liệu mới
        NguyenVatLieu nguyenVatLieu = nguyenVatLieuMapper.toEntity(nguyenVatLieuDTO);
        nguyenVatLieu = nguyenVatLieuRepository.save(nguyenVatLieu);
        return nguyenVatLieuMapper.toDTO(nguyenVatLieu);
    }

    @Override
    public List<NguyenVatLieuDTO> getAllNguyenVatLieu() {
        List<NguyenVatLieu> nguyenVatLieus = nguyenVatLieuRepository.findAll();
        return nguyenVatLieus.stream()
                .map(nguyenVatLieuMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<NguyenVatLieuDTO> getNguyenVatLieuById( Integer nvlId) {
        Optional<NguyenVatLieu> nguyenVatLieuOptional = nguyenVatLieuRepository.findById(nvlId);
        if (nguyenVatLieuOptional.isPresent()) {
            return Optional.of(nguyenVatLieuMapper.toDTO(nguyenVatLieuOptional.get()));
        } else {
            throw new CustomException("NguyenVatLieu not found with id " + nvlId, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public NguyenVatLieuDTO updateNguyenVatLieu( Integer nvlId, NguyenVatLieuDTO nguyenVatLieuDTO) {
        if (!nguyenVatLieuRepository.existsById(nvlId)) {
            throw new CustomException("NguyenVatLieu not found with id " + nvlId, HttpStatus.NOT_FOUND);
        }
        NguyenVatLieu nguyenVatLieu = nguyenVatLieuMapper.toEntity(nguyenVatLieuDTO);
        nguyenVatLieu.setNvlId(nvlId);
//        Account account = userRepository.findByAccountId(nguyenVatLieuDTO.getAccountId());
//        if(account == null){
//            throw new CustomException("Account not found with id " + nvlId, HttpStatus.NOT_FOUND);
//        }
//        nguyenVatLieu.setAccount(account);
        nguyenVatLieu = nguyenVatLieuRepository.save(nguyenVatLieu);
        return nguyenVatLieuMapper.toDTO(nguyenVatLieu);
    }

    @Override
    public void deleteNguyenVatLieu(Integer nvlId) {
        if (!nguyenVatLieuRepository.existsById(nvlId)) {
            throw new CustomException("NguyenVatLieu not found with id " + nvlId, HttpStatus.NOT_FOUND);
        }
        nguyenVatLieuRepository.deleteById(nvlId);
    }
}
