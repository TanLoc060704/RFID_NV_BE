package namviet.rfid_api.repository;

import namviet.rfid_api.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham,String> {

    List<SanPham> findBySkuContaining(String sku);

    List<SanPham> findBySkuIn(List<String> skuList);

    SanPham findBySku(String sku);

    @Override
    Page<SanPham> findAll(Pageable pageable);

    @Query(value = "SELECT sp.*, u.upc AS upc_code " +
            "FROM san_pham sp " +
            "JOIN upc u ON sp.upc_id = u.upc_id " +
            "WHERE CONTAINS((sp.sku, sp.content, sp.inlay), :searchText) " +
            "OR CONTAINS(u.upc, :searchText) " +
            "ORDER BY sp.sku DESC", nativeQuery = true)
    Page<SanPham> searchSPWithFTS(@Param("searchText") String searchText,Pageable pageable);
}
