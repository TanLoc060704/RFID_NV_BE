package namviet.rfid_api.repository;

import namviet.rfid_api.entity.LuuMau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LuuMauRepository extends JpaRepository<LuuMau, Integer> {
}
