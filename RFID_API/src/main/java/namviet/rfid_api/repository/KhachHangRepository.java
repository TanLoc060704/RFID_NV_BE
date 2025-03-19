package namviet.rfid_api.repository;

import namviet.rfid_api.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
    Optional<KhachHang> findByTenKhachHang(String tenKhachHang);
}
