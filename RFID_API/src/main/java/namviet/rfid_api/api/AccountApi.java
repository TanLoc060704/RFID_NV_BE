package namviet.rfid_api.api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import namviet.rfid_api.dto.AccountDTO;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.service.AccountService;
import namviet.rfid_api.constant.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private/account")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountApi {

    private final AccountService accountService;

    @ExceptionHandler(CustomException.class)
    public ResponseObject<?> handleCustomException(CustomException e) {
        return ResponseObject.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .build();
    }

    @PostMapping
    public ResponseObject<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        AccountDTO createdAccount = accountService.createAccount(accountDTO);
        return ResponseObject.<AccountDTO>builder()
                .status(HttpStatus.CREATED)
                .message("Account created successfully")
                .data(createdAccount)
                .build();
    }

    @PutMapping("/{accountId}")
    public ResponseObject<AccountDTO> updateAccount(@PathVariable int accountId, @RequestBody AccountDTO accountDTO) {
        AccountDTO updatedAccount = accountService.updateAccount(accountId, accountDTO);
        return ResponseObject.<AccountDTO>builder()
                .status(HttpStatus.OK)
                .message("Account updated successfully")
                .data(updatedAccount)
                .build();
    }

    @DeleteMapping("/{accountId}")
    public ResponseObject<String> deleteAccount(@PathVariable int accountId) {
        accountService.deleteAccount(accountId);
        return ResponseObject.<String>builder()
                .status(HttpStatus.NO_CONTENT)
                .message("Account deleted successfully")
                .build();
    }

    @GetMapping("/{accountId}")
    public ResponseObject<AccountDTO> getAccountById(@PathVariable int accountId) {
        AccountDTO accountDTO = accountService.getAccountById(accountId);
        return ResponseObject.<AccountDTO>builder()
                .status(HttpStatus.OK)
                .message("Account fetched successfully")
                .data(accountDTO)
                .build();
    }

    @GetMapping
    public ResponseObject<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseObject.<List<AccountDTO>>builder()
                .status(HttpStatus.OK)
                .message("Accounts fetched successfully")
                .data(accounts)
                .build();
    }
}
