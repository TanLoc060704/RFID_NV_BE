package namviet.rfid_api.repository;

import namviet.rfid_api.entity.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang,Integer> {

    List<DonHang> findAll();
    List<DonHang> findByMaLenhContaining(String meLenh);
    DonHang findByDonHangId(int donHangId);
}
