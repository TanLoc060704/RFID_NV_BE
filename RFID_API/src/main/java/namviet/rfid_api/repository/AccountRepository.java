package namviet.rfid_api.repository;

import namviet.rfid_api.entity.Account;
import namviet.rfid_api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
    boolean existsByUserName(String userName);
}
