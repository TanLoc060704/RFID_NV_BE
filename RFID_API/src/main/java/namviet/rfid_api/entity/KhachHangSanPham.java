package namviet.rfid_api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KhachHangSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "khach_hang_san_pham")
    int khachHangSanPhamId;

    @ManyToOne
    @JoinColumn(name = "khach_hang_id")
    KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "sku")
    SanPham sanPham;
}
