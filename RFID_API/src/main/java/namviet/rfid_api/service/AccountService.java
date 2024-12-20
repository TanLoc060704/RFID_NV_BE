package namviet.rfid_api.service;

import namviet.rfid_api.entity.AccountE;

public interface AccountService {
    AccountE findByUserName(String username);
    AccountE findByAccountId(int accountId);
}
