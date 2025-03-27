package namviet.rfid_api.repository;

import namviet.rfid_api.entity.Dulieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DuLieuRepository extends JpaRepository<Dulieu,Integer> {

    List<Dulieu> findByDonHangSanPhamDonHangSanPhamId(int donHangSanPhamId);
    Optional<Dulieu> findByEpc(String epc);
}
