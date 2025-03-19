package namviet.rfid_api.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.SanPhamDTO;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.SanPhamService;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/private/san-pham")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SanPhamAPI {

    final SanPhamService sanPhamService;

    @GetMapping
    public ResponseObject<?> timTatCaSanPham() {
        Optional<List<SanPhamDTO>> dsSanPham = sanPhamService.timTatCaSanPham();
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Thông tin tất cả sản phẩm")
                .data(dsSanPham)
                .build();
    }

    @GetMapping("/tim-theo-sku")
    public ResponseObject<?> timSanPhamTheoSKU(@RequestParam("sku") String sku) {
        Optional<List<SanPhamDTO>> dsSanPham = sanPhamService.timSanPhamTheoSKU(sku);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Thông tin sản phẩm theo sku")
                .data(dsSanPham)
                .build();
    }

    @PutMapping
    public ResponseObject<?> capNhatSanPham(@RequestBody SanPhamDTO sanPhamDTO) {
       Optional<SanPhamDTO> sanPham = sanPhamService.capNhatSanPham(sanPhamDTO);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Cập nhật san pham thành công")
                .data(sanPham)
                .build();
    }

    @PostMapping
    public ResponseObject<?> themSanPham(@RequestBody SanPhamDTO sanPhamDTO) {
        SanPhamDTO sanPham = sanPhamService.createSanPham(sanPhamDTO);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("thêm sản phẩm thành công")
                .data(sanPham)
                .build();
    }

    @PostMapping("/them-ds-san-pham")
    public ResponseObject<?> themListSanPham(@RequestBody List<SanPhamDTO> dsSanPhamDTO) {
        Optional<List<SanPhamDTO>> themDsSanPham = sanPhamService.themSKUHoanLoat(dsSanPhamDTO);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("thêm danh sách sản phẩm thành công")
                .data(themDsSanPham)
                .build();
    }

    @GetMapping("/template")
    public ResponseEntity<Resource> exprotTemplate() {
        try {
            Resource resource = sanPhamService.template();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=template.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            throw new CustomException("Error exporting template", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload-file-san-pham")
    public ResponseObject<?> uploadFile(@RequestParam("file") MultipartFile file) {
        sanPhamService.uploadFile(file);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("File uploaded successfully")
                .build();
    }

    @GetMapping("/pagination")
    public ResponseObject<?> getSanPhamPagination(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sku"));
            Page<SanPhamDTO> sanPhamDTOS = sanPhamService.getSanPhamPagination(pageable);
            return ResponseObject.builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all san pham successfully")
                    .data(sanPhamDTOS)
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching san pham", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseObject<Page<SanPhamDTO>> searchSPWithFTSService(@RequestParam("searchText") String searchText,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            String ftsSearch = "\"" + searchText + "*\"";
            Page<SanPhamDTO> result = (searchText == null || searchText.trim().isEmpty())
                    ? sanPhamService.getSanPhamPagination(pageable)
                    : sanPhamService.searchSPWithFTSService(ftsSearch, pageable);
            return ResponseObject.<Page<SanPhamDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all san pham successfully")
                    .data(result)
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error fetching san pham", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(CustomException.class)
    public ResponseObject<?> handleCustomException(CustomException e) {
        return ResponseObject.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();
    }

}
