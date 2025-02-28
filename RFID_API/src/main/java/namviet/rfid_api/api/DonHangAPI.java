package namviet.rfid_api.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.DonHangDTO;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.DonHangService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/private/don-hang")
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

    @PostMapping()
    public ResponseObject<?> taoDonHang(@RequestBody DonHangDTO donHangDTO) {
        DonHangDTO donHang = donHangService.taoDonHang(donHangDTO);
        return ResponseObject.builder()
                .status(HttpStatus.CREATED)
                .message("Tạo Đơn Hàng Thành Công")
                .data(donHang)
                .build();
    }
    
    @GetMapping()
    public ResponseObject<List<DonHangDTO>> getAll() {
        try {
            return ResponseObject.<List<DonHangDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all DonHang successfully")
                    .data(donHangService.findAll())
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching DonHang", HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
    }

    @GetMapping("/pagination")
    public ResponseObject<Page<DonHangDTO>> getAllPagination(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "donHangId"));
            return ResponseObject.<Page<DonHangDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all DonHang successfully")
                    .data(donHangService.getDonHangPagination(pageable))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching DonHang", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseObject<Page<DonHangDTO>> searchWithFTS(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam("searchText") String searchText) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            String ftsSearchText = "\"" + searchText + "*\"";
            return ResponseObject.<Page<DonHangDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all DonHang successfully")
                    .data(donHangService.searchWithFTSService(ftsSearchText, pageable))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching DonHang", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseObject<DonHangDTO> getDonHangById(@PathVariable("id") Integer id) {
        try {
            return ResponseObject.<DonHangDTO>builder()
                    .status(HttpStatus.OK)
                    .message("Thông tin đơn hàng")
                    .data(donHangService.getDonHangById(id)
                        .orElseThrow(() -> new CustomException("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND)))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching DonHang", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseObject<DonHangDTO> updateDonHang(@PathVariable("id") Integer id, @RequestBody DonHangDTO donHangDTO) {
        try {
            return ResponseObject.<DonHangDTO>builder()
                    .status(HttpStatus.OK)
                    .message("Cập nhật đơn hàng thành công")
                    .data(donHangService.updateDonHang(id, donHangDTO))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error updating DonHang", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseObject<?> deleteDonHang(@PathVariable("id") Integer id) {
        try {
            donHangService.deleteDonHang(id);
            return ResponseObject.builder()
                    .status(HttpStatus.OK)
                    .message("Xóa đơn hàng thành công")
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error deleting DonHang", HttpStatus.INTERNAL_SERVER_ERROR);
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
