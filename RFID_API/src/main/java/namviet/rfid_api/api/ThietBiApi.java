package namviet.rfid_api.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.ThietBiDTO;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.ThietBiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private/thiet-bi")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThietBiApi {
    final ThietBiService thietBiService;

    @PostMapping
    public ResponseObject<ThietBiDTO> create(@RequestBody ThietBiDTO dto) {
        try {
            return ResponseObject.<ThietBiDTO>builder()
                    .status(HttpStatus.CREATED)
                    .message("ThietBi created successfully")
                    .data(thietBiService.createThietBi(dto))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error creating ThietBi", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseObject<List<ThietBiDTO>> getAll() {
      try {
            return ResponseObject.<List<ThietBiDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all ThietBi successfully")
                    .data(thietBiService.getAllThietBi())
                    .build();
      }  catch (CustomException a) {
          throw a;
      } catch (Exception e) {
            throw new CustomException("Error fetching ThietBi", HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    @GetMapping("/pagination")
    public ResponseObject<Page<ThietBiDTO>> getAllPagination(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("thietBiId").descending());
            return ResponseObject.<Page<ThietBiDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all ThietBi successfully")
                    .data(thietBiService.getThietBiPagination(pageable))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching ThietBi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseObject<Page<ThietBiDTO>> search(@RequestParam(defaultValue = "") String searchText,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            String ftsSearchText = "\"" + searchText + "*\"";
            return ResponseObject.<Page<ThietBiDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all ThietBi successfully")
                    .data(thietBiService.searchWithFTSService(ftsSearchText, pageable))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching ThietBi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseObject<ThietBiDTO> getById(@PathVariable Integer id) {
        try {
            return ResponseObject.<ThietBiDTO>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched ThietBi successfully")
                    .data(thietBiService.getThietBiById(id).orElseThrow(() -> new CustomException("Not found", HttpStatus.NOT_FOUND)))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching ThietBi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseObject<ThietBiDTO> update(@PathVariable Integer id, @RequestBody ThietBiDTO dto) {
        try {
            return ResponseObject.<ThietBiDTO>builder()
                    .status(HttpStatus.OK)
                    .message("Updated ThietBi successfully")
                    .data(thietBiService.updateThietBi(id, dto))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error updating ThietBi", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseObject<String> delete(@PathVariable Integer id) {
        try {
            thietBiService.deleteThietBi(id);
            return ResponseObject.<String>builder()
                    .status(HttpStatus.OK)
                    .message("Deleted ThietBi successfully")
                    .data("Deleted")
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error deleting ThietBi", HttpStatus.BAD_REQUEST);
        }
    }
}
