package namviet.rfid_api.serviceImpl;

import namviet.rfid_api.entity.AccountE;
import namviet.rfid_api.repository.UserRepository;
import namviet.rfid_api.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    public AccountServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private final UserRepository userRepository;

    @Override
    public AccountE findByUserName(String username) {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public AccountE findByAccountId(int accountId) {
        return userRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

