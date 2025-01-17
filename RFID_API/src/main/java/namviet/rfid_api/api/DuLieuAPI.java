package namviet.rfid_api.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.DonHangDTO;
import namviet.rfid_api.dto.DonHangSanPhamDTO;
import namviet.rfid_api.dto.DuLieuDTO;
import namviet.rfid_api.entity.SanPham;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.DuLieuService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DuLieuAPI {

    final DuLieuService duLieuService;

    @PostMapping("/tao-file-du-lieu")
    public ResponseObject<?> taoFileDuLieu(@RequestParam("donHangSanPhamID") int donHangSanPhamID) {
        boolean duLieuDTOS = duLieuService.taoFileData(donHangSanPhamID);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(duLieuDTOS)
                .build();
    }

    @PostMapping("/decoder-epc")
    public ResponseObject<?> decoderEPC(@RequestParam("epc") String epc) {
        SanPham sanPham = duLieuService.decoderEpc(epc);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(sanPham)
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
