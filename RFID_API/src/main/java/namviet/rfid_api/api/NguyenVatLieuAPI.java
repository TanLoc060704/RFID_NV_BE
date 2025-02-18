package namviet.rfid_api.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.dto.NguyenVatLieuDTO;
import namviet.rfid_api.service.NguyenVatLieuService;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NguyenVatLieuAPI {

    private final NguyenVatLieuService nguyenVatLieuService;

    @PostMapping("/nguyen-vat-lieu")
    @Transactional
    public ResponseObject<NguyenVatLieuDTO> createNguyenVatLieu(@RequestBody NguyenVatLieuDTO nguyenVatLieuDTO) {
        try {
            NguyenVatLieuDTO createdNguyenVatLieu = nguyenVatLieuService.createNguyenVatLieu(nguyenVatLieuDTO);
            return ResponseObject.<NguyenVatLieuDTO>builder()
                    .status(HttpStatus.CREATED)
                    .message("NguyenVatLieu created successfully")
                    .data(createdNguyenVatLieu)
                    .build();
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
            throw new CustomException("Error creating NguyenVatLieu", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/nguyen-vat-lieu")
    public ResponseObject<List<NguyenVatLieuDTO>> getAllNguyenVatLieu() {
        try {
            List<NguyenVatLieuDTO> nguyenVatLieuList = nguyenVatLieuService.getAllNguyenVatLieu();
            return ResponseObject.<List<NguyenVatLieuDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Successfully fetched all NguyenVatLieu")
                    .data(nguyenVatLieuList)
                    .build();
        } catch (Exception e) {
            throw new CustomException("Error fetching NguyenVatLieu", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/nguyen-vat-lieu/{id}")
    public ResponseObject<NguyenVatLieuDTO> getNguyenVatLieuById(@PathVariable Integer id) {
        try {
            NguyenVatLieuDTO nguyenVatLieuDTO = nguyenVatLieuService.getNguyenVatLieuById(id)
                    .orElseThrow(() -> new CustomException("NguyenVatLieu not found", HttpStatus.NOT_FOUND));
            return ResponseObject.<NguyenVatLieuDTO>builder()
                    .status(HttpStatus.OK)
                    .message("Successfully fetched NguyenVatLieu")
                    .data(nguyenVatLieuDTO)
                    .build();
        } catch (Exception e) {
            throw new CustomException("Error fetching NguyenVatLieu", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Cập nhật NguyenVatLieu
    @PutMapping("/nguyen-vat-lieu/{id}")
    public ResponseObject<NguyenVatLieuDTO> updateNguyenVatLieu(@PathVariable Integer id,
                                                                @RequestBody NguyenVatLieuDTO nguyenVatLieuDTO) {
        try {
            NguyenVatLieuDTO updatedNguyenVatLieu = nguyenVatLieuService.updateNguyenVatLieu(id, nguyenVatLieuDTO);
            return ResponseObject.<NguyenVatLieuDTO>builder()
                    .status(HttpStatus.OK)
                    .message("NguyenVatLieu updated successfully")
                    .data(updatedNguyenVatLieu)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error updating NguyenVatLieu", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/nguyen-vat-lieu/pagination")
    public ResponseObject<Page<NguyenVatLieuDTO>> getAllNvlPagination (@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "nvlId"));
            return ResponseObject.<Page<NguyenVatLieuDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all NguyenVatLieu successfully")
                    .data(nguyenVatLieuService.getNguyenVatLieuPagination(pageable))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error fetching NguyenVatLieu", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Xóa NguyenVatLieu
    @DeleteMapping("/nguyen-vat-lieu/{id}")
    @Transactional
    public ResponseObject<Void> deleteNguyenVatLieu(@PathVariable Integer id) {
        try {
            nguyenVatLieuService.deleteNguyenVatLieu(id);
            return ResponseObject.<Void>builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("NguyenVatLieu deleted successfully")
                    .data(null)
                    .build();
        } catch (Exception e) {
            throw new CustomException("Error deleting NguyenVatLieu", HttpStatus.INTERNAL_SERVER_ERROR);
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
