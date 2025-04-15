package namviet.rfid_api.api;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.UpcDTO;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.UpcService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/pagination")
    public ResponseObject<?> getPagination(@RequestParam(defaultValue = "0") Integer page,
                                           @RequestParam(defaultValue = "10") Integer size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"upcId"));
            return ResponseObject.builder()
                    .status(HttpStatus.OK)
                    .message("Fetched Upc pagination successfully")
                    .data(upcService.getUpcPagination(pageable))
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            throw new CustomException("Error fetching Upc", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    public ResponseObject<Page<UpcDTO>> searchWithFTS(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam("searchText") String searchText) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            String ftsSearchText = "\"" + searchText + "*\"";
            Page<UpcDTO> result = (searchText == null || searchText.trim().isEmpty())
                    ? upcService.getUpcPagination(pageable)
                    : upcService.searchUpcWithFTSService(ftsSearchText, pageable);

            return ResponseObject.<Page<UpcDTO>>builder()
                    .status(HttpStatus.OK)
                    .message("Fetched all Upc successfully")
                    .data(result)
                    .build();
        } catch (CustomException a) {
            throw a;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error fetching Upc", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload-file-nhieu-upc")
    public ResponseObject<?> uploadFile(@RequestParam("file") MultipartFile file) {
        upcService.uploadFile(file);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("File uploaded successfully")
                .build();
    }

//    @GetMapping("/template")
//    public ResponseObject<?> template() {
//        try {
//            return ResponseObject.builder()
//                    .status(HttpStatus.OK)
//                    .message("Template created successfully")
//                    .data(upcService.template())
//                    .build();
//        } catch (CustomException a) {
//            throw a;
//        } catch (Exception e) {
//            throw new CustomException("Error creating template", HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping("/template")
    public ResponseEntity<Resource> template() {
        try {
            // Gọi service để tạo file Excel
            Resource resource = upcService.template();

            // Thiết lập header để tải file
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=template_upc.xlsx") // Tên file tải về
                    .contentType(MediaType.APPLICATION_OCTET_STREAM) // Loại file
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error creating template", HttpStatus.BAD_REQUEST);
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
