package namviet.rfid_api.repository;

import namviet.rfid_api.entity.DonHangSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonHangSanPhamRepository extends JpaRepository<DonHangSanPham, Integer> {
    Optional<DonHangSanPham> findByDonHangSanPhamId(int DonHangSanPhamId);
    List<DonHangSanPham> findByDonHangMaLenhContaining(String maLenh);
    List<DonHangSanPham> findByDonHangDonHangId(int donHangId);

}
