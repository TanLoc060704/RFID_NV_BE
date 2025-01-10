package namviet.rfid_api.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.DonHangDTO;
import namviet.rfid_api.service.DonHangService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/private")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DonHangAPI {

    final DonHangService donHangService;

    @GetMapping("/tim-don-hang-theo-ma-lenh")
    public ResponseObject<?> timDonHangTheoMaLenh(@RequestParam("ma-lenh") String maLenh) {
        List<DonHangDTO> dsDonHang = donHangService.timDonHangTheoMaLenh(maLenh);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Thông tin đơn hàng")
                .data(dsDonHang)
                .build();
    }

    @PostMapping("/tao-don-hang")
    public ResponseObject<?> taoDonHang(@RequestBody DonHangDTO donHangDTO) {
        DonHangDTO donHang = donHangService.taoDonHang(donHangDTO);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Tạo Đơn Hàng Thành Công")
                .data(donHang)
                .build();
    }
}
