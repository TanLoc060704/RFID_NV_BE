package namviet.rfid_api.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.BanThanhPhamDTO;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.BanThanhPhamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private/ban-thanh-pham")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BanThanhPhamApi {

    final BanThanhPhamService banThanhPhamService;

    @PostMapping
    public ResponseObject<BanThanhPhamDTO> create(@RequestBody BanThanhPhamDTO dto) {
        try {
            return ResponseObject.<BanThanhPhamDTO>builder()
                    .status(HttpStatus.CREATED)
                    .message("BanThanhPham created successfully")
                    .data(banThanhPhamService.createBanThanhPham(dto))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error creating BanThanhPham", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseObject<List<BanThanhPhamDTO>> getAll() {
        try {
            return ResponseObject.<List<BanThanhPhamDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all BanThanhPham successfully")
                    .data(banThanhPhamService.getAllBanThanhPham())
                    .build();
        } catch (CustomException a) {
          throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching BanThanhPham", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pagination")
    public ResponseObject<Page<BanThanhPhamDTO>> getAllPagination(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        try {

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "banThanhPhamId"));

            return ResponseObject.<Page<BanThanhPhamDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all BanThanhPham successfully")
                    .data(banThanhPhamService.getBanThanhPhamPagination(pageable))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching BanThanhPham", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseObject<Page<BanThanhPhamDTO>> searchWithFTS(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size,
                                                               @RequestParam("searchText") String searchText) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            String ftsSearchText = "\"" + searchText + "*\"";
            Page<BanThanhPhamDTO> result = (searchText == null || searchText.trim().isEmpty())
                    ? banThanhPhamService.getBanThanhPhamPagination(pageable)
                    : banThanhPhamService.searchWithFTSService(ftsSearchText, pageable);
             return ResponseObject.<Page<BanThanhPhamDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all BanThanhPham successfully")
                    .data(result)
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error fetching BanThanhPham", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseObject<BanThanhPhamDTO> getById(@PathVariable Integer id) {
        try {
            return ResponseObject.<BanThanhPhamDTO>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched BanThanhPham successfully")
                    .data(banThanhPhamService.getBanThanhPhamById(id)
                            .orElseThrow(() -> new CustomException("BanThanhPham not found", HttpStatus.NOT_FOUND)))
                    .build();
        } catch (CustomException a) {
          throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching BanThanhPham", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseObject<BanThanhPhamDTO> update(@PathVariable Integer id, @RequestBody BanThanhPhamDTO dto) {
        try {
            return ResponseObject.<BanThanhPhamDTO>builder()
                    .status(HttpStatus.OK)
                    .message("Updated BanThanhPham successfully")
                    .data(banThanhPhamService.updateBanThanhPham(id, dto))
                    .build();
        }catch (CustomException a) {
            throw a;
        }
        catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> delete(@PathVariable Integer id) {
        try {
            banThanhPhamService.deleteBanThanhPham(id);
            return ResponseObject.<Void>builder()
                    .status(HttpStatus.NO_CONTENT)
                    .message("Deleted BanThanhPham successfully")
                    .data(null)
                    .build();
        } catch (CustomException a) {
          throw a;
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
