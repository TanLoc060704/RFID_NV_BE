package namviet.rfid_api.repository;

import namviet.rfid_api.entity.DonHangSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface DonHangSanPhamRepository extends JpaRepository<DonHangSanPham, Integer> {
    Optional<DonHangSanPham> findByDonHangSanPhamId(int DonHangSanPhamId);
    List<DonHangSanPham> findByDonHangMaLenhContaining(String maLenh);
    List<DonHangSanPham> findByDonHangDonHangId(int donHangId);
    boolean existsByTenFile(String tenFile);

    @Modifying
    @Query("UPDATE DonHangSanPham d SET d.soLanTao = d.soLanTao + 1 WHERE d.donHangSanPhamId = :donHangSanPhamId")
    void updateSoLanTao(int donHangSanPhamId);
}
