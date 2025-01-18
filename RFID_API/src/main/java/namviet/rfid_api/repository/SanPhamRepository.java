package namviet.rfid_api.repository;

import namviet.rfid_api.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham,String> {

    List<SanPham> findBySkuContaining(String sku);

    List<SanPham> findBySkuIn(List<String> skuList);

    SanPham findBySku(String sku);
}
