package namviet.rfid_api.repository;

import namviet.rfid_api.entity.BanThanhPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BanThanhPhamRepository extends JpaRepository<BanThanhPham,Integer> {

}
