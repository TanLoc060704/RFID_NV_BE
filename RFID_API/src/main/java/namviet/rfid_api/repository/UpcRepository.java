package namviet.rfid_api.repository;

import namviet.rfid_api.entity.Upc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpcRepository extends JpaRepository<Upc,Integer> {
}
