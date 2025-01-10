package namviet.rfid_api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Table(name = "don_hang_san_pham")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DonHangSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "don_hang_san_pham_id")
    Integer donHangSanPhamId;

    @ManyToOne
    @JoinColumn(name = "don_hang_id", nullable = false)
    DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "sku", nullable = false)
    SanPham sanPham;

    @Column(name = "ten_file", nullable = false, unique = true)
    String tenFile;

    @Column(name = "so_luong", nullable = false)
    Integer soLuong;

}
