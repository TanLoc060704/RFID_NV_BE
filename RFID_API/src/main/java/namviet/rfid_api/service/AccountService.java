package namviet.rfid_api.service;

import namviet.rfid_api.dto.AccountDTO;
import namviet.rfid_api.entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AccountService {
    Account findByUserName(String username);
    Account findByAccountId(int accountId);

    AccountDTO createAccount(AccountDTO accountDTO);
    AccountDTO updateAccount(int accountId, AccountDTO accountDTO);
    void deleteAccount(int accountId);
    AccountDTO getAccountById(int accountId);
    List<AccountDTO> getAllAccounts();
}
