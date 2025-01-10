package namviet.rfid_api.serviceImpl;

import lombok.RequiredArgsConstructor;
import namviet.rfid_api.dto.AccountDTO;
import namviet.rfid_api.entity.Account;
import namviet.rfid_api.entity.Role;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.mapper.AccountMapper;
import namviet.rfid_api.repository.RoleRepository;
import namviet.rfid_api.repository.UserRepository;
import namviet.rfid_api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    private final RoleRepository roleRepository;

    @Override
    public Account findByUserName(String username) {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public Account findByAccountId(int accountId) {
        return userRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public AccountDTO createAccount(AccountDTO accountDTO) {
        System.out.println(accountDTO.getIsActive());
        Account account = accountMapper.toEntity(accountDTO);
        System.out.println(account.isActive());
        try{
//            account = userRepository.save(account);
        }catch(Exception e){
            throw new CustomException("Username Đã Tồn Tại",HttpStatus.BAD_REQUEST);
        }
        return accountMapper.toDto(account);
    }

    @Override
    @Transactional
    public AccountDTO updateAccount(int accountId, AccountDTO accountDTO) {
        Account existingAccount = userRepository.findById(accountId)
                .orElseThrow(() -> new CustomException("Account not found", HttpStatus.NOT_FOUND));

        Role role = roleRepository.findById(accountDTO.getRoleId())
                .orElseThrow(() -> new CustomException("Role not found", HttpStatus.NOT_FOUND));

        existingAccount.setUserName(accountDTO.getUserName());
        existingAccount.setEmail(accountDTO.getEmail());
        existingAccount.setPassword(accountDTO.getPassword());
        existingAccount.setRole(role);
        existingAccount.setActive(accountDTO.getIsActive());

        existingAccount = userRepository.save(existingAccount);

        return accountMapper.toDto(existingAccount);
    }


    @Override
    public void deleteAccount(int accountId) {
        Account account = userRepository.findById(accountId)
                .orElseThrow(() -> new CustomException("Account not found", HttpStatus.NOT_FOUND));
        userRepository.delete(account);
    }

    @Override
    public AccountDTO getAccountById(int accountId) {
        Account account = userRepository.findById(accountId)
                .orElseThrow(() -> new CustomException("Account not found", HttpStatus.NOT_FOUND));
        return accountMapper.toDto(account);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = userRepository.findAll();
        return accounts.stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }
}

