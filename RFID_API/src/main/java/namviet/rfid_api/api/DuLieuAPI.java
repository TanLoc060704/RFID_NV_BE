package namviet.rfid_api.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.DonHangDTO;
import namviet.rfid_api.dto.DonHangSanPhamDTO;
import namviet.rfid_api.dto.DuLieuDTO;
import namviet.rfid_api.dto.FileResourceDTO;
import namviet.rfid_api.entity.SanPham;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.DuLieuService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DuLieuAPI {

    final DuLieuService duLieuService;

    @PostMapping("/tao-file-du-lieu")
    public ResponseObject<?> taoFileDuLieu(@RequestParam("donHangSanPhamID") int donHangSanPhamID) {
        boolean duLieuDTOS = duLieuService.taoFileData(donHangSanPhamID);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(duLieuDTOS)
                .build();
    }

    @PostMapping("/decoder-epc")
    public ResponseObject<?> decoderEPC(@RequestParam("epc") String epc) {
        SanPham sanPham = duLieuService.decoderEpc(epc);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(sanPham)
                .build();
    }

    @GetMapping("/download-file")
    public ResponseObject<List<FileResourceDTO>> downloadFile(@RequestParam("donHangSanPhamID") int donHangSanPhamID) {
        List<FileResourceDTO> resources = duLieuService.downloadFile(donHangSanPhamID);
        return ResponseObject.<List<FileResourceDTO>>builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(resources)
                .build();
    }

    @GetMapping("/download-file-by-don-hang-san-pham-id")
    public ResponseObject<FileResourceDTO> downloadFileByDonHangSanPhamId(@RequestParam("donHangSanPhamID") int donHangSanPhamID) {
        FileResourceDTO resource = duLieuService.downloadFileByDonHangSanPhamId(donHangSanPhamID);
        return ResponseObject.<FileResourceDTO>builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(resource)
                .build();
    }

    @GetMapping("/download-list-file-serial-by-don-hang-san-pham-id")
    public ResponseObject<List<FileResourceDTO>> downloadFileListSerialByDonHangSanPhamId(@RequestParam("donHangId") int donHangID) {
        List<FileResourceDTO> resources = duLieuService.downloadListFileSerialByDonHangSanPhamId(donHangID);
        return ResponseObject.<List<FileResourceDTO>>builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(resources)
                .build();
    }

    @GetMapping("/download-file-serial-by-don-hang-san-pham-id")
    public ResponseObject<FileResourceDTO> downloadFileSerialByDonHangSanPhamId(@RequestParam("donHangSanPhamID") int donHangSanPhamID) {
        FileResourceDTO resource = duLieuService.downloadFileSerialByDonHangSanPhamId(donHangSanPhamID);
        return ResponseObject.<FileResourceDTO>builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(resource)
                .build();
    }


    @GetMapping("/download-list-file-hex-by-don-hang-san-pham-id")
    public ResponseObject<List<FileResourceDTO>> downloadListFileHexByDonHangSanPhamId(@RequestParam("donHangSanPhamID") int donHangSanPhamID) {
        List<FileResourceDTO> resources = duLieuService.downloadFileListHexByDonHangSanPhamId(donHangSanPhamID);
        return ResponseObject.<List<FileResourceDTO>>builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(resources)
                .build();
    }

    @GetMapping("/download-file-hex-by-don-hang-san-pham-id")
    public ResponseObject<FileResourceDTO> downloadFileHexByDonHangSanPhamId( @RequestParam("donHangSanPhamID") int donHangSanPhamID) {
        FileResourceDTO resource = duLieuService.downloadFileHexByDonHangSanPhamId(donHangSanPhamID);
        return ResponseObject.<FileResourceDTO>builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(resource)
                .build();
    }

    @GetMapping("/download-list-file-import-file-by-don-hang-san-pham-id")
    public ResponseObject<List<FileResourceDTO>> downloadListFileImportFileByDonHangSanPhamId(@RequestParam("donHangID") int donHangID) {
        List<FileResourceDTO> resources = duLieuService.downloadFileListImportFileByDonHangSanPhamId(donHangID);
        return ResponseObject.<List<FileResourceDTO>>builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(resources)
                .build();
    }

    @GetMapping("/download-file-import-file-by-don-hang-san-pham-id")
    public ResponseObject<FileResourceDTO> downloadFileImportFileByDonHangSanPhamId(@RequestParam("donHangSanPhamID") int donHangSanPhamID) {
        FileResourceDTO resource = duLieuService.downloadFileImportFileByDonHangSanPhamId(donHangSanPhamID);
        return ResponseObject.<FileResourceDTO>builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(resource)
                .build();
    }

    @GetMapping("/find-by-epc")
    public ResponseObject<DuLieuDTO> findByEpc(@RequestParam("epc") String epc) {
        DuLieuDTO duLieuDTO = duLieuService.findByEpc(epc);
        return ResponseObject.<DuLieuDTO>builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(duLieuDTO)
                .build();
    }

    @GetMapping("/decode-epc")
    public ResponseObject<SanPham> decodeEpc(@RequestParam("epc") String epc) {
        SanPham sanPham = duLieuService.decoderEpc(epc);
        return ResponseObject.<SanPham>builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(sanPham)
                .build();
    }

    @GetMapping("/encode-epc")
    public ResponseObject<String> encodeEpc(@RequestParam("upc") String upc,
                                            @RequestParam("partition") int partition,
                                            @RequestParam("filter") int filter,
                                            @RequestParam("serial") long serial) {
        String epc = duLieuService.encoderEpc(upc, partition, filter, serial);
        return ResponseObject.<String>builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(epc)
                .build();
    }

    @GetMapping("/convert-to-hex")
    public ResponseObject<String> convertToHex(@RequestParam("chuoi") String chuoi) {
        String hex = duLieuService.convertoHex(chuoi);
        return ResponseObject.<String>builder()
                .status(HttpStatus.OK)
                .message("Tạo File dữ liệu thành công")
                .data(hex)
                .build();
    }

    @ExceptionHandler(CustomException.class)
    public ResponseObject<?> handleCustomException(CustomException e) {
        return ResponseObject.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();
    }
}
