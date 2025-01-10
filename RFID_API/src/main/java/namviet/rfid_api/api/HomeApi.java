package namviet.rfid_api.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.DonHangDTO;
import namviet.rfid_api.entity.Account;
import namviet.rfid_api.entity.DonHang;
import namviet.rfid_api.entity.DonHangSanPham;
import namviet.rfid_api.repository.DonHangRepository;
import namviet.rfid_api.service.AccountService;
import namviet.rfid_api.service.DonHangSanPhamService;
import namviet.rfid_api.service.DonHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/private")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomeApi {

    final AccountService accountService;


    @GetMapping("/test")
    public String home() {
        Account account = accountService.findByAccountId(2);
        return account.getEmail();
    }

    @GetMapping("/home")
    public ResponseObject<?> test() {
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("da Vao")
                .data("123")
                .build();
    }
}
