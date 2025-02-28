package namviet.rfid_api.repository;

import namviet.rfid_api.entity.DongGoi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DongGoiRepository extends JpaRepository<DongGoi,Integer> {

    List<DongGoi> findByDonHangSanPhamDonHangMaLenhContaining(String maLenh);

    @Override
    Page<DongGoi> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM dong_goi WHERE CONTAINS(code, :searchText) ORDER BY dong_goi_id DESC ", nativeQuery = true)
    Page<DongGoi> searchWithFTS(@Param("searchText") String searchText, Pageable pageable);
}
