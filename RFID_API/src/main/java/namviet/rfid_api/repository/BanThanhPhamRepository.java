package namviet.rfid_api.repository;

import namviet.rfid_api.entity.BanThanhPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BanThanhPhamRepository extends JpaRepository<BanThanhPham,Integer> {
    @Override
    Page<BanThanhPham> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM ban_thanh_pham WHERE CONTAINS(code, :searchText) ORDER BY ban_thanh_pham_id DESC ", nativeQuery = true)
    Page<BanThanhPham> searchWithFTS(@Param("searchText") String searchText, Pageable pageable);

}
