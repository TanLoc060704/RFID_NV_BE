package namviet.rfid_api.repository;

import namviet.rfid_api.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUserName(String userName);
    Optional<Account> findByUserNameAndIsActiveTrue(String userName);

    Account findByAccountId(int accountID);
}
