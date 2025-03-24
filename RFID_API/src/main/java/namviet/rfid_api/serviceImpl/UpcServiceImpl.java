package namviet.rfid_api.serviceImpl;

import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.UpcDTO;
import namviet.rfid_api.entity.Upc;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.mapper.UpcMapper;
import namviet.rfid_api.repository.UpcRepository;
import namviet.rfid_api.service.UpcService;
import namviet.rfid_api.utils.StringCellValue;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
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

    @Override
    public Page<UpcDTO> getUpcPagination(Pageable pageable) {
        return upcRepository.findAll(pageable).map(upcMapper::toDTO);
    }

    @Override
    public Page<UpcDTO> searchUpcWithFTSService(String searchText, Pageable pageable) {
        Page<Upc> entities = upcRepository.searchWithFTS(searchText, pageable);
        return entities.map(upcMapper::toDTO);
    }

@Override
public Resource template() {
    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        var sheet = workbook.createSheet("danh sahc Upc");
        var headerRow = sheet.createRow(0);
        String[] headers = {"STT", "UPC", "Serial"};
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        workbook.write(out);
        return new ByteArrayResource(out.toByteArray());
    } catch (Exception e) {
        e.printStackTrace();
        throw new CustomException("Error creating template", HttpStatus.BAD_REQUEST);
    }
}

    @Override
    public void uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new CustomException("Upload file Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            var sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                var row = sheet.getRow(i);
                Upc upc = new Upc();
                String upcValue = StringCellValue.getCellValueAsString(row.getCell(1));
                String serialValue = StringCellValue.getCellValueAsString(row.getCell(2));

                upc.setUpc(StringCellValue.getCellValueAsString(row.getCell(1))); // Giả sử độ dài tối đa cho 'upc' là 14
                upc.setSerial(Long.parseLong(StringCellValue.getCellValueAsString(row.getCell(2)))); // Giả sử độ dài tối đa cho 'serial' là 20

                upcRepository.save(upc);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Upload file Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String truncate(String value, int maxLength) {
        if (value.length() > maxLength) {
            return value.substring(0, maxLength);
        }
        return value;
    }
}
