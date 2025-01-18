package namviet.rfid_api.api;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.LuuMauDTO;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.LuuMauService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private/luu-mau")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class LuuMauApi {

    LuuMauService luuMauService;

    @PostMapping
    public ResponseObject<LuuMauDTO> create(@RequestBody LuuMauDTO luuMauDTO){
        try {
            return ResponseObject.<LuuMauDTO>builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Luu mau created sussessfully")
                    .data(luuMauService.createLuuMau(luuMauDTO))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error creating khach hang", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseObject<List<LuuMauDTO>> getAll() {
        try {
            return ResponseObject.<List<LuuMauDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all luu mau successfully")
                    .data(luuMauService.getAllLuuMau())
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching luu mau", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseObject<LuuMauDTO> getById(@PathVariable Integer id) {
        try {
            return ResponseObject.<LuuMauDTO>builder()
                    .status(HttpStatus.OK)
                    .message("Fetcged Khach Hang successfully")
                    .data(luuMauService.getLuuMauByLuMauId(id)
                            .orElseThrow(() -> new CustomException("Luu Mau found",HttpStatus.BAD_REQUEST)))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching Luu Mau",HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseObject<LuuMauDTO> update(@PathVariable Integer id, @RequestBody LuuMauDTO luuMauDTO) {
        try {
            return ResponseObject.<LuuMauDTO>builder()
                    .status(HttpStatus.OK)
                    .message("Updated Luu Mau successfully")
                    .data(luuMauService.updateLuuMau(id,luuMauDTO))
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
