package namviet.rfid_api.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.DonHangSanPhamDTO;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.DonHangSanPhamService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/private")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DonHangSanPhamAPI {

    final DonHangSanPhamService donHangSanPhamService;

    @GetMapping("/tim-don-hang-san-pham")
    public ResponseObject<?> getDonHangSanPhamByMaLenh(@RequestParam("ma-lenh") String maLenh) {
        List<DonHangSanPhamDTO> dsDonHangSanPham = donHangSanPhamService.dSSanPhamTheoDonHang(maLenh);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Thông tin đơn hàng")
                .data(dsDonHangSanPham)
                .build();
    }

    @PostMapping("/them-san-pham-vao-don-hang-moi")
    public ResponseObject<?> themSanPhamVaoDonHangNew(@RequestBody List<DonHangSanPhamDTO> dsDonHangSanPhamDTO) {
        List<DonHangSanPhamDTO> dsDonHangSanPham = donHangSanPhamService.themSPVaoDonHangNew(dsDonHangSanPhamDTO);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Thêm sản phẩm vào đơn hàng thành công")
                .data(dsDonHangSanPham)
                .build();
    }

    @PostMapping("/them-san-pham-file")
    public ResponseObject<?> themSanPhamVaoDonHangFile(@RequestParam("file") MultipartFile file) {
//        List<DonHangSanPhamDTO> dsDonHangSanPham = donHangSanPhamService.themSPVaoDonHangFile(file);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Thêm sản phẩm vào đơn hàng thành công")
                .data(null)
                .build();
    }


    @PutMapping("/cap-nhat-don-hang-san-pham")
    public ResponseObject<?> capNhatDonHangSanPham(@RequestBody DonHangSanPhamDTO donHangSanPham) {
        DonHangSanPhamDTO donHangSanPhamNew = donHangSanPhamService.capNhatDonHangSanPham(donHangSanPham);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Cập nhật thông tin đơn hàng san pham thành công")
                .data(donHangSanPhamNew)
                .build();
    }

    @GetMapping("/tim-don-hang-san-pham-theo-id")
    public ResponseObject<?> getDonHangSanPhamByID(@RequestParam("dh-sp-id") int donHangSanPhamId) {
        Optional<DonHangSanPhamDTO> donHangSanPham = donHangSanPhamService.timDonHangSanPham(donHangSanPhamId);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Thông tin đơn hàng sản phẩm")
                .data(donHangSanPham)
                .build();
    }

    @GetMapping("/tim-don-hang-don-hang-id")
    public ResponseObject<?> getDonHangDonHangID(@RequestParam("dh-id") int donHangSanPhamId) {
        Optional<List<DonHangSanPhamDTO>> donHangSanPham = donHangSanPhamService.timDonHangTheoDonHangId(donHangSanPhamId);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Thông tin đơn hàng theo don hang id")
                .data(donHangSanPham)
                .build();
    }

    @PostMapping("/import-file")
    public ResponseObject<?> importFile(@RequestParam("dsFile") List<MultipartFile> dsFile, @RequestParam("maLenh") String maLenh,@RequestParam("sku") String sku, @RequestParam("viTriEPC") int viTriEpc){
        try {
            List<DonHangSanPhamDTO> dsDonHangSanPham = donHangSanPhamService.importFile(dsFile,maLenh,sku,viTriEpc);
            return ResponseObject.builder()
                    .status(HttpStatus.OK)
                    .message("Import thanh công")
                    .data(dsDonHangSanPham)
                    .build();
        } catch (CustomException a){
          throw a;
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST) ;
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
