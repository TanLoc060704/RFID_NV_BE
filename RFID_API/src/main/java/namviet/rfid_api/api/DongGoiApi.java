package namviet.rfid_api.api;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.DongGoiDTO;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.DongGoiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private/dong-goi")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DongGoiApi {

    final DongGoiService dongGoiService;

    @PostMapping
    public ResponseObject<DongGoiDTO> create(@RequestBody DongGoiDTO dongGoiDTO) {
        try {
            return ResponseObject.<DongGoiDTO>builder()
                    .status(HttpStatus.CREATED)
                    .message("DongGoi created successfully")
                    .data(dongGoiService.createDongGoi(dongGoiDTO))
                    .build();
        } catch (Exception e) {
            throw new CustomException("Error creating DongGoi", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseObject<List<DongGoiDTO>> getAll() {
        try {
            return ResponseObject.<List<DongGoiDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all DongGoi successfully")
                    .data(dongGoiService.getAllDongGoi())
                    .build();
        } catch (Exception e) {
            throw new CustomException("Error fetching DongGoi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseObject<DongGoiDTO> getById(@PathVariable Integer id) {
        try {
            return ResponseObject.<DongGoiDTO>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched DongGoi successfully")
                    .data(dongGoiService.getDongGoiById(id))
                    .build();
        } catch (Exception e) {
            throw new CustomException("Error fetching DongGoi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseObject<DongGoiDTO> update(@PathVariable Integer id, @RequestBody DongGoiDTO dongGoiDTO) {
        try {
            return ResponseObject.<DongGoiDTO>builder()
                    .status(HttpStatus.OK)
                    .message("Updated DongGoi successfully")
                    .data(dongGoiService.updateDongGoi(id, dongGoiDTO))
                    .build();
        } catch (Exception e) {
            throw new CustomException("Error updating DongGoi", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/pagination")
    ResponseObject<Page<DongGoiDTO>> getAllPagination(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dongGoiId"));
            return ResponseObject.<Page<DongGoiDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all DongGoi successfully")
                    .data(dongGoiService.getDongGoiPagination(pageable))
                    .build();
        } catch (Exception e) {
            throw new CustomException("Error fetching DongGoi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseObject<Page<DongGoiDTO>> searchWithFTS(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam("searchText") String searchText) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            String ftsSearchText = "\"" + searchText + "*\"";
            return ResponseObject.<Page<DongGoiDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all DongGoi successfully")
                    .data(dongGoiService.searchWithFTSService(ftsSearchText, pageable))
                    .build();
        } catch (Exception e) {
            throw new CustomException("Error fetching DongGoi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/find-by-ma-lenh")
    public ResponseObject<List<DongGoiDTO>> findDongGoiByMaLenh (@RequestParam("MaLenh") String MaLenh) {
        try {
            return ResponseObject.<List<DongGoiDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched DongGoi by MaLenh successfully")
                    .data(dongGoiService.findDongGoiByMaLenh(MaLenh))
                    .build();
        } catch (Exception e) {
            throw new CustomException("Error fetching DongGoi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> delete(@PathVariable Integer id) {
        try {
            dongGoiService.deleteDongGoi(id);
            return ResponseObject.<Void>builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Deleted DongGoi successfully")
                    .data(null)
                    .build();
        } catch (Exception e) {
            throw new CustomException("Error deleting DongGoi", HttpStatus.INTERNAL_SERVER_ERROR);
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
