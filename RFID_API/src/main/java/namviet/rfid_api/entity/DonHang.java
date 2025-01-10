package namviet.rfid_api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "don_hang")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class    DonHang {

    public DonHang(Integer donHangId) {
        this.donHangId = donHangId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "don_hang_id")
    Integer donHangId;

    @Column(name = "ma_lenh", nullable = false, length = 50)
    String maLenh;

    @Column(name = "po", nullable = false, length = 50)
    String po;

    @Column(name = "total", nullable = false)
    Integer total;

    @Column(name = "loai_data", nullable = false, length = 50)
    String loaiData;

    @ManyToOne
    @JoinColumn(name = "nhan_vien_id", referencedColumnName = "nhan_vien_id")
    NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "khach_hang_id", referencedColumnName = "khach_hang_id")
    KhachHang khachHang;
}
