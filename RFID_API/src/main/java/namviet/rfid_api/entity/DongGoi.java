package namviet.rfid_api.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dong_goi")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DongGoi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dong_goi_id")
    Integer dongGoiId;

    @Column(name = "so_pcs_tot", nullable = false)
    Integer soPcsTot;

    @Column(name = "so_pcs_hu", nullable = false, columnDefinition = "int default 0")
    Integer soPcsHu = 0;

    @ManyToOne
    @JoinColumn(name = "don_hang_san_pham_id_dong_goi", referencedColumnName = "don_hang_san_pham_id")
    DonHangSanPham donHangSanPham;

    @ManyToOne
    @JoinColumn(name = "nhan_vien_id", referencedColumnName = "nhan_vien_id")
    NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "thiet_bi_id", referencedColumnName = "thiet_bi_id")
    ThietBi thietBi;

    @ManyToOne
    @JoinColumn(name = "ban_thanh_pham_id", referencedColumnName = "ban_thanh_pham_id")
    BanThanhPham banThanhPham;

    @Column(name = "code", insertable = false )
    String code;

    @Column(name = "ngay_lap", insertable = false )
    Date ngayLap;

    @Column(name = "index_cuon")
    int indexCuon;

}
