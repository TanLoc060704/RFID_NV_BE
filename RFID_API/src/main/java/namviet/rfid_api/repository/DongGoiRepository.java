package namviet.rfid_api.repository;

import namviet.rfid_api.entity.DongGoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DongGoiRepository extends JpaRepository<DongGoi,Integer> {
}
