package namviet.rfid_api.serviceImpl;

import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.NhanVienDTO;
import namviet.rfid_api.entity.Account;
import namviet.rfid_api.entity.NhanVien;
import namviet.rfid_api.mapper.NhanVienMapper;
import namviet.rfid_api.repository.AccountRepository;
import namviet.rfid_api.repository.NhanVienRepository;
import namviet.rfid_api.repository.RoleRepository;
import namviet.rfid_api.service.NhanVienService;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NhanVienServiceImpl implements NhanVienService {

    private final NhanVienRepository nhanVienRepository;
    private final NhanVienMapper nhanVienMapper;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public ResponseObject<List<NhanVienDTO>> findAll() {
        List<NhanVien> entities = nhanVienRepository.findAll();
        List<NhanVienDTO> dtos = entities.stream()
                .map(nhanVienMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseObject<>(HttpStatus.OK, "Fetched all employees", dtos);
    }

    @Override
    public ResponseObject<NhanVienDTO> findById(Integer id) {
        NhanVien nhanVien = nhanVienRepository.findById(id)
                .orElseThrow(() -> new CustomException("Employee not found", HttpStatus.NOT_FOUND));
        return new ResponseObject<>(HttpStatus.OK, "Employee found", nhanVienMapper.toDto(nhanVien));
    }

    @Override
    @Transactional
    public ResponseObject<NhanVienDTO> create(NhanVienDTO nhanVienDTO) {
        Account account = new Account();
        if(accountRepository.existsByUserName(nhanVienDTO.getUsername())) {
            throw new CustomException("Username already exists", HttpStatus.BAD_REQUEST);
        }
        account.setUserName(nhanVienDTO.getUsername());
        account.setPassword(passwordEncoder.encode(nhanVienDTO.getPassword()));
        account.setRole(roleRepository.findByRoleId(nhanVienDTO.getRoleId()));
        account.setActive(nhanVienDTO.isActive());
        account.setEmail("email@gamil.com");
        account = accountRepository.save(account);

        NhanVien entity = nhanVienMapper.toEntity(nhanVienDTO);
        entity.setAccount(account); // Set the saved account to the NhanVien entity
        NhanVien savedEntity = nhanVienRepository.save(entity);
        return new ResponseObject<>(HttpStatus.CREATED, "Employee created successfully", nhanVienMapper.toDto(savedEntity));
    }

    @Override
    @Transactional
    public ResponseObject<NhanVienDTO> update(Integer id, NhanVienDTO nhanVienDTO) {
        NhanVien existingNhanVien = nhanVienRepository.findById(id)
                .orElseThrow(() -> new CustomException("Employee not found", HttpStatus.NOT_FOUND));

        Account account = existingNhanVien.getAccount();
        if (accountRepository.existsByUserName(nhanVienDTO.getUsername()) && !account.getUserName().equals(nhanVienDTO.getUsername())) {
            throw new CustomException("Username already exists", HttpStatus.BAD_REQUEST);
        }

        account.setUserName(nhanVienDTO.getUsername());
        account.setPassword(passwordEncoder.encode(nhanVienDTO.getPassword()));
        account.setRole(roleRepository.findByRoleId(nhanVienDTO.getRoleId()));
        account.setActive(nhanVienDTO.isActive());
        accountRepository.save(account);

        existingNhanVien.setHoTen(nhanVienDTO.getHoTen());
        existingNhanVien.setChucVu(nhanVienDTO.getChucVu());
        NhanVien updatedEntity = nhanVienRepository.save(existingNhanVien);

        return new ResponseObject<>(HttpStatus.OK, "Employee updated successfully", nhanVienMapper.toDto(updatedEntity));
    }

    @Override
    public ResponseObject<String> delete(Integer id) {
        NhanVien nhanVien = nhanVienRepository.findById(id)
                .orElseThrow(() -> new CustomException("Employee not found", HttpStatus.NOT_FOUND));
        nhanVienRepository.delete(nhanVien);
        return new ResponseObject<>(HttpStatus.OK, "Employee deleted successfully", null);
    }
}