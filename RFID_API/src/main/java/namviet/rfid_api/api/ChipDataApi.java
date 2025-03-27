package namviet.rfid_api.api;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.ChipDataDTO;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.ChipDataService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/private/chip-data")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ChipDataApi {
    final ChipDataService chipDataService;

    @PostMapping
    public ResponseObject<ChipDataDTO> create(@RequestBody ChipDataDTO dto) {
        try {
            return ResponseObject.<ChipDataDTO>builder()
                    .status(HttpStatus.CREATED)
                    .message("ChipData created successfully")
                    .data(chipDataService.createChipData(dto))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error creating ChipData", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseObject<?> getAll() {
        try {
            return ResponseObject.builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all ChipData successfully")
                    .data(chipDataService.getAllChipData())
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching ChipData", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseObject<?> getById(@PathVariable Integer id) {
        try {
            return ResponseObject.builder()
                    .status(HttpStatus.OK)
                    .message("Fetched ChipData successfully")
                    .data(chipDataService.getChipDataById(id))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching ChipData", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseObject<?> update(@PathVariable Integer id, @RequestBody ChipDataDTO dto) {
        try {
            return ResponseObject.builder()
                    .status(HttpStatus.OK)
                    .message("Updated ChipData successfully")
                    .data(chipDataService.updateChipData(id, dto))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error updating ChipData", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseObject<?> delete(@PathVariable Integer id) {
        try {
            chipDataService.deleteChipData(id);
            return ResponseObject.builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Deleted ChipData successfully")
                    .data(null)
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error deleting ChipData", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseObject<?> search(@RequestParam String chipId) {
        try {
            return ResponseObject.builder()
                    .status(HttpStatus.OK)
                    .message("Fetched ChipData successfully")
                    .data(chipDataService.findBytidHeader(chipId))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching ChipData", HttpStatus.INTERNAL_SERVER_ERROR);
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
