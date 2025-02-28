package namviet.rfid_api.repository;

import namviet.rfid_api.entity.DonHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang,Integer> {

    List<DonHang> findAll();
    List<DonHang> findByMaLenhContaining(String meLenh);
    DonHang findByDonHangId(int donHangId);
    DonHang findByMaLenh(String maLenh);

    @Override
    Page<DonHang> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM don_hang WHERE CONTAINS(ma_lenh, :searchText) ORDER BY don_hang_id DESC ", nativeQuery = true)
    Page<DonHang> searchWithFTS(@Param("searchText") String searchText, Pageable pageable);
}
