package namviet.rfid_api.api;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.UpcDTO;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.UpcService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private/upc")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UpcApi {
    UpcService upcService;

    @PostMapping
    public ResponseObject<UpcDTO> create(@RequestBody UpcDTO upcDTO){
        try {
            return ResponseObject.<UpcDTO>builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Upc created sussessfully")
                    .data(upcService.createUpc(upcDTO))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error creating Upc", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseObject<List<UpcDTO>> getAll() {
        try {
            return ResponseObject.<List<UpcDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all Upc successfully")
                    .data(upcService.getAllUpc())
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching Upc", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseObject<UpcDTO> getById(@PathVariable Integer id) {
        try {
            return ResponseObject.<UpcDTO>builder()
                    .status(HttpStatus.OK)
                    .message("Fetcged UPC successfully")
                    .data(upcService.getUpcById(id)
                            .orElseThrow(() -> new CustomException("Upc not found",HttpStatus.BAD_REQUEST)))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching UPC",HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseObject<UpcDTO> update(@PathVariable Integer id, @RequestBody UpcDTO upc) {
        try {
            return ResponseObject.<UpcDTO>builder()
                    .status(HttpStatus.OK)
                    .message("Updated Upc successfully")
                    .data(upcService.updateUpc(id,upc))
                    .build();
        } catch (CustomException a) {
            throw a;
        }catch (Exception e){
            throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
/**
    @DeleteMapping("/{id}")
    public ResponseObject<Void> delete(@PathVariable Integer id) {
        try {
            upcService.deleteUpc(id);
            return ResponseObject.<Void>builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Deleted Upc successfully")
                    .data(null)
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
**/

    @ExceptionHandler(CustomException.class)
    public ResponseObject<?> handleCustomException(CustomException e) {
        return ResponseObject.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();
    }
}