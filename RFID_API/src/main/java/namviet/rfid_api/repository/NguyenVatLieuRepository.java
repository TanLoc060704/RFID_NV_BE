package namviet.rfid_api.repository;

import namviet.rfid_api.entity.NguyenVatLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NguyenVatLieuRepository extends JpaRepository<NguyenVatLieu,Integer> {

    boolean existsByCode(String code);
}
