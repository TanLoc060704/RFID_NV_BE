package namviet.rfid_api.api;

import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.entity.AccountE;
import namviet.rfid_api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/private")
public class HomeApi {

    @Autowired
    AccountService accountService;

    @GetMapping("/test")
    public String home() {
        AccountE account = accountService.findByAccountId(2);
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
