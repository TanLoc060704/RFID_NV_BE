package namviet.rfid_api.api;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.NhanVienDTO;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.NhanVienService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/private/nhan-vien")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class NhanVienApi {

    NhanVienService nhanVienService;



    @GetMapping
    public ResponseObject<List<NhanVienDTO>> findAll() {
        return nhanVienService.findAll();
    }


    @GetMapping("/{id}")
    public ResponseObject<NhanVienDTO> findById(@PathVariable Integer id) {
        return nhanVienService.findById(id);
    }


    @PostMapping
    public ResponseObject<NhanVienDTO> create(@RequestBody NhanVienDTO nhanVienDTO) {
        return nhanVienService.create(nhanVienDTO);
    }


    @PutMapping("/{id}")
    public ResponseObject<NhanVienDTO> update(@PathVariable Integer id, @RequestBody NhanVienDTO nhanVienDTO) {
        return nhanVienService.update(id, nhanVienDTO);
    }


    @ExceptionHandler(CustomException.class)
    public ResponseObject<?> handleCustomException(CustomException e) {
        return ResponseObject.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();
    }
}
