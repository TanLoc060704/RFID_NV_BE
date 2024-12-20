package namviet.rfid_api.repository;

import namviet.rfid_api.entity.AccountE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AccountE, Integer> {
    Optional<AccountE> findByUserName(String userName);
    Optional<AccountE> findByUserNameAndIsActiveTrue(String userName);
}
