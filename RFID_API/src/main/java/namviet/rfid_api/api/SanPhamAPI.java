package namviet.rfid_api.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.SanPhamDTO;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.SanPhamService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/private")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SanPhamAPI {

    final SanPhamService sanPhamService;

    @GetMapping("/tim-tat-ca-san-pham")
    public ResponseObject<?> timTatCaSanPham() {
        Optional<List<SanPhamDTO>> dsSanPham = sanPhamService.timTatCaSanPham();
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Thông tin tất cả sản phẩm")
                .data(dsSanPham)
                .build();
    }

    @GetMapping("/tim-san-pham-theo-sku")
    public ResponseObject<?> timSanPhamTheoSKU(@RequestParam("sku") String sku) {
        Optional<List<SanPhamDTO>> dsSanPham = sanPhamService.timSanPhamTheoSKU(sku);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Thông tin sản phẩm theo sku")
                .data(dsSanPham)
                .build();
    }

    @PostMapping("/cap-nhat-hoac-tao-san-pham")
    public ResponseObject<?> capNhatSanPham(@RequestBody SanPhamDTO sanPhamDTO) {
       Optional<SanPhamDTO> sanPham = sanPhamService.capNhatSanPham(sanPhamDTO);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Cập nhật san pham thành công")
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

    @ExceptionHandler(CustomException.class)
    public ResponseObject<?> handleCustomException(CustomException e) {
        return ResponseObject.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();
    }

}
