package namviet.rfid_api.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.KhachHangDTO;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.KhachHangService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private/khach-hang")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class KhachHangAPI {

    KhachHangService khachHangService;


    @PostMapping
    public ResponseObject<KhachHangDTO> create(@RequestBody KhachHangDTO KhachHangDTO){
        try {
            return ResponseObject.<KhachHangDTO>builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Khach Hang created sussessfully")
                    .data(khachHangService.createKhachHang(KhachHangDTO))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error creating khach hang", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseObject<List<KhachHangDTO>> getAll() {
        try {
            return ResponseObject.<List<KhachHangDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all khach hang successfully")
                    .data(khachHangService.getAllKhachHang())
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching Khach Hang", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseObject<KhachHangDTO> getById(@PathVariable Integer id) {
        try {
            return ResponseObject.<KhachHangDTO>builder()
                    .status(HttpStatus.OK)
                    .message("Fetcged Khach Hang successfully")
                    .data(khachHangService.getKhachHangById(id)
                            .orElseThrow(() -> new CustomException("Khach hang found",HttpStatus.BAD_REQUEST)))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching Khach Hang",HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseObject<KhachHangDTO> update(@PathVariable Integer id, @RequestBody KhachHangDTO khachHangDTO) {
        try {
            return ResponseObject.<KhachHangDTO>builder()
                    .status(HttpStatus.OK)
                    .message("Updated khach hang successfully")
                    .data(khachHangService.updateKhahHang(id,khachHangDTO))
                    .build();
        } catch (CustomException a) {
            throw a;
        }catch (Exception e){
            throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
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
