package namviet.rfid_api.serviceImpl;

import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.SanPhamDTO;
import namviet.rfid_api.entity.SanPham;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.mapper.SanPhamMapper;
import namviet.rfid_api.repository.SanPhamRepository;
import namviet.rfid_api.service.SanPhamService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamServiceImpl implements SanPhamService {

    final SanPhamRepository sanPhamRepository;
    final SanPhamMapper sanPhamMapper;

    @Override
    public SanPhamDTO createSanPham(SanPhamDTO sanPhamDTO) {
        SanPham sanPham = sanPhamMapper.toEntity(sanPhamDTO);
        SanPham sanPhamSaved = sanPhamRepository.save(sanPham);
        return sanPhamMapper.toDto(sanPhamSaved);
    }

    @Override
    public Optional<List<SanPhamDTO>> timTatCaSanPham() {
        List<SanPham> DsSanPham = sanPhamRepository.findAll();

        if(DsSanPham.isEmpty()){
            return Optional.empty();
        }

        List<SanPhamDTO> dsSanPhamDTO = DsSanPham.stream()
                .map(sanPhamMapper::toDto)
                .collect(Collectors.toList());

        return Optional.of(dsSanPhamDTO);
    }

    @Override
    public Optional<List<SanPhamDTO>> timSanPhamTheoSKU(String sku) {
        List<SanPham> dsSanPham = sanPhamRepository.findBySkuContaining(sku);

        if(dsSanPham.isEmpty()){
            return Optional.empty();
        }

        List<SanPhamDTO> dsSanPhamDTO = dsSanPham.stream()
                .map(sanPhamMapper::toDto)
                .collect(Collectors.toList());

        return Optional.of(dsSanPhamDTO);
    }

    @Override
    @Transactional
    public Optional<SanPhamDTO> capNhatSanPham(SanPhamDTO sanPhamDTO) {
        if(sanPhamDTO == null){
            return Optional.empty();
        }
        SanPham sanPham =  sanPhamRepository.save(sanPhamMapper.toEntity(sanPhamDTO));
        return Optional.of(sanPhamMapper.toDto(sanPham));
    }

    @Override
    @Transactional
    public Optional<List<SanPhamDTO>> themSKUHoanLoat(List<SanPhamDTO> dsSanPhamDTO) {
        if (dsSanPhamDTO == null || dsSanPhamDTO.isEmpty()) {
            return Optional.empty();
        }

        List<SanPham> dsSanPhamEntities = dsSanPhamDTO.stream()
                .map(sanPhamMapper::toEntity)
                .collect(Collectors.toList());

        List<SanPham> existingSanPhamList = sanPhamRepository.findBySkuIn(
                dsSanPhamEntities.stream()
                        .map(SanPham::getSku)
                        .collect(Collectors.toList())
        );

        if (!existingSanPhamList.isEmpty()) {
            String errorMessage = "Các sản phẩm với SKU đã tồn tại trong hệ thống: "
                    + existingSanPhamList.stream()
                    .map(SanPham::getSku)
                    .collect(Collectors.joining(", "));
            throw new CustomException(errorMessage, HttpStatus.BAD_REQUEST);
        }

        List<SanPham> dsSanPhamSaved = sanPhamRepository.saveAll(dsSanPhamEntities);

        List<SanPhamDTO> dsSanPhamDTOSaved = dsSanPhamSaved.stream()
                .map(sanPhamMapper::toDto)
                .collect(Collectors.toList());

        return Optional.of(dsSanPhamDTOSaved);
    }

    @Override
    public Resource template() {
        try(Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()){
            var sheet = workbook.createSheet("danh sach sku");
            var headerRow = sheet.createRow(0);
            String[] headers = {"STT","SKU", "Masp", "UPC", "Kh", "Head","Partition","Filter","Kich thuoc","Ma Inlay","NCC","Content"};
            for(int i = 0; i < headers.length; i++){
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            workbook.write(out);
            return new ByteArrayResource(out.toByteArray());
        }catch (Exception e){
            e.printStackTrace();
            throw new CustomException("Error generating Excel file", HttpStatus.INTERNAL_SERVER_ERROR);
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
                SanPham sanPham = new SanPham();

                sanPhamRepository.save(sanPham);
            }
        } catch (Exception e) {
            throw new CustomException("Upload file Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
